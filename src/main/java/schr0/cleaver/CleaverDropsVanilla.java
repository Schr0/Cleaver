package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class CleaverDropsVanilla
{

	private static final int EPIC_AMOUNT = 10;
	private static final int RARE_AMOUNT = 20;
	private static final int UNCOMMON_AMOUNT = 30;

	// TODO /* ======================================== BOSS =====================================*/

	// EntityDragon
	public static ArrayList<ItemStack> getDropsDragon(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityDragon target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWither
	public static ArrayList<ItemStack> getDropsWither(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityWither target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// TODO /* ======================================== MONSTER =====================================*/

	// EntityBlaze
	public static ArrayList<ItemStack> getDropsBlaze(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityBlaze target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
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

		target.setOnFire(false);

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityCaveSpider
	public static ArrayList<ItemStack> getDropsCaveSpider(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityCaveSpider target, EntityLivingBase attacker)
	{
		return getDropsSpider(drops, rarity, stack, target, attacker);
	}

	// EntityCreeper
	public static ArrayList<ItemStack> getDropsCreeper(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityCreeper target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.GUNPOWDER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.GUNPOWDER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.GUNPOWDER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.GUNPOWDER, 4));

				break;
		}

		target.setCreeperState(-1);

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityElderGuardian
	public static ArrayList<ItemStack> getDropsElderGuardian(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityElderGuardian target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					int metaSalmon = ItemFishFood.FishType.SALMON.getMetadata();

					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_FISH, 1, metaSalmon));
					}
					else
					{
						drops.add(new ItemStack(Items.FISH, 1, metaSalmon));
					}
				}
				else
				{
					int metaCod = ItemFishFood.FishType.COD.getMetadata();

					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_FISH, 1, metaCod));
					}
					else
					{
						drops.add(new ItemStack(Items.FISH, 1, metaCod));
					}
				}

				break;

			case RARE :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}

				break;

			case EPIC :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
				{
					drops.add(new ItemStack(Blocks.SPONGE, 1, 1));
				}
				else
				{
					drops.add(new ItemStack(Blocks.SEA_LANTERN));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEnderman
	public static ArrayList<ItemStack> getDropsEnderman(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityEnderman target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.ENDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.ENDER_PEARL));
				}

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.CHORUS_FRUIT_POPPED));
				}
				else
				{
					drops.add(new ItemStack(Items.CHORUS_FRUIT));
				}

				break;

			case EPIC :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Blocks.CHORUS_FLOWER));
				}
				else
				{
					drops.add(new ItemStack(Blocks.END_STONE));
				}

				break;
		}

		IBlockState state = target.getHeldBlockState();

		if (state != null)
		{
			Item block = Item.getItemFromBlock(state.getBlock());
			int meta = block.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0;

			drops.add(new ItemStack(block, 1, meta));

			target.setHeldBlockState((IBlockState) null);
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEndermite
	public static ArrayList<ItemStack> getDropsEndermite(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityEndermite target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEvoker
	public static ArrayList<ItemStack> getDropsEvoker(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityEvoker target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				// none

				break;

			case RARE :

				drops.add(new ItemStack(Items.EMERALD));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.TOTEM_OF_UNDYING));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGhast
	public static ArrayList<ItemStack> getDropsGhast(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityGhast target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGiantZombie
	public static ArrayList<ItemStack> getDropsGiantZombie(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityGiantZombie target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.BAKED_POTATO, 10));
					}
					else
					{
						drops.add(new ItemStack(Items.POTATO, 10));
					}

				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH, 10));
				}

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGuardian
	public static ArrayList<ItemStack> getDropsGuardian(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityGuardian target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					int metaSalmon = ItemFishFood.FishType.SALMON.getMetadata();

					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_FISH, 1, metaSalmon));
					}
					else
					{
						drops.add(new ItemStack(Items.FISH, 1, metaSalmon));
					}
				}
				else
				{
					int metaCod = ItemFishFood.FishType.COD.getMetadata();

					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_FISH, 1, metaCod));
					}
					else
					{
						drops.add(new ItemStack(Items.FISH, 1, metaCod));
					}
				}

				break;

			case RARE :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Blocks.SEA_LANTERN));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityHusk
	public static ArrayList<ItemStack> getDropsHusk(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityHusk target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarity, stack, target, attacker);
	}

	// EntityIllusionIllager
	public static ArrayList<ItemStack> getDropsIllusionIllager(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityIllusionIllager target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityIronGolem
	public static ArrayList<ItemStack> getDropsIronGolem(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityIronGolem target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Blocks.RED_FLOWER));

				break;

			case RARE :

				drops.add(new ItemStack(Items.IRON_NUGGET));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.IRON_INGOT));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMagmaCube
	public static ArrayList<ItemStack> getDropsMagmaCube(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityMagmaCube target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.MAGMA_CREAM, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.MAGMA_CREAM, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.MAGMA_CREAM, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.MAGMA_CREAM, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityPigZombie
	public static ArrayList<ItemStack> getDropsPigZombie(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityPigZombie target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
				{
					drops.add(new ItemStack(Items.BAKED_POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityPolarBear
	public static ArrayList<ItemStack> getDropsPolarBear(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityPolarBear target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
				}

				break;

			case UNCOMMON :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_FISH, 2, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.SALMON.getMetadata()));
				}

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_FISH, 3, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 3, ItemFishFood.FishType.SALMON.getMetadata()));
				}

				break;

			case EPIC :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_FISH, 4, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 4, ItemFishFood.FishType.SALMON.getMetadata()));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityShulker
	public static ArrayList<ItemStack> getDropsShulker(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityShulker target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySilverfish
	public static ArrayList<ItemStack> getDropsSilverfish(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySilverfish target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySkeleton
	public static ArrayList<ItemStack> getDropsSkeleton(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySkeleton target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
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

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.ARROW));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySlime
	public static ArrayList<ItemStack> getDropsSlime(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySlime target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.SLIME_BALL, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.SLIME_BALL, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.SLIME_BALL, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.SLIME_BALL, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySnowman
	public static ArrayList<ItemStack> getDropsSnowman(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySnowman target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.SNOWBALL, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.SNOWBALL, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.SNOWBALL, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.SNOWBALL, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySpider
	public static ArrayList<ItemStack> getDropsSpider(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySpider target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.STRING));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.STRING, 2));

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.STRING));

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityStray
	public static ArrayList<ItemStack> getDropsStray(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityStray target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
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

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.TIPPED_ARROW, PotionType.REGISTRY.getIDForObject(PotionTypes.SLOWNESS)));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVex
	public static ArrayList<ItemStack> getDropsVex(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityVex target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVindicator
	public static ArrayList<ItemStack> getDropsVindicator(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityVindicator target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				// none

				break;

			case RARE :

				drops.add(new ItemStack(Items.EMERALD));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.EMERALD, 2));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitch
	public static ArrayList<ItemStack> getDropsWitch(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityWitch target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.GLASS_BOTTLE));
				}
				else
				{
					drops.add(new ItemStack(Items.STICK));
				}

				break;

			case RARE :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SUGAR));
				}

				break;

			case EPIC :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.GLOWSTONE_DUST));
				}
				else
				{
					drops.add(new ItemStack(Items.REDSTONE));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitherSkeleton
	public static ArrayList<ItemStack> getDropsWitherSkeleton(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityWitherSkeleton target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < UNCOMMON_AMOUNT)
				{
					drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
				}
				else
				{
					drops.add(new ItemStack(Items.COAL));
				}

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BONE));

				break;

			case RARE :

				drops.add(new ItemStack(Items.ARROW));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
				drops.add(new ItemStack(Items.BONE));
				drops.add(new ItemStack(Items.ARROW));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombie
	public static ArrayList<ItemStack> getDropsZombie(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityZombie target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.BAKED_POTATO));
					}
					else
					{
						drops.add(new ItemStack(Items.POTATO));
					}

				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (getRandom(target).nextInt(100) < EPIC_AMOUNT)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombieVillager
	public static ArrayList<ItemStack> getDropsZombieVillager(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityZombieVillager target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarity, stack, target, attacker);
	}

	// TODO /* ======================================== PASSIVE =====================================*/

	// EntityBat
	public static ArrayList<ItemStack> getDropsBat(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityBat target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityChicken
	public static ArrayList<ItemStack> getDropsChicken(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityChicken target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.FEATHER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.FEATHER, 2));

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_CHICKEN));
				}
				else
				{
					drops.add(new ItemStack(Items.CHICKEN));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.FEATHER));

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_CHICKEN));
				}
				else
				{
					drops.add(new ItemStack(Items.CHICKEN));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityCow
	public static ArrayList<ItemStack> getDropsCow(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityCow target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_BEEF));
				}
				else
				{
					drops.add(new ItemStack(Items.BEEF));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER));

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_BEEF));
				}
				else
				{
					drops.add(new ItemStack(Items.BEEF));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityDonkey
	public static ArrayList<ItemStack> getDropsDonkey(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityDonkey target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.LEATHER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityHorse
	public static ArrayList<ItemStack> getDropsHorse(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityHorse target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.LEATHER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityLlama
	public static ArrayList<ItemStack> getDropsLlama(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityLlama target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.LEATHER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMooshroom
	public static ArrayList<ItemStack> getDropsMooshroom(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityMooshroom target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_BEEF));
				}
				else
				{
					drops.add(new ItemStack(Items.BEEF));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER));

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_BEEF));
				}
				else
				{
					drops.add(new ItemStack(Items.BEEF));
				}

				break;
		}

		World world = target.getEntityWorld();
		EntityCow entitycow = new EntityCow(world);
		entitycow.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
		entitycow.setHealth(target.getHealth());
		entitycow.renderYawOffset = target.renderYawOffset;

		world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, target.posX, target.posY + (double) (target.height / 2.0F), target.posZ, 0.0D, 0.0D, 0.0D);

		if (target.hasCustomName())
		{
			entitycow.setCustomNameTag(target.getCustomNameTag());
		}

		target.setDead();

		world.spawnEntity(entitycow);

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMule
	public static ArrayList<ItemStack> getDropsMule(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityMule target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.LEATHER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.LEATHER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.LEATHER, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityOcelot
	public static ArrayList<ItemStack> getDropsOcelot(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityOcelot target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityParrot
	public static ArrayList<ItemStack> getDropsParrot(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityParrot target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.FEATHER, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.FEATHER, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.FEATHER, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.FEATHER, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityPig
	public static ArrayList<ItemStack> getDropsPig(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityPig target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_PORKCHOP, 1));
				}
				else
				{
					drops.add(new ItemStack(Items.PORKCHOP, 1));
				}

				break;

			case UNCOMMON :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_PORKCHOP, 2));
				}
				else
				{
					drops.add(new ItemStack(Items.PORKCHOP, 2));
				}

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_PORKCHOP, 3));
				}
				else
				{
					drops.add(new ItemStack(Items.PORKCHOP, 3));
				}

				break;

			case EPIC :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_PORKCHOP, 4));
				}
				else
				{
					drops.add(new ItemStack(Items.PORKCHOP, 4));
				}

				break;
		}

		if (target.getSaddled())
		{
			drops.add(new ItemStack(Items.SADDLE));

			target.setSaddled(false);
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityRabbit
	public static ArrayList<ItemStack> getDropsRabbit(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityRabbit target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.RABBIT_HIDE));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.RABBIT_HIDE, 2));

				break;

			case RARE :

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_RABBIT));
				}
				else
				{
					drops.add(new ItemStack(Items.RABBIT));
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.RABBIT_HIDE));

				if (isSmelting(stack, target))
				{
					drops.add(new ItemStack(Items.COOKED_RABBIT));
				}
				else
				{
					drops.add(new ItemStack(Items.RABBIT));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySheep
	public static ArrayList<ItemStack> getDropsSheep(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySheep target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				if (target.getSheared())
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_MUTTON, 1));
					}
					else
					{
						drops.add(new ItemStack(Items.MUTTON, 1));
					}
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, target.getFleeceColor().getMetadata()));
				}

				break;

			case UNCOMMON :

				if (target.getSheared())
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_MUTTON, 2));
					}
					else
					{
						drops.add(new ItemStack(Items.MUTTON, 2));
					}
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 2, target.getFleeceColor().getMetadata()));
				}

				break;

			case RARE :

				if (target.getSheared())
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_MUTTON, 3));
					}
					else
					{
						drops.add(new ItemStack(Items.MUTTON, 3));
					}
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 3, target.getFleeceColor().getMetadata()));
				}

				break;

			case EPIC :

				if (target.getSheared())
				{
					if (isSmelting(stack, target))
					{
						drops.add(new ItemStack(Items.COOKED_MUTTON, 4));
					}
					else
					{
						drops.add(new ItemStack(Items.MUTTON, 4));
					}
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 4, target.getFleeceColor().getMetadata()));
				}

				break;
		}

		if (!target.getSheared())
		{
			target.setSheared(true);
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySkeletonHorse
	public static ArrayList<ItemStack> getDropsSkeletonHorse(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySkeletonHorse target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.DYE, 2, EnumDyeColor.WHITE.getDyeDamage()));

				break;

			case RARE :

				drops.add(new ItemStack(Items.BONE));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
				drops.add(new ItemStack(Items.BONE));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySquid
	public static ArrayList<ItemStack> getDropsSquid(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntitySquid target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.DYE, 2, EnumDyeColor.BLACK.getDyeDamage()));

				break;

			case RARE :

				drops.add(new ItemStack(Items.DYE, 3, EnumDyeColor.BLACK.getDyeDamage()));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.DYE, 4, EnumDyeColor.BLACK.getDyeDamage()));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVillager
	public static ArrayList<ItemStack> getDropsVillager(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityVillager target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				// none

				break;

			case RARE :

				if (attacker instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) attacker;
					MerchantRecipeList merchantrecipelist = ((EntityVillager) target).getRecipes(player);

					if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
					{
						int merchantSize = getRandom(target).nextInt(merchantrecipelist.size());
						MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(merchantSize);
						ItemStack stackMerchant = merchantrecipe.getItemToSell().copy();

						if (stackMerchant != null)
						{
							drops.add(stackMerchant);
						}
					}
				}

				break;

			case EPIC :

				drops.add(new ItemStack(Items.EMERALD, 1));

				if (attacker instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) attacker;
					MerchantRecipeList merchantrecipelist = ((EntityVillager) target).getRecipes(player);

					if (merchantrecipelist != null && !merchantrecipelist.isEmpty())
					{
						int merchantSize = getRandom(target).nextInt(merchantrecipelist.size());
						MerchantRecipe merchantrecipe = (MerchantRecipe) merchantrecipelist.get(merchantSize);
						ItemStack stackMerchant = merchantrecipe.getItemToSell().copy();

						if (stackMerchant != null)
						{
							drops.add(stackMerchant);
						}
					}
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWolf
	public static ArrayList<ItemStack> getDropsWolf(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityWolf target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombieHorse
	public static ArrayList<ItemStack> getDropsZombieHorse(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityZombieHorse target, EntityLivingBase attacker)
	{
		switch (getEnumRarity(rarity))
		{
			case COMMON :

				drops.add(new ItemStack(Items.ROTTEN_FLESH, 1));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.ROTTEN_FLESH, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.ROTTEN_FLESH, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.ROTTEN_FLESH, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static ArrayList<ItemStack> getDrops(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (target instanceof EntityAgeable)
		{
			EntityAgeable entityAgeable = (EntityAgeable) target;

			if (entityAgeable.isChild())
			{
				drops.clear();
			}
		}

		return drops;
	}

	private static boolean isSmelting(ItemStack stack, EntityLivingBase target)
	{
		return (target.isBurning() || (0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)));
	}

	private static EnumRarity getEnumRarity(int rarity)
	{
		if (rarity < EPIC_AMOUNT)
		{
			return EnumRarity.EPIC;
		}

		if (rarity < RARE_AMOUNT)
		{
			return EnumRarity.RARE;
		}

		if (rarity < UNCOMMON_AMOUNT)
		{
			return EnumRarity.UNCOMMON;
		}

		return EnumRarity.COMMON;
	}

	private static Random getRandom(EntityLivingBase target)
	{
		return target.getEntityWorld().rand;
	}

}