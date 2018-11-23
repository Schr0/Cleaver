package schr0.cleaver.api.itemcleaver;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ChopDropsPassive
{

	/* TODO ======================================== OVERWORLD ===================================== /
	 * EntityBat
	 * EntityChicken
	 * EntityRabbit
	 * EntitySheep
	 */

	// EntityBat
	public static ArrayList<ItemStack> getBat(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityBat target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityChicken
	public static ArrayList<ItemStack> getChicken(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityChicken target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.CHICKEN));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.FEATHER));

				break;

			case RARE :

				drops.add(new ItemStack(Items.FEATHER, 2));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.CHICKEN));
				drops.add(new ItemStack(Items.FEATHER));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityRabbit
	public static ArrayList<ItemStack> getRabbit(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityRabbit target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.RABBIT));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.RABBIT_HIDE));

				break;

			case RARE :

				drops.add(new ItemStack(Items.RABBIT_HIDE, 2));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.RABBIT));
				drops.add(new ItemStack(Items.RABBIT_HIDE));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySheep
	public static ArrayList<ItemStack> getSheep(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySheep target, EntityLivingBase attacker)
	{
		drops.add(new ItemStack(Items.MUTTON));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

}
