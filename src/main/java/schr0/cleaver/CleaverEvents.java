package schr0.cleaver;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import schr0.cleaver.api.item.ICleaverItem;

public class CleaverEvents
{

	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onLivingAttackEvent(LivingAttackEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidLivingAttackEvent(target, damageSource))
		{
			return;
		}

		if (damageSource.getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
			ItemStack stackMainHand = attacker.getHeldItemMainhand();

			if (stackMainHand.getItem() instanceof ICleaverItem)
			{
				ICleaverItem cleaverItem = (ICleaverItem) stackMainHand.getItem();
				boolean isCriticalAttack = (0.0F < attacker.fallDistance) && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(MobEffects.BLINDNESS) && !attacker.isRiding();

				float attackAmmount;

				if (isCriticalAttack)
				{
					attackAmmount = cleaverItem.getAttackAmmount((event.getAmount() * 1.5F), stackMainHand, target, attacker);
				}
				else
				{
					attackAmmount = cleaverItem.getAttackAmmount(event.getAmount(), stackMainHand, target, attacker);
				}

				event.setCanceled(true);

				if (cleaverItem.onAttackTarget(attackAmmount, cleaverItem.canChopTarget(attackAmmount, stackMainHand, target, attacker), stackMainHand, target, attacker))
				{
					if (attacker instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) attacker;

						if (isCriticalAttack)
						{
							player.onCriticalHit(target);
						}

						player.resetCooldown();
					}

					target.hurtResistantTime = 0;

					target.attackEntityFrom(getCleaverDamageSource(damageSource, target, attacker), attackAmmount);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidLivingDeathEvent(target, damageSource))
		{
			return;
		}

		if (damageSource instanceof CleaverDamageSource)
		{
			CleaverDamageSource cleaverDamageSource = (CleaverDamageSource) damageSource;
			EntityLivingBase attacker = cleaverDamageSource.getAttacker();

			event.setCanceled(true);

			if (attacker instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) attacker;

				// TODO Reflection : forge-1.12.2-14.23.5.2768
				ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, player, "attackingPlayer", "field_70717_bb");
				ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, 100, "recentlyHit", "field_70718_bc");

				target.onDeath(DamageSource.causePlayerDamage(player));
			}
			else
			{
				target.onDeath(DamageSource.causeMobDamage(attacker));
			}
		}
	}

	@SubscribeEvent
	public void onLivingDropsEvent(LivingDropsEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidLivingDropsEvent(target, event.getSource()))
		{
			return;
		}

		if (damageSource.getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
			ItemStack stackMainHand = attacker.getHeldItemMainhand();

			if (stackMainHand.getItem() instanceof ICleaverItem)
			{
				ICleaverItem cleaverItem = (ICleaverItem) stackMainHand.getItem();
				List<EntityItem> drops = cleaverItem.getDeathTargetDrops(Lists.newArrayList(event.getDrops()), stackMainHand, target, attacker);

				event.getDrops().clear();

				event.getDrops().addAll(drops);
			}
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();

		if (isInvalidEntityJoinWorldEvent(entity))
		{
			return;
		}

		if (EntityList.getKey(EntityZombie.class).equals(EntityList.getKey(entity)))
		{
			EntityZombie zombie = (EntityZombie) entity;
			ItemStack stackMainHand = zombie.getHeldItemMainhand();

			if (stackMainHand.isEmpty() && canZombieHeldItemCleaver(zombie))
			{
				zombie.setHeldItem(EnumHand.MAIN_HAND, getZombieCleaver(zombie));
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static boolean isInvalidEntityJoinWorldEvent(Entity target)
	{
		if (target.getEntityWorld().isRemote || (target == null))
		{
			return true;
		}

		if (target != null && target.isDead)
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidLivingAttackEvent(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote || (target == null) || (damageSource == null))
		{
			return true;
		}

		if ((target != null) && (target.isDead || target.getHealth() <= 0))
		{
			return true;
		}

		if (damageSource instanceof EntityDamageSourceIndirect)
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidLivingDeathEvent(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote || (target == null) || (damageSource == null))
		{
			return true;
		}

		if (damageSource instanceof EntityDamageSourceIndirect)
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidLivingDropsEvent(EntityLivingBase target, DamageSource damageSource)
	{
		return isInvalidLivingDeathEvent(target, damageSource);
	}

	private static CleaverDamageSource getCleaverDamageSource(DamageSource damageSource, EntityLivingBase target, EntityLivingBase attacker)
	{
		CleaverDamageSource cleaverDamageSource = new CleaverDamageSource(attacker);

		if (target instanceof EntityDragon)
		{
			cleaverDamageSource.setExplosion();
		}

		if (damageSource.isProjectile())
		{
			cleaverDamageSource.setProjectile();
		}

		if (damageSource.isExplosion())
		{
			cleaverDamageSource.setExplosion();
		}

		if (damageSource.isUnblockable())
		{
			cleaverDamageSource.setDamageBypassesArmor();
		}

		if (damageSource.canHarmInCreative())
		{
			cleaverDamageSource.setDamageAllowedInCreativeMode();
		}

		if (damageSource.isDamageAbsolute())
		{
			cleaverDamageSource.setDamageIsAbsolute();
		}

		if (damageSource.isFireDamage())
		{
			cleaverDamageSource.setFireDamage();
		}

		if (damageSource.isDifficultyScaled())
		{
			cleaverDamageSource.setDifficultyScaled();
		}

		if (damageSource.isMagicDamage())
		{
			cleaverDamageSource.setMagicDamage();
		}

		return cleaverDamageSource;
	}

	private static boolean canZombieHeldItemCleaver(EntityZombie zombie)
	{
		World world = zombie.getEntityWorld();
		float chance;

		switch (world.getDifficulty())
		{
			case EASY :

				chance = 0.03F;

				break;

			case NORMAL :

				chance = 0.04F;

				break;

			case HARD :

				chance = 0.05F;

				break;

			default :

				chance = 0.02F;

				break;
		}

		// return true;
		return (world.rand.nextFloat() < chance);
	}

	private static ItemStack getZombieCleaver(EntityZombie zombie)
	{
		ItemStack stack = new ItemStack(CleaverItems.CLEAVER);
		World world = zombie.getEntityWorld();
		Random random = world.rand;
		float additionalDifficulty = world.getDifficultyForLocation(zombie.getPosition()).getClampedAdditionalDifficulty();

		if (random.nextFloat() < (0.25F * additionalDifficulty))
		{
			int level = (int) (5.0F + additionalDifficulty * (float) random.nextInt(18));

			EnchantmentHelper.addRandomEnchantment(random, stack, level, false);
		}

		return stack;
	}

}
