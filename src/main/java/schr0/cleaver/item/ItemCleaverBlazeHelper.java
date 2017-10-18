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
import schr0.cleaver.packet.particleposition.MessageParticlePosition;

public class ItemCleaverBlazeHelper
{

	private static final int POTION_DURATION_MAX = (60 * 20);
	private static final int POTION_DURATION_MIN = (20 * 20);
	private static final int POTION_DURATION = 100;
	private static final int POTION_DURATION_LIMIT = (11 * 20);

	private static final Set<Block> EVAPORATION_BLOCKS = Sets.newHashSet(new Block[]
	{
			Blocks.WATER, Blocks.FLOWING_WATER,
			Blocks.ICE, Blocks.FROSTED_ICE, Blocks.PACKED_ICE,
			Blocks.SNOW, Blocks.SNOW_LAYER,
	});

	public static void cleavePotions(int heatAmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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

	public static void onUpdateRestraint(int chageAmount, ItemStack stack, EntityLivingBase owner, int timeCount)
	{
		for (EntityLivingBase aroundEntityLivingBase : getAroundEntityLivingBase(owner, chageAmount))
		{
			if (aroundEntityLivingBase.isEntityAlive())
			{
				int halfSec = (20 / 2);
				aroundEntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, halfSec, 10, false, false));
				aroundEntityLivingBase.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, halfSec, 10, false, false));

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(aroundEntityLivingBase.getPosition(), CleaverParticles.POSITION_BLAZE_AURA));
			}
		}

		World world = owner.getEntityWorld();
		BlockPos posOwner = owner.getPosition();
		List<BlockPos> posAuras = Lists.newArrayList();
		Iterable<BlockPos> posArounds = BlockPos.getAllInBox(posOwner.add(-chageAmount, -chageAmount, -chageAmount), posOwner.add(chageAmount, chageAmount, chageAmount));
		Iterable<BlockPos> posEraseArounds = BlockPos.getAllInBox(posOwner.add(-(chageAmount - 1), -(chageAmount - 1), -(chageAmount - 1)), posOwner.add((chageAmount - 1), (chageAmount - 1), (chageAmount - 1)));

		for (BlockPos posAura : posArounds)
		{
			if (posAura.equals(posOwner))
			{
				continue;
			}

			posAuras.add(posAura);
		}

		int erase = (chageAmount - 1);

		for (BlockPos posAura : posArounds)
		{
			for (BlockPos posErase : posEraseArounds)
			{
				if (posAura.equals(posErase))
				{
					posAuras.remove(posErase);
				}
			}
		}

		for (BlockPos posAura : posAuras)
		{
			if (timeCount % 2 == 0)
			{
				CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(posAura, CleaverParticles.POSITION_BLAZE_AURA));
			}
		}

		if (timeCount % 20 == 0)
		{
			world.playEvent(1009, posOwner, 0);
		}
	}

	public static void attackBlazeExplosion(int heatAmount, int chageAmount, ItemStack stack, EntityLivingBase attacker)
	{
		World world = attacker.getEntityWorld();

		for (EntityLivingBase aroundEntityLivingBase : getAroundEntityLivingBase(attacker, chageAmount))
		{
			aroundEntityLivingBase.hurtResistantTime = 0;

			aroundEntityLivingBase.attackEntityFrom(DamageSource.causeExplosionDamage(attacker), heatAmount);

			aroundEntityLivingBase.hurtResistantTime = 0;

			world.createExplosion(attacker, aroundEntityLivingBase.posX, aroundEntityLivingBase.posY, aroundEntityLivingBase.posZ, 1.0F, false);
		}

		evaporationBlock(stack, attacker, chageAmount, chageAmount);

		world.playEvent(1009, attacker.getPosition(), 0);
	}

	public static void onUpdateBlazeShield(int heatAmount, ItemStack stack, EntityLivingBase owner)
	{
		World world = owner.getEntityWorld();

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

		if (evaporationBlock(stack, owner, 0, 1))
		{
			world.playEvent(1009, owner.getPosition(), 0);
		}

		if (owner.isWet() && (owner.ticksExisted % 20 == 0))
		{
			world.playEvent(1009, owner.getPosition(), 0);
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

	private static boolean evaporationBlock(ItemStack stack, EntityLivingBase owner, int rangeXZ, int rangeY)
	{
		World world = owner.getEntityWorld();
		BlockPos posOwner = owner.getPosition();
		int evaporationCount = 0;

		for (BlockPos posAround : BlockPos.getAllInBox(posOwner.add(-rangeXZ, -rangeY, -rangeXZ), posOwner.add(rangeXZ, rangeY, rangeXZ)))
		{
			Block block = world.getBlockState(posAround).getBlock();

			for (Block blockEvaporation : EVAPORATION_BLOCKS)
			{
				if (block.equals(blockEvaporation))
				{
					world.setBlockState(posAround, Blocks.AIR.getDefaultState(), 2);

					CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(posAround, CleaverParticles.POSITION_BLAZE_AURA));

					++evaporationCount;
				}
			}
		}

		return (0 < evaporationCount);
	}

}
