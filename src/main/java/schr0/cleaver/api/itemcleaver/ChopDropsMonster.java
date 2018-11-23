package schr0.cleaver.api.itemcleaver;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class ChopDropsMonster
{

	/* TODO ======================================== OVERWORLD ===================================== /
	 * EntityCreeper
	 * EntityEnderman
	 * EntityEndermite
	 * EntityGiantZombie
	 * EntityPolarBear
	 * EntitySilverfish
	 * EntityWitch
	 */

	// EntityCreeper
	public static ArrayList<ItemStack> getCreeper(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCreeper target, EntityLivingBase attacker)
	{
		target.setCreeperState(-1);

		drops.add(new ItemStack(Items.GUNPOWDER));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEnderman
	public static ArrayList<ItemStack> getEnderman(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEnderman target, EntityLivingBase attacker)
	{
		IBlockState state = target.getHeldBlockState();

		if (state != null)
		{
			Item block = Item.getItemFromBlock(state.getBlock());
			int meta = block.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0;

			drops.add(new ItemStack(block, 1, meta));

			target.setHeldBlockState((IBlockState) null);

			return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
		}

		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.ENDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.ENDER_PEARL));
				}

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.CHORUS_FRUIT));

				break;

			case RARE :

				drops.add(new ItemStack(Blocks.CHORUS_PLANT));

				break;

			case EPIC :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Blocks.END_STONE));
				}
				else
				{
					drops.add(new ItemStack(Blocks.CHORUS_FLOWER));
				}

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEndermite
	public static ArrayList<ItemStack> getEndermite(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEndermite target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGiantZombie
	public static ArrayList<ItemStack> getGiantZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGiantZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.RARE)
				{
					drops.add(new ItemStack(Items.POTATO, 10));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH, 10));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.RARE)
				{
					drops.add(new ItemStack(Items.POISONOUS_POTATO, 10));
				}
				else
				{
					drops.add(new ItemStack(Items.CARROT, 10));
				}

				break;

			case RARE :

				drops.add(new ItemStack(Items.IRON_NUGGET, 10));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.IRON_INGOT, 10));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityPolarBear
	public static ArrayList<ItemStack> getPolarBear(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPolarBear target, EntityLivingBase attacker)
	{
		drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySilverfish
	public static ArrayList<ItemStack> getSilverfish(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySilverfish target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitch
	public static ArrayList<ItemStack> getWitch(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWitch target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.GLASS_BOTTLE));
				}
				else
				{
					drops.add(new ItemStack(Items.STICK));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					if (ItemCleaverHelper.isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
					}
					else
					{
						drops.add(new ItemStack(Items.SPIDER_EYE));
					}
				}
				else
				{
					drops.add(new ItemStack(Items.SUGAR));
				}

				break;

			case RARE :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.REDSTONE));
				}
				else
				{
					drops.add(new ItemStack(Items.GUNPOWDER));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.GLOWSTONE_DUST));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== NETHER ===================================== /
	 * EntityBlaze
	 * EntityGhast
	 */

	// EntityBlaze
	public static ArrayList<ItemStack> getBlaze(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityBlaze target, EntityLivingBase attacker)
	{
		target.setOnFire(false);

		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.BLAZE_POWDER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BLAZE_POWDER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.BLAZE_ROD));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.BLAZE_POWDER));
				drops.add(new ItemStack(Items.BLAZE_ROD));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGhast
	public static ArrayList<ItemStack> getGhast(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGhast target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.GUNPOWDER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.GUNPOWDER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.GHAST_TEAR));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.GUNPOWDER));
				drops.add(new ItemStack(Items.GHAST_TEAR));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== END ===================================== /
	 * EntityShulker
	 */

	// EntityShulker
	public static ArrayList<ItemStack> getShulker(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityShulker target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

}
