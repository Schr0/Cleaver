package schr0.cleaver.init;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import schr0.cleaver.api.ICleaverItem;

public class CleaverEvent
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

		if (isInvalidAttack(target, damageSource))
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
				float attackAmmount = cleaverItem.getAttackAmmount(event.getAmount(), stackMainHand, target, attacker);
				boolean isCriticalAttack = (0.0F < attacker.fallDistance) && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(MobEffects.BLINDNESS) && !attacker.isRiding();

				event.setCanceled(true);

				if (isCriticalAttack)
				{
					attackAmmount *= 1.5F;
				}

				if (cleaverItem.onAttackTarget(stackMainHand, target, attacker, attackAmmount, cleaverItem.isCleaveTarget(stackMainHand, target, attacker, attackAmmount)))
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
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		EntityLivingBase target = event.getEntityLiving();
		DamageSource damageSource = event.getSource();

		if (damageSource instanceof CleaverDamageSource)
		{
			CleaverDamageSource cleaverDamageSource = (CleaverDamageSource) damageSource;
			EntityLivingBase attacker = cleaverDamageSource.getAttacker();

			event.setCanceled(true);

			if (attacker instanceof EntityPlayer)
			{
				onDeathByPlayer(target, (EntityPlayer) attacker);
			}
			else
			{
				target.onDeath(DamageSource.causeMobDamage(attacker));
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static boolean isInvalidAttack(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote)
		{
			return true;
		}

		if ((target == null) || (target != null && (target.isDead || target.getHealth() <= 0)))
		{
			return true;
		}

		return (damageSource instanceof EntityDamageSourceIndirect);
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

	private static void onDeathByPlayer(EntityLivingBase target, EntityPlayer player)
	{
		/*
				try
				{
					Field f1 = EntityLivingBase.class.getDeclaredFields()[37];
					Field f2 = EntityLivingBase.class.getDeclaredFields()[38];
		
					f1.setAccessible(true);
					f2.setAccessible(true);
		
					FMLLog.info("%s", f1.getName() + " : " + f2.getName());
				}
				catch (Exception e)
				{
					throw new UnableToAccessFieldException(new String[0], e);
				}
		//*/

		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, player, 37/*"attackingPlayer"*/);
		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, 100, 38/*"recentlyHit"*/);

		target.onDeath(DamageSource.causePlayerDamage(player));
	}

}
