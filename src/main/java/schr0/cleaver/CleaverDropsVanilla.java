package schr0.cleaver;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
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

public class CleaverDropsVanilla
{

	// TODO /* ======================================== BOSS =====================================*/

	// EntityDragon
	public static ArrayList<ItemStack> getDropsDragon(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityDragon target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntityWither
	public static ArrayList<ItemStack> getDropsWither(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityWither target, EntityLivingBase attacker)
	{
		return drops;
	}

	// TODO /* ======================================== MONSTER =====================================*/

	// EntityBlaze
	public static ArrayList<ItemStack> getDropsBlaze(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityBlaze target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityCaveSpider
	public static ArrayList<ItemStack> getDropsCaveSpider(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityCaveSpider target, EntityLivingBase attacker)
	{
		return getDropsSpider(drops, rarityValue, stack, target, attacker);
	}

	// EntityCreeper
	public static ArrayList<ItemStack> getDropsCreeper(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityCreeper target, EntityLivingBase attacker)
	{
		int stackAmount;

		switch (getRarity(rarityValue))
		{
			case UNCOMMON :

				stackAmount = 2;

				break;

			case RARE :

				stackAmount = 3;

				break;

			case EPIC :

				stackAmount = 4;

				break;

			default :

				stackAmount = 1;
		}

		drops.add(new ItemStack(Items.GUNPOWDER, stackAmount));

		return drops;
	}

	// EntityElderGuardian
	public static ArrayList<ItemStack> getDropsElderGuardian(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityElderGuardian target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (random.nextInt(100) < 40)
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

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}

				break;

			case EPIC :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Blocks.SPONGE, 1, 1));
				}
				else
				{
					drops.add(new ItemStack(Blocks.SEA_LANTERN));
				}

				break;
		}

		return drops;
	}

	// EntityEnderman
	public static ArrayList<ItemStack> getDropsEnderman(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityEnderman target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;
		IBlockState state = target.getHeldBlockState();

		if (state != null)
		{
			Item block = Item.getItemFromBlock(state.getBlock());
			int meta = block.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0;

			drops.add(new ItemStack(block, 1, meta));

			target.setHeldBlockState((IBlockState) null);
		}

		switch (getRarity(rarityValue))
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

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Blocks.CHORUS_FLOWER));
				}
				else
				{
					drops.add(new ItemStack(Blocks.END_STONE));
				}

				break;
		}

		return drops;
	}

	// EntityEndermite
	public static ArrayList<ItemStack> getDropsEndermite(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityEndermite target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntityEvoker
	public static ArrayList<ItemStack> getDropsEvoker(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityEvoker target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityGhast
	public static ArrayList<ItemStack> getDropsGhast(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityGhast target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityGiantZombie
	public static ArrayList<ItemStack> getDropsGiantZombie(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityGiantZombie target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntityGuardian
	public static ArrayList<ItemStack> getDropsGuardian(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityGuardian target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (random.nextInt(100) < 40)
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

				if (random.nextInt(100) < 40)
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

		return drops;
	}

	// EntityHusk
	public static ArrayList<ItemStack> getDropsHusk(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityHusk target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarityValue, stack, target, attacker);
	}

	// EntityIllusionIllager
	public static ArrayList<ItemStack> getDropsIllusionIllager(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityIllusionIllager target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntityIronGolem
	public static ArrayList<ItemStack> getDropsIronGolem(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityIronGolem target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityMagmaCube
	public static ArrayList<ItemStack> getDropsMagmaCube(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityMagmaCube target, EntityLivingBase attacker)
	{
		int stackAmount;

		switch (getRarity(rarityValue))
		{
			case UNCOMMON :

				stackAmount = 2;

				break;

			case RARE :

				stackAmount = 3;

				break;

			case EPIC :

				stackAmount = 4;

				break;

			default :

				stackAmount = 1;
		}

		drops.add(new ItemStack(Items.MAGMA_CREAM, stackAmount));

		return drops;
	}

	// EntityPigZombie
	public static ArrayList<ItemStack> getDropsPigZombie(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityPigZombie target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				drops.add(new ItemStack(Items.ROTTEN_FLESH));

				break;

			case UNCOMMON :

				switch (random.nextInt(2))
				{
					case 0 :

						drops.add(new ItemStack(Items.CARROT));

						break;

					case 1 :

						drops.add(new ItemStack(Items.BAKED_POTATO));

						break;

					case 2 :

						drops.add(new ItemStack(Items.POISONOUS_POTATO));

						break;
				}

				break;

			case RARE :

				drops.add(new ItemStack(Items.GOLD_NUGGET));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.GOLD_INGOT));

				break;
		}

		return drops;
	}

	// EntityPolarBear
	public static ArrayList<ItemStack> getDropsPolarBear(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityPolarBear target, EntityLivingBase attacker)
	{
		int stackAmount;

		switch (getRarity(rarityValue))
		{
			case UNCOMMON :

				stackAmount = 2;

				break;

			case RARE :

				stackAmount = 3;

				break;

			case EPIC :

				stackAmount = 4;

				break;

			default :

				stackAmount = 1;
		}

		int metaSalmon = ItemFishFood.FishType.SALMON.getMetadata();

		if (isSmelting(stack, target))
		{
			drops.add(new ItemStack(Items.COOKED_FISH, stackAmount, metaSalmon));
		}
		else
		{
			drops.add(new ItemStack(Items.FISH, stackAmount, metaSalmon));
		}

		return drops;
	}

	// EntityShulker
	public static ArrayList<ItemStack> getDropsShulker(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityShulker target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntitySilverfish
	public static ArrayList<ItemStack> getDropsSilverfish(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntitySilverfish target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntitySkeleton
	public static ArrayList<ItemStack> getDropsSkeleton(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntitySkeleton target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntitySlime
	public static ArrayList<ItemStack> getDropsSlime(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntitySlime target, EntityLivingBase attacker)
	{
		int stackAmount;

		switch (getRarity(rarityValue))
		{
			case UNCOMMON :

				stackAmount = 2;

				break;

			case RARE :

				stackAmount = 3;

				break;

			case EPIC :

				stackAmount = 4;

				break;

			default :

				stackAmount = 1;
		}

		drops.add(new ItemStack(Items.SLIME_BALL, stackAmount));

		return drops;
	}

	// EntitySnowman
	public static ArrayList<ItemStack> getDropsSnowman(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntitySnowman target, EntityLivingBase attacker)
	{
		int stackAmount;

		switch (getRarity(rarityValue))
		{
			case UNCOMMON :

				stackAmount = 2;

				break;

			case RARE :

				stackAmount = 3;

				break;

			case EPIC :

				stackAmount = 4;

				break;

			default :

				stackAmount = 1;
		}

		drops.add(new ItemStack(Items.SNOWBALL, stackAmount));

		return drops;
	}

	// EntitySpider
	public static ArrayList<ItemStack> getDropsSpider(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntitySpider target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityStray
	public static ArrayList<ItemStack> getDropsStray(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityStray target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityVex
	public static ArrayList<ItemStack> getDropsVex(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityVex target, EntityLivingBase attacker)
	{
		return drops;
	}

	// EntityVindicator
	public static ArrayList<ItemStack> getDropsVindicator(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityVindicator target, EntityLivingBase attacker)
	{
		switch (getRarity(rarityValue))
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

		return drops;
	}

	// EntityWitch
	public static ArrayList<ItemStack> getDropsWitch(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityWitch target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				// none

				break;

			case UNCOMMON :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.GLASS_BOTTLE));
				}
				else
				{
					drops.add(new ItemStack(Items.STICK));
				}

				break;

			case RARE :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.SPIDER_EYE));
				}
				else
				{
					drops.add(new ItemStack(Items.SUGAR));
				}

				break;

			case EPIC :

				if (random.nextInt(100) < 40)
				{
					drops.add(new ItemStack(Items.GLOWSTONE_DUST));
				}
				else
				{
					drops.add(new ItemStack(Items.REDSTONE));
				}

				break;
		}

		return drops;
	}

	// EntityWitherSkeleton
	public static ArrayList<ItemStack> getDropsWitherSkeleton(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityWitherSkeleton target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				if (random.nextInt(100) < 40)
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

		return drops;
	}

	// EntityZombie
	public static ArrayList<ItemStack> getDropsZombie(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityZombie target, EntityLivingBase attacker)
	{
		Random random = target.getEntityWorld().rand;

		switch (getRarity(rarityValue))
		{
			case COMMON :

				drops.add(new ItemStack(Items.ROTTEN_FLESH));

				break;

			case UNCOMMON :

				switch (random.nextInt(2))
				{
					case 0 :

						drops.add(new ItemStack(Items.CARROT));

						break;

					case 1 :

						if (isSmelting(stack, target))
						{
							drops.add(new ItemStack(Items.BAKED_POTATO));
						}
						else
						{
							drops.add(new ItemStack(Items.POTATO));
						}

						break;

					case 2 :

						drops.add(new ItemStack(Items.POISONOUS_POTATO));

						break;
				}

				break;

			case RARE :

				drops.add(new ItemStack(Items.IRON_NUGGET));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.IRON_INGOT));

				break;
		}

		return drops;
	}

	// EntityZombieVillager
	public static ArrayList<ItemStack> getDropsZombieVillager(ArrayList<ItemStack> drops, int rarityValue, ItemStack stack, EntityZombieVillager target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarityValue, stack, target, attacker);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static boolean isSmelting(ItemStack stack, EntityLivingBase target)
	{
		return (target.isBurning() || (0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)));
	}

	private static EnumRarity getRarity(int rarityValue)
	{
		if (rarityValue < 10)// 10
		{
			return EnumRarity.EPIC;
		}

		if (rarityValue < 30)// 20
		{
			return EnumRarity.RARE;
		}

		if (rarityValue < 60)// 30
		{
			return EnumRarity.UNCOMMON;
		}

		return EnumRarity.COMMON;
	}

}
