package schr0.cleaver;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		float attackAmmount = event.getAmount();
		float targetHealth = (target.getHealth() + target.getAbsorptionAmount());

		if (this.isInvalidLivingAttackEvent(target, damageSource))
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

				target.hurtResistantTime = 0;

				if (attackAmmount < targetHealth)
				{
					event.setCanceled(true);

					target.attackEntityFrom(getCleaverDamageSource(target, damageSource), attackAmmount);
				}
				else
				{
					damageSource.setDamageBypassesArmor().setDamageIsAbsolute();
				}

				itemCleaver.onAttackTarget(stackMainHand, target, attacker, itemCleaver.isCleaveTarget(stackMainHand, target, attacker));
			}
		}
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static boolean isInvalidLivingAttackEvent(EntityLivingBase target, DamageSource damageSource)
	{
		if (target.getEntityWorld().isRemote)
		{
			return true;
		}

		if (target == null || (target != null && (target.isDead || target.getHealth() <= 0)))
		{
			return true;
		}

		return (damageSource instanceof EntityDamageSourceIndirect);
	}

	public static DamageSource getCleaverDamageSource(EntityLivingBase target, DamageSource damageSource)
	{
		DamageSource cleaver = CleaverDamageSource.CLEAVER;

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

}
