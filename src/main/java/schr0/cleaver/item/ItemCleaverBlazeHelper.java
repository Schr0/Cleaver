package schr0.cleaver.item;

import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemCleaverBlazeHelper
{

	private static final int POTION_DURATION = 100;
	private static final int POTION_DURATION_MAX = (60 * 20);
	private static final int POTION_DURATION_MIN = (20 * 20);
	private static final int POTION_DURATION_LIMIT = (11 * 20);

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

	public static void onUpdateBlazeShield(int heatAmount, World world, ItemStack stack, EntityLivingBase owner)
	{
		Random random = getRandom(owner);

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

}
