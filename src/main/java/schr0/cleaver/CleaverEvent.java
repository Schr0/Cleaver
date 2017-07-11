package schr0.cleaver;

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
import schr0.cleaver.api.ItemCleaver;

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

		if (this.isInvalidAttack(target, damageSource))
		{
			return;
		}

		if (damageSource.getTrueSource() instanceof EntityLivingBase)
		{
			EntityLivingBase attacker = (EntityLivingBase) damageSource.getTrueSource();
			ItemStack stackMainHand = attacker.getHeldItemMainhand();

			if (stackMainHand.getItem() instanceof ItemCleaver)
			{
				ItemCleaver itemCleaver = (ItemCleaver) stackMainHand.getItem();
				float attackAmmount = itemCleaver.getAttackAmmount(event.getAmount(), stackMainHand, target, attacker);
				boolean isCriticalAttack = (0.0F < attacker.fallDistance) && !attacker.onGround && !attacker.isOnLadder() && !attacker.isInWater() && !attacker.isPotionActive(MobEffects.BLINDNESS) && !attacker.isRiding();

				if (isCriticalAttack)
				{
					attackAmmount *= 1.5F;

					if (attacker instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) attacker;

						if (isCriticalAttack)
						{
							player.onCriticalHit(target);
						}
					}
				}

				event.setCanceled(true);

				target.hurtResistantTime = 0;

				target.attackEntityFrom(getCleaverDamageSource(damageSource, target, attacker), attackAmmount);

				itemCleaver.onAttackTarget(stackMainHand, target, attacker, attackAmmount, itemCleaver.isCleaveTarget(stackMainHand, target, attacker, attackAmmount));
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
				onKillPlayer(target, (EntityPlayer) attacker);
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

	private static DamageSource getCleaverDamageSource(DamageSource damageSource, EntityLivingBase target, EntityLivingBase attacker)
	{
		DamageSource cleaver = new CleaverDamageSource(attacker);

		if (target instanceof EntityDragon)
		{
			cleaver.setExplosion();
		}

		if (damageSource.isProjectile())
		{
			cleaver.setProjectile();
		}

		if (damageSource.isExplosion())
		{
			cleaver.setExplosion();
		}

		if (damageSource.isUnblockable())
		{
			cleaver.setDamageBypassesArmor();
		}

		if (damageSource.canHarmInCreative())
		{
			cleaver.setDamageAllowedInCreativeMode();
		}

		if (damageSource.isDamageAbsolute())
		{
			cleaver.setDamageIsAbsolute();
		}

		if (damageSource.isFireDamage())
		{
			cleaver.setFireDamage();
		}

		if (damageSource.isDifficultyScaled())
		{
			cleaver.setDifficultyScaled();
		}

		if (damageSource.isMagicDamage())
		{
			cleaver.setMagicDamage();
		}

		return cleaver;
	}

	private static void onKillPlayer(EntityLivingBase target, EntityPlayer player)
	{
		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, 100, "recentlyHit");
		ObfuscationReflectionHelper.setPrivateValue(EntityLivingBase.class, target, player, "attackingPlayer");

		target.onDeath(DamageSource.causePlayerDamage(player));
	}

}
