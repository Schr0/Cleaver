package schr0.cleaver.api;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;

public class ItemCleaverHelper
{
	private static final int RARITY_PERCENT = 100;
	private static final int RARITY_EPIC = 10;
	private static final int RARITY_RARE = 30;
	private static final int RARITY_UNCOMMON = 60;

	/**
	 * 「装備解除」のドロップアイテム.
	 *
	 * @param sharpnessAmount
	 *            切れ味値.
	 * @param target
	 *            「装備解除」されるEntityLivingBase.
	 * @param attacker
	 *            「装備解除」するEntityLivingBase.
	 *
	 * @return 「装備解除」のドロップアイテム.
	 */
	public static ArrayList<ItemStack> getCleaveEquipments(int sharpnessAmount, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> equipments = new ArrayList<ItemStack>();

		for (EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values())
		{
			ItemStack stackEquipment = target.getItemStackFromSlot(equipmentSlot);

			if (!stackEquipment.isEmpty())
			{
				if (stackEquipment.isItemStackDamageable() && !stackEquipment.getItem().isDamaged(stackEquipment))
				{
					stackEquipment.setItemDamage(getUsedItemDamage(stackEquipment, sharpnessAmount));
				}

				equipments.add(stackEquipment);

				target.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);

				return equipments;
			}
		}

		return equipments;
	}

	/**
	 * 「剥ぎ取り」のドロップアイテム.
	 *
	 * @param sharpnessAmount
	 *            切れ味値.
	 * @param stack
	 *            「ItemCleaver」のItemStack.
	 * @param target
	 *            「剥ぎ取り」されるEntityLivingBase.
	 * @param attacker
	 *            「剥ぎ取り」するEntityLivingBase.
	 *
	 * @return 「剥ぎ取り」のドロップアイテム.
	 */
	public static ArrayList<ItemStack> getCleaveDrops(int sharpnessAmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return new ArrayList<ItemStack>();
		}

		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		EnumRarity rarity = getDropRarity(sharpnessAmount);
		ResourceLocation targetKey = EntityList.getKey(target);

		// TODO /* ======================================== BOSS =====================================*/

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsDragon(drops, rarity, stack, (EntityDragon) target, attacker);
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsWither(drops, rarity, stack, (EntityWither) target, attacker);
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsBlaze(drops, rarity, stack, (EntityBlaze) target, attacker);
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsCaveSpider(drops, rarity, stack, (EntityCaveSpider) target, attacker);
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsCreeper(drops, rarity, stack, (EntityCreeper) target, attacker);
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsElderGuardian(drops, rarity, stack, (EntityElderGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsEnderman(drops, rarity, stack, (EntityEnderman) target, attacker);
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsEndermite(drops, rarity, stack, (EntityEndermite) target, attacker);
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsEvoker(drops, rarity, stack, (EntityEvoker) target, attacker);
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsGhast(drops, rarity, stack, (EntityGhast) target, attacker);
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsGiantZombie(drops, rarity, stack, (EntityGiantZombie) target, attacker);
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsGuardian(drops, rarity, stack, (EntityGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsHusk(drops, rarity, stack, (EntityHusk) target, attacker);
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsIllusionIllager(drops, rarity, stack, (EntityIllusionIllager) target, attacker);
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsIronGolem(drops, rarity, stack, (EntityIronGolem) target, attacker);
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsMagmaCube(drops, rarity, stack, (EntityMagmaCube) target, attacker);
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsPigZombie(drops, rarity, stack, (EntityPigZombie) target, attacker);
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsPolarBear(drops, rarity, stack, (EntityPolarBear) target, attacker);
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsShulker(drops, rarity, stack, (EntityShulker) target, attacker);
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSkeleton(drops, rarity, stack, (EntitySkeleton) target, attacker);
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSlime(drops, rarity, stack, (EntitySlime) target, attacker);
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSnowman(drops, rarity, stack, (EntitySnowman) target, attacker);
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSpider(drops, rarity, stack, (EntitySpider) target, attacker);
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsStray(drops, rarity, stack, (EntityStray) target, attacker);
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsVex(drops, rarity, stack, (EntityVex) target, attacker);
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsVindicator(drops, rarity, stack, (EntityVindicator) target, attacker);
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsWitch(drops, rarity, stack, (EntityWitch) target, attacker);
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsWitherSkeleton(drops, rarity, stack, (EntityWitherSkeleton) target, attacker);
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsZombie(drops, rarity, stack, (EntityZombie) target, attacker);
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsZombieVillager(drops, rarity, stack, (EntityZombieVillager) target, attacker);
		}

		// TODO /* ======================================== PASSIVE =====================================*/

		if (EntityList.getKey(EntityBat.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsBat(drops, rarity, stack, (EntityBat) target, attacker);
		}

		if (EntityList.getKey(EntityChicken.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsChicken(drops, rarity, stack, (EntityChicken) target, attacker);
		}

		if (EntityList.getKey(EntityCow.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsCow(drops, rarity, stack, (EntityCow) target, attacker);
		}

		if (EntityList.getKey(EntityDonkey.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsDonkey(drops, rarity, stack, (EntityDonkey) target, attacker);
		}

		if (EntityList.getKey(EntityHorse.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsHorse(drops, rarity, stack, (EntityHorse) target, attacker);
		}

		if (EntityList.getKey(EntityLlama.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsLlama(drops, rarity, stack, (EntityLlama) target, attacker);
		}

		if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsMooshroom(drops, rarity, stack, (EntityMooshroom) target, attacker);
		}

		if (EntityList.getKey(EntityMule.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsMule(drops, rarity, stack, (EntityMule) target, attacker);
		}

		if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsOcelot(drops, rarity, stack, (EntityOcelot) target, attacker);
		}

		if (EntityList.getKey(EntityParrot.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsParrot(drops, rarity, stack, (EntityParrot) target, attacker);
		}

		if (EntityList.getKey(EntityPig.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsPig(drops, rarity, stack, (EntityPig) target, attacker);
		}

		if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsRabbit(drops, rarity, stack, (EntityRabbit) target, attacker);
		}

		if (EntityList.getKey(EntitySheep.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSheep(drops, rarity, stack, (EntitySheep) target, attacker);
		}

		if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSkeletonHorse(drops, rarity, stack, (EntitySkeletonHorse) target, attacker);
		}

		if (EntityList.getKey(EntitySquid.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsSquid(drops, rarity, stack, (EntitySquid) target, attacker);
		}

		if (EntityList.getKey(EntityVillager.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsVillager(drops, rarity, stack, (EntityVillager) target, attacker);
		}

		if (EntityList.getKey(EntityWolf.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsWolf(drops, rarity, stack, (EntityWolf) target, attacker);
		}

		if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
		{
			return ItemCleaverHelper.getDropsZombieHorse(drops, rarity, stack, (EntityZombieHorse) target, attacker);
		}

		return drops;
	}

	// TODO /* ======================================== BOSS =====================================*/

	// EntityDragon
	public static ArrayList<ItemStack> getDropsDragon(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityDragon target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWither
	public static ArrayList<ItemStack> getDropsWither(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWither target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// TODO /* ======================================== MONSTER =====================================*/

	// EntityBlaze
	public static ArrayList<ItemStack> getDropsBlaze(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityBlaze target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityCaveSpider
	public static ArrayList<ItemStack> getDropsCaveSpider(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCaveSpider target, EntityLivingBase attacker)
	{
		return getDropsSpider(drops, rarity, stack, target, attacker);
	}

	// EntityCreeper
	public static ArrayList<ItemStack> getDropsCreeper(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCreeper target, EntityLivingBase attacker)
	{
		target.setCreeperState(-1);

		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.GUNPOWDER));

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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityElderGuardian
	public static ArrayList<ItemStack> getDropsElderGuardian(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityElderGuardian target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS, 2));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD, 2));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.COD.getMetadata()));
				}

				break;

			case RARE :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.PUFFERFISH.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
				}

				break;

			case EPIC :

				if (getDropRarity() == EnumRarity.RARE)
				{
					drops.add(new ItemStack(Blocks.SPONGE, 1, 1));
				}
				else
				{
					drops.add(new ItemStack(Blocks.SEA_LANTERN, 2));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEnderman
	public static ArrayList<ItemStack> getDropsEnderman(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEnderman target, EntityLivingBase attacker)
	{
		IBlockState state = target.getHeldBlockState();

		if (state != null)
		{
			Item block = Item.getItemFromBlock(state.getBlock());
			int meta = block.getHasSubtypes() ? state.getBlock().getMetaFromState(state) : 0;

			drops.add(new ItemStack(block, 1, meta));

			target.setHeldBlockState((IBlockState) null);

			return getDrops(drops, rarity, stack, target, attacker);
		}

		switch (rarity)
		{
			case COMMON :

				if (isSmelting(stack, target))
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

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Blocks.END_STONE));
				}
				else
				{
					drops.add(new ItemStack(Blocks.CHORUS_FLOWER));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEndermite
	public static ArrayList<ItemStack> getDropsEndermite(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEndermite target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityEvoker
	public static ArrayList<ItemStack> getDropsEvoker(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityEvoker target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityGhast
	public static ArrayList<ItemStack> getDropsGhast(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGhast target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.GUNPOWDER, 2));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.GUNPOWDER, 3));

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
	public static ArrayList<ItemStack> getDropsGiantZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGiantZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.RARE)
				{
					drops.add(new ItemStack(Items.POTATO, 10));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH, 10));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.RARE)
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
	public static ArrayList<ItemStack> getDropsGuardian(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityGuardian target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.PRISMARINE_CRYSTALS));
				}
				else
				{
					drops.add(new ItemStack(Items.PRISMARINE_SHARD));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));
				}
				else
				{
					drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()));
				}

				break;

			case RARE :

				if (getDropRarity() == EnumRarity.UNCOMMON)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityHusk
	public static ArrayList<ItemStack> getDropsHusk(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityHusk target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarity, stack, target, attacker);
	}

	// EntityIllusionIllager
	public static ArrayList<ItemStack> getDropsIllusionIllager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityIllusionIllager target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityIronGolem
	public static ArrayList<ItemStack> getDropsIronGolem(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityIronGolem target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMagmaCube
	public static ArrayList<ItemStack> getDropsMagmaCube(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMagmaCube target, EntityLivingBase attacker)
	{
		if (target.isSmallSlime())
		{
			return getDrops(drops, rarity, stack, target, attacker);
		}

		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.MAGMA_CREAM));

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
	public static ArrayList<ItemStack> getDropsPigZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPigZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.BAKED_POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
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
	public static ArrayList<ItemStack> getDropsPolarBear(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPolarBear target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.FISH, 2, ItemFishFood.FishType.SALMON.getMetadata()));

				break;

			case RARE :

				drops.add(new ItemStack(Items.FISH, 3, ItemFishFood.FishType.SALMON.getMetadata()));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.FISH, 4, ItemFishFood.FishType.SALMON.getMetadata()));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityShulker
	public static ArrayList<ItemStack> getDropsShulker(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityShulker target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySilverfish
	public static ArrayList<ItemStack> getDropsSilverfish(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySilverfish target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySkeleton
	public static ArrayList<ItemStack> getDropsSkeleton(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySkeleton target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySlime
	public static ArrayList<ItemStack> getDropsSlime(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySlime target, EntityLivingBase attacker)
	{
		if (target.isSmallSlime())
		{
			return getDrops(drops, rarity, stack, target, attacker);
		}

		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.SLIME_BALL));

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
	public static ArrayList<ItemStack> getDropsSnowman(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySnowman target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.SNOWBALL));

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
	public static ArrayList<ItemStack> getDropsSpider(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySpider target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.STRING));

				break;

			case UNCOMMON :

				if (isSmelting(stack, target))
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

				if (isSmelting(stack, target))
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityStray
	public static ArrayList<ItemStack> getDropsStray(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityStray target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVex
	public static ArrayList<ItemStack> getDropsVex(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVex target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityVindicator
	public static ArrayList<ItemStack> getDropsVindicator(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVindicator target, EntityLivingBase attacker)
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

				drops.add(new ItemStack(Items.EMERALD, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitch
	public static ArrayList<ItemStack> getDropsWitch(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWitch target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.GLASS_BOTTLE));
				}
				else
				{
					drops.add(new ItemStack(Items.STICK));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					if (isSmelting(stack, target))
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

				if (getDropRarity() == EnumRarity.UNCOMMON)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWitherSkeleton
	public static ArrayList<ItemStack> getDropsWitherSkeleton(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWitherSkeleton target, EntityLivingBase attacker)
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

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombie
	public static ArrayList<ItemStack> getDropsZombie(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombie target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
				{
					drops.add(new ItemStack(Items.POTATO));
				}
				else
				{
					drops.add(new ItemStack(Items.ROTTEN_FLESH));
				}

				break;

			case UNCOMMON :

				if (getDropRarity() == EnumRarity.UNCOMMON)
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
	public static ArrayList<ItemStack> getDropsZombieVillager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombieVillager target, EntityLivingBase attacker)
	{
		return getDropsZombie(drops, rarity, stack, target, attacker);
	}

	// TODO /* ======================================== PASSIVE =====================================*/

	// EntityBat
	public static ArrayList<ItemStack> getDropsBat(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityBat target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityChicken
	public static ArrayList<ItemStack> getDropsChicken(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityChicken target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.CHICKEN));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.CHICKEN, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.FEATHER));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.CHICKEN));
				drops.add(new ItemStack(Items.FEATHER));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityCow
	public static ArrayList<ItemStack> getDropsCow(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCow target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.BEEF));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.BEEF, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.LEATHER));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.BEEF));
				drops.add(new ItemStack(Items.LEATHER));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityDonkey
	public static ArrayList<ItemStack> getDropsDonkey(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityDonkey target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

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
	public static ArrayList<ItemStack> getDropsHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityHorse target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

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
	public static ArrayList<ItemStack> getDropsLlama(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLlama target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

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
	public static ArrayList<ItemStack> getDropsMooshroom(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMooshroom target, EntityLivingBase attacker)
	{
		World world = target.getEntityWorld();
		EntityCow entitycow = new EntityCow(world);
		entitycow.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
		entitycow.setHealth(target.getHealth());
		entitycow.renderYawOffset = target.renderYawOffset;

		if (target.hasCustomName())
		{
			entitycow.setCustomNameTag(target.getCustomNameTag());
		}

		target.setDead();

		world.spawnEntity(entitycow);

		for (int amount = 0; amount < 5; ++amount)
		{
			world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) target.height, target.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMule
	public static ArrayList<ItemStack> getDropsMule(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMule target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.LEATHER));

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
	public static ArrayList<ItemStack> getDropsOcelot(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityOcelot target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityParrot
	public static ArrayList<ItemStack> getDropsParrot(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityParrot target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.FEATHER));

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
	public static ArrayList<ItemStack> getDropsPig(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPig target, EntityLivingBase attacker)
	{
		if (target.getSaddled())
		{
			drops.add(new ItemStack(Items.SADDLE));

			target.setSaddled(false);
		}

		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.PORKCHOP));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.PORKCHOP, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.PORKCHOP, 3));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.PORKCHOP, 4));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityRabbit
	public static ArrayList<ItemStack> getDropsRabbit(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityRabbit target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.RABBIT));

				break;

			case UNCOMMON :

				drops.add(new ItemStack(Items.RABBIT, 2));

				break;

			case RARE :

				drops.add(new ItemStack(Items.RABBIT_HIDE));

				break;

			case EPIC :

				drops.add(new ItemStack(Items.RABBIT));
				drops.add(new ItemStack(Items.RABBIT_HIDE));

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySheep
	public static ArrayList<ItemStack> getDropsSheep(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySheep target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				if (target.getSheared())
				{
					drops.add(new ItemStack(Items.MUTTON));
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 1, target.getFleeceColor().getMetadata()));
				}

				break;

			case UNCOMMON :

				if (target.getSheared())
				{
					drops.add(new ItemStack(Items.MUTTON, 2));
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 2, target.getFleeceColor().getMetadata()));
				}

				break;

			case RARE :

				if (target.getSheared())
				{
					drops.add(new ItemStack(Items.MUTTON, 3));
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 3, target.getFleeceColor().getMetadata()));
				}

				break;

			case EPIC :

				if (target.getSheared())
				{
					drops.add(new ItemStack(Items.MUTTON, 4));
				}
				else
				{
					drops.add(new ItemStack(Item.getItemFromBlock(Blocks.WOOL), 4, target.getFleeceColor().getMetadata()));
				}

				break;
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySkeletonHorse
	public static ArrayList<ItemStack> getDropsSkeletonHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySkeletonHorse target, EntityLivingBase attacker)
	{
		switch (rarity)
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
	public static ArrayList<ItemStack> getDropsSquid(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySquid target, EntityLivingBase attacker)
	{
		switch (rarity)
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
	public static ArrayList<ItemStack> getDropsVillager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVillager target, EntityLivingBase attacker)
	{
		int listSize;

		switch (rarity)
		{
			case UNCOMMON :

				listSize = 2;

				break;

			case RARE :

				listSize = 3;

				break;

			case EPIC :

				listSize = 4;

				break;

			default :

				listSize = 1;
		}

		if (attacker instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) attacker;
			MerchantRecipeList merchantRecipeList = ((EntityVillager) target).getRecipes(player);

			if (merchantRecipeList != null && !merchantRecipeList.isEmpty())
			{
				for (int size = 0; size < listSize; size++)
				{
					int merchantRecipeListSize = merchantRecipeList.size();
					MerchantRecipe merchantRecipe = merchantRecipeList.get(getRandom().nextInt(merchantRecipeListSize));
					ItemStack stackMerchantCopy = merchantRecipe.getItemToSell().copy();

					if (!merchantRecipe.isRecipeDisabled() && stackMerchantCopy != null)
					{
						merchantRecipe.incrementToolUses();

						stackMerchantCopy.setCount(1);

						drops.add(stackMerchantCopy);
					}
				}
			}
		}

		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWolf
	public static ArrayList<ItemStack> getDropsWolf(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWolf target, EntityLivingBase attacker)
	{
		return getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombieHorse
	public static ArrayList<ItemStack> getDropsZombieHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombieHorse target, EntityLivingBase attacker)
	{
		switch (rarity)
		{
			case COMMON :

				drops.add(new ItemStack(Items.ROTTEN_FLESH));

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

	private static Random getRandom()
	{
		return new SecureRandom();
	}

	private static int getUsedItemDamage(ItemStack stackUsed, int sharpnessAmount)
	{
		Random random = getRandom();
		int usedDamage = (stackUsed.getMaxDamage() / sharpnessAmount);
		int itemDamage = (stackUsed.getMaxDamage() - usedDamage);

		itemDamage = Math.max(itemDamage, usedDamage);

		return random.nextInt(usedDamage);
	}

	private static EnumRarity getDropRarity(int sharpnessAmount)
	{
		Random random = getRandom();
		int rarityAmount = random.nextInt(RARITY_PERCENT);

		if (0 < sharpnessAmount)
		{
			rarityAmount = (rarityAmount - random.nextInt(sharpnessAmount * 10));
		}

		if (rarityAmount < RARITY_EPIC)
		{
			return EnumRarity.EPIC;// 10%
		}

		if (rarityAmount < RARITY_RARE)
		{
			return EnumRarity.RARE;// 20%
		}

		if (rarityAmount < RARITY_UNCOMMON)
		{
			return EnumRarity.UNCOMMON;// 30%
		}

		return EnumRarity.COMMON;// 40%
	}

	private static EnumRarity getDropRarity()
	{
		return getDropRarity(0);
	}

	private static boolean isSmelting(ItemStack stack, EntityLivingBase target)
	{
		return ((0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)) || target.isBurning());
	}

	private static ArrayList<ItemStack> getDrops(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (target instanceof IShearable)
		{
			IShearable shearable = (IShearable) target;
			World world = target.getEntityWorld();
			BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);

			if (shearable.isShearable(stack, world, pos))
			{
				drops.addAll(shearable.onSheared(stack, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack)));
			}
		}

		if (!drops.isEmpty())
		{
			if (isSmelting(stack, target))
			{
				ArrayList<ItemStack> dropsSmelting = new ArrayList<ItemStack>();

				for (ItemStack stackDrop : drops)
				{
					if (FurnaceRecipes.instance().getSmeltingResult(stackDrop).isEmpty())
					{
						dropsSmelting.add(stackDrop);
					}
					else
					{
						dropsSmelting.add(FurnaceRecipes.instance().getSmeltingResult(stackDrop).copy());
					}
				}

				drops.clear();

				drops.addAll(dropsSmelting);
			}
		}

		if (target instanceof EntityAgeable)
		{
			EntityAgeable entityAgeable = (EntityAgeable) target;

			if (entityAgeable.isChild())
			{
				drops.clear();
			}
		}

		if (MinecraftForge.EVENT_BUS.post(new CleaverAPI.ItemCleaverDropsEvent(drops, rarity, stack, target, attacker)))
		{
			drops.clear();
		}

		// TODO
		FMLLog.info("REALITY : %s", rarity.name());

		return drops;
	}

}
