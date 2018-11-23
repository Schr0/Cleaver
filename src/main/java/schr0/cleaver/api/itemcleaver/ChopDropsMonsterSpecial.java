package schr0.cleaver.api.itemcleaver;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;

public class ChopDropsMonsterSpecial
{

	/* TODO ======================================== BOSS ===================================== /
	 * EntityDragon
	 * EntityWither
	 */

	// EntityDragon
	public static ArrayList<ItemStack> getDragon(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityDragon target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWither
	public static ArrayList<ItemStack> getWither(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWither target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== ZOMBIE ===================================== /
	 * EntityZombie
	 * EntityZombieVillager
	 * EntityHusk
	 * EntityPigZombie
	 */

	// EntityZombie
	public static ArrayList<ItemStack> getZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.POISONOUS_POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.CARROT));
				}

				break;

			case RARE :

				drops.add(new ItemStack(Items.IRON_NUGGET));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.IRON_INGOT));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombieVillager
	public static ArrayList<ItemStack> getZombieVillager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombieVillager target, EntityLivingBase attacker)
	{
		return getZombie(drops, rarity, stack, target, attacker);
	}

	// EntityHusk
	public static ArrayList<ItemStack> getHusk(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityHusk target, EntityLivingBase attacker)
	{
		return getZombie(drops, rarity, stack, target, attacker);
	}

	// EntityPigZombie
	public static ArrayList<ItemStack> getPigZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPigZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.BAKED_POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.POISONOUS_POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.CARROT));
				}

				break;

			case RARE :

				drops.add(new ItemStack(Items.GOLD_NUGGET));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.GOLD_INGOT));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== SKELETON ===================================== /
	 * EntitySkeleton
	 * EntityStray
	 * EntityWitherSkeleton
	 */

	// EntitySkeleton
	public static ArrayList<ItemStack> getSkeleton(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySkeleton target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BONE));

				break;

			case RARE :

				drops.add(new ItemStack(Items.ARROW));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.ARROW));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityStray
	public static ArrayList<ItemStack> getStray(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityStray target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BONE));

				break;

			case RARE :

				drops.add(new ItemStack(Items.TIPPED_ARROW, PotionType.REGISTRY.getIDForObject(PotionTypes.SLOWNESS)));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.TIPPED_ARROW, PotionType.REGISTRY.getIDForObject(PotionTypes.SLOWNESS)));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitherSkeleton
	public static ArrayList<ItemStack> getWitherSkeleton(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWitherSkeleton target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BONE));

				break;

			case RARE :

				drops.add(new ItemStack(Items.COAL));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.COAL));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== SPIDER ===================================== /
	 * EntitySpider
	 * EntityCaveSpider
	 */

	// EntitySpider
	public static ArrayList<ItemStack> getSpider(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySpider target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.STRING));

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}

				break;

			case RARE :

				drops.add(new ItemStack(Blocks.WEB));

				break;

			case EPIC :

				if (ItemCleaverHelper.isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}

				drops.add(new ItemStack(Blocks.WEB));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityCaveSpider
	public static ArrayList<ItemStack> getCaveSpider(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCaveSpider target, EntityLivingBase attacker)
	{
		return getSpider(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== SLIME ===================================== /
	 * EntitySlime
	 * EntityMagmaCube
	 */

	// EntitySlime
	public static ArrayList<ItemStack> getSlime(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySlime target, EntityLivingBase attacker)
	{
		if (target.isSmallSlime())
		{
			return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
		}

		drops.add(new ItemStack(Items.SLIME_BALL));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMagmaCube
	public static ArrayList<ItemStack> getMagmaCube(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMagmaCube target, EntityLivingBase attacker)
	{
		if (target.isSmallSlime())
		{
			return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
		}

		drops.add(new ItemStack(Items.MAGMA_CREAM));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== GOLEM ===================================== /
	 * EntitySnowman
	 * EntityIronGolem
	 */

	// EntitySnowman
	public static ArrayList<ItemStack> getSnowman(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySnowman target, EntityLivingBase attacker)
	{
		drops.add(new ItemStack(Items.SNOWBALL));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityIronGolem
	public static ArrayList<ItemStack> getIronGolem(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityIronGolem target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Blocks.RED_FLOWER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.IRON_NUGGET));

				break;

			case RARE :

				drops.add(new ItemStack(Items.IRON_INGOT));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.IRON_INGOT, 4));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== ILLAGER ===================================== /
	 * EntityEvoker
	 * EntityVindicator
	 * EntityVex
	 * EntityIllusionIllager
	 */

	// EntityEvoker
	public static ArrayList<ItemStack> getEvoker(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEvoker target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.EMERALD));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.EMERALD, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.EMERALD, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.TOTEM_OF_UNDYING));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVindicator
	public static ArrayList<ItemStack> getVindicator(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVindicator target, EntityLivingBase attacker)
	{
		drops.add(new ItemStack(Items.EMERALD));

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVex
	public static ArrayList<ItemStack> getVex(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVex target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityIllusionIllager
	public static ArrayList<ItemStack> getIllusionIllager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityIllusionIllager target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== GUARDIAN ===================================== /
	 * EntityGuardian
	 * EntityElderGuardian
	 */

	// EntityGuardian
	public static ArrayList<ItemStack> getGuardian(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGuardian target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));
				}

				break;

			case RARE :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Blocks.SEA_LANTERN));

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

	// EntityElderGuardian
	public static ArrayList<ItemStack> getElderGuardian(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityElderGuardian target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS, 2));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
				}

				break;

			case UNCOMMON :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.COD.getMetadata()));
				}

				break;

			case RARE :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}

				break;

			case EPIC :

				if (ItemCleaverHelper.getChopDropsRarity() == EnumRarity.RARE)
				{
					drops.add(new ItemStack(Blocks.SPONGE, 1, 1));
				}
				else
				{
					drops.add(new ItemStack(Blocks.SEA_LANTERN, 2));
				}

				break;
		}

		return ItemCleaverHelper.initializeChopDrops(drops, rarity, stack, target, attacker);
	}

}
