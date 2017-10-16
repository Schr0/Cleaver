package schr0.cleaver.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.init.CleaverParticles;
import schr0.cleaver.packet.particleentity.MessageParticleEntity;
import schr0.cleaver.packet.particleposition.MessageParticlePosition;

public class ItemCleaverBlazeHelper
{

	private static final int POTION_DURATION = 100;
	private static final int POTION_DURATION_MAX = (60 * 20);
	private static final int POTION_DURATION_MIN = (20 * 20);
	private static final int POTION_DURATION_LIMIT = (11 * 20);
	private static final Set<Block> EVAPORATION_BLOCKS = Sets.newHashSet(new Block[]
	{
			Blocks.WATER, Blocks.FLOWING_WATER,
			Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE,
			Blocks.SNOW, Blocks.SNOW_LAYER,
	});

	public static void onCleaveGoodPotions(int heatAmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		Random random = getRandom(attacker);
		ArrayList<Potion> activeGoodPotions = new ArrayList<Potion>();
		ArrayList<Potion> nonActiveGoodPotions = new ArrayList<Potion>();

		for (Potion potion : Potion.REGISTRY)
		{
			if (isInvalidPotion(potion))
			{
				continue;
			}

			if (!potion.isBadEffect())
			{
				if (attacker.isPotionActive(potion))
				{
					activeGoodPotions.add(potion);
				}
				else
				{
					nonActiveGoodPotions.add(potion);
				}
			}
		}

		if (activeGoodPotions.size() < 5)
		{
			if (!nonActiveGoodPotions.isEmpty())
			{
				Potion potion = nonActiveGoodPotions.get(random.nextInt(nonActiveGoodPotions.size()));
				int duration = (heatAmount * POTION_DURATION);
				duration = Math.min(duration, POTION_DURATION_MAX);
				duration = Math.max(duration, POTION_DURATION_MIN);

				attacker.addPotionEffect(new PotionEffect(potion, (20 + duration)));
			}
		}
		else
		{
			if (!activeGoodPotions.isEmpty())
			{
				boolean successIncrease = false;

				do
				{
					Potion potion = activeGoodPotions.get(random.nextInt(activeGoodPotions.size()));
					PotionEffect potionEffect = attacker.getActivePotionEffect(potion);
					int amplifier = potionEffect.getAmplifier();

					++amplifier;

					if (amplifier < 3)
					{
						attacker.addPotionEffect(new PotionEffect(potion, potionEffect.getDuration(), amplifier));

						successIncrease = true;
					}
					else
					{
						successIncrease = false;
					}

					int limitIncreasePotions = 0;

					for (Potion activePotion : activeGoodPotions)
					{
						if (2 <= attacker.getActivePotionEffect(potion).getAmplifier())
						{
							++limitIncreasePotions;
						}
					}

					if (activeGoodPotions.size() <= limitIncreasePotions)
					{
						successIncrease = true;
					}
				}
				while (!successIncrease);
			}
		}

		target.clearActivePotions();
	}

	public static void onBlazeRestraint(int heatAmount, ItemStack stack, EntityLivingBase attacker, int chageAmount)
	{
		for (EntityLivingBase aroundEntityLivingBase : getAroundEntityLivingBase(attacker, chageAmount))
		{
			if (aroundEntityLivingBase.isEntityAlive())
			{
				aroundEntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 10, false, false));

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(aroundEntityLivingBase, CleaverParticles.ENTITY_BLAZE_SHIELD));
			}
		}
	}

	public static void onBlazeAttack(int heatAmount, ItemStack stack, EntityLivingBase attacker, int chageAmount)
	{
		World world = attacker.getEntityWorld();

		for (EntityLivingBase aroundEntityLivingBase : getAroundEntityLivingBase(attacker, chageAmount))
		{
			aroundEntityLivingBase.attackEntityFrom(DamageSource.causeExplosionDamage(attacker), heatAmount);

			aroundEntityLivingBase.motionY += 0.5D;

			aroundEntityLivingBase.hurtResistantTime = 0;

			world.createExplosion(attacker, aroundEntityLivingBase.posX, aroundEntityLivingBase.posY, aroundEntityLivingBase.posZ, 1.0F, false);
		}
	}

	public static void onUpdateBlazeShield(int heatAmount, World world, ItemStack stack, EntityLivingBase owner)
	{
		if (owner.isBurning())
		{
			owner.extinguish();
		}

		for (Potion potion : Potion.REGISTRY)
		{
			if (isInvalidPotion(potion))
			{
				continue;
			}

			if (owner.isPotionActive(potion))
			{
				if (potion.isBadEffect())
				{
					owner.removePotionEffect(potion);
				}
				else
				{
					PotionEffect potionEffect = owner.getActivePotionEffect(potion);

					if (potionEffect.getDuration() < POTION_DURATION_LIMIT)
					{
						int duration = (potionEffect.getDuration() + (heatAmount * POTION_DURATION));
						duration = Math.min(duration, POTION_DURATION_MAX);
						duration = Math.max(duration, POTION_DURATION_MIN);

						owner.addPotionEffect(new PotionEffect(potion, (20 + duration), potionEffect.getAmplifier()));

						stack.damageItem(1, owner);
					}
				}
			}
		}

		BlockPos posOwner = new BlockPos(owner);
		float rangeXYZ = 1;
		int evaporationCount = 0;

		for (BlockPos posAround : BlockPos.getAllInBox(posOwner.add(-rangeXYZ, -rangeXYZ, -rangeXYZ), posOwner.add(rangeXYZ, rangeXYZ, rangeXYZ)))
		{
			Block block = world.getBlockState(posAround).getBlock();

			for (Block blockEvaporation : EVAPORATION_BLOCKS)
			{
				if (block.equals(blockEvaporation))
				{
					world.setBlockState(posAround, Blocks.AIR.getDefaultState(), 2);

					CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(posAround, CleaverParticles.POSITION_BLAZE_SMELTING));

					++evaporationCount;
				}
			}
		}

		if ((owner.isWet() && owner.ticksExisted % 20 == 0) || (0 < evaporationCount))
		{
			world.playEvent((EntityPlayer) null, 1009, new BlockPos(owner), 0);
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static Random getRandom(Entity owner)
	{
		return owner.getEntityWorld().rand;
	}

	private static boolean isInvalidPotion(Potion potion)
	{
		ArrayList<Potion> invalidPotions = Lists.newArrayList(MobEffects.HEALTH_BOOST, MobEffects.ABSORPTION, MobEffects.GLOWING);

		for (Potion invalidPotion : invalidPotions)
		{
			if (invalidPotion.getRegistryName().equals(potion.getRegistryName()))
			{
				return true;
			}
		}

		if (potion.isInstant())
		{
			return true;
		}

		return false;
	}

	private static List<EntityLivingBase> getAroundEntityLivingBase(EntityLivingBase owner, double rangeXYZ)
	{
		List<EntityLivingBase> listEntityLivingBase = Lists.newArrayList();

		for (EntityLivingBase aroundEntityLivingBase : owner.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, owner.getEntityBoundingBox().grow(rangeXYZ, rangeXYZ, rangeXYZ)))
		{
			if (aroundEntityLivingBase.equals(owner))
			{
				continue;
			}

			listEntityLivingBase.add(aroundEntityLivingBase);
		}

		return listEntityLivingBase;
	}

}
