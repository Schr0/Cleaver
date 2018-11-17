package schr0.cleaver;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import schr0.cleaver.api.ICleaverItem;

public class CleaverEvents
{

	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onAttackTargetEvent(LivingAttackEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidAttackTarget(target, damageSource))
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

				if (cleaverItem.onAttackTarget(attackAmmount, cleaverItem.canCleaveTarget(attackAmmount, stackMainHand, target, attacker), stackMainHand, target, attacker))
				{
					target.hurtResistantTime = 0;

					target.attackEntityFrom(getCleaverDamageSource(damageSource, target, attacker), attackAmmount);

					if (attacker instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) attacker;

						if (isCriticalAttack)
						{
							player.onCriticalHit(target);
						}

						player.resetCooldown();
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onDeathTargetEvent(LivingDeathEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidDeathTarget(target, damageSource))
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

				// TODO Reflection : forge1.12-14.21.1.2387
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
	public void onDropsTargetEvent(LivingDropsEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (isInvalidDropsTarget(target, event.getSource()))
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
				List<EntityItem> drops = cleaverItem.getDropsTarget(Lists.newArrayList(event.getDrops()), stackMainHand, target, attacker);

				event.getDrops().clear();

				event.getDrops().addAll(drops);
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static boolean isInvalidAttackTarget(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote)
		{
			return true;
		}

		if ((target == null) || (target != null && (target.isDead || target.getHealth() <= 0)))
		{
			return true;
		}

		if (damageSource instanceof EntityDamageSourceIndirect)
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidDeathTarget(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote)
		{
			return true;
		}

		if (damageSource instanceof EntityDamageSourceIndirect)
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidAttackOwner(EntityLivingBase owner)
	{
		if (owner.getEntityWorld().isRemote)
		{
			return true;
		}

		if ((owner == null) || (owner != null && (owner.isDead || owner.getHealth() <= 0)))
		{
			return true;
		}

		return false;
	}

	private static boolean isInvalidHurtOwner(EntityLivingBase owner)
	{
		return isInvalidAttackOwner(owner);
	}

	private static boolean isInvalidDropsTarget(EntityLivingBase target, DamageSource damageSource)
	{
		return isInvalidDeathTarget(target, damageSource);
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

}
