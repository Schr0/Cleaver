package schr0.cleaver.api;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
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
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;

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
	public static ArrayList<ItemStack> getTargetEquipments(int sharpnessAmount, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> equipments = new ArrayList<ItemStack>();

		for (EntityEquipmentSlot slotEq : EntityEquipmentSlot.values())
		{
			ItemStack stackEq = target.getItemStackFromSlot(slotEq);

			if (!stackEq.isEmpty())
			{
				if (stackEq.isItemStackDamageable() && !stackEq.getItem().isDamaged(stackEq))
				{
					stackEq.setItemDamage(getUsedItemDamage(stackEq, sharpnessAmount));
				}

				equipments.add(stackEq);

				target.setItemStackToSlot(slotEq, ItemStack.EMPTY);

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
	public static ArrayList<ItemStack> getTargetDrops(int sharpnessAmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return new ArrayList<ItemStack>();
		}

		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		EnumRarity rarity = getDropRarity(sharpnessAmount);
		ResourceLocation targetKey = EntityList.getKey(target);

		/* TODO ======================================== OVERWORLD ===================================== /
		 * EntityCreeper
		 * EntityEnderman
		 * EntityEndermite
		 * EntityGiantZombie
		 * EntityPolarBear
		 * EntitySilverfish
		 * EntityWitch
		 */

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			return TargetDropsMonster.getCreeper(drops, rarity, stack, (EntityCreeper) target, attacker);
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			return TargetDropsMonster.getEnderman(drops, rarity, stack, (EntityEnderman) target, attacker);
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return TargetDropsMonster.getEndermite(drops, rarity, stack, (EntityEndermite) target, attacker);
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			return TargetDropsMonster.getGiantZombie(drops, rarity, stack, (EntityGiantZombie) target, attacker);
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			return TargetDropsMonster.getPolarBear(drops, rarity, stack, (EntityPolarBear) target, attacker);
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return TargetDropsMonster.getSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			return TargetDropsMonster.getWitch(drops, rarity, stack, (EntityWitch) target, attacker);
		}

		/* TODO ======================================== NETHER ===================================== /
		 * EntityBlaze
		 * EntityGhast
		 */

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			return TargetDropsMonster.getBlaze(drops, rarity, stack, (EntityBlaze) target, attacker);
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			return TargetDropsMonster.getGhast(drops, rarity, stack, (EntityGhast) target, attacker);
		}

		/* TODO ======================================== END ===================================== /
		 * EntityShulker
		 */

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return TargetDropsMonster.getShulker(drops, rarity, stack, (EntityShulker) target, attacker);
		}

		/* TODO ======================================== BOSS ===================================== /
		 * EntityDragon
		 * EntityWither
		 */

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getDragon(drops, rarity, stack, (EntityDragon) target, attacker);
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getWither(drops, rarity, stack, (EntityWither) target, attacker);
		}

		/* TODO ======================================== ZOMBIE ===================================== /
		 * EntityZombie
		 * EntityZombieVillager
		 * EntityHusk
		 * EntityPigZombie
		 */

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getZombie(drops, rarity, stack, (EntityZombie) target, attacker);
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getZombieVillager(drops, rarity, stack, (EntityZombieVillager) target, attacker);
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getHusk(drops, rarity, stack, (EntityHusk) target, attacker);
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getPigZombie(drops, rarity, stack, (EntityPigZombie) target, attacker);
		}

		/* TODO ======================================== SKELETON ===================================== /
		 * EntitySkeleton
		 * EntityStray
		 * EntityWitherSkeleton
		 */

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getSkeleton(drops, rarity, stack, (EntitySkeleton) target, attacker);
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getStray(drops, rarity, stack, (EntityStray) target, attacker);
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getWitherSkeleton(drops, rarity, stack, (EntityWitherSkeleton) target, attacker);
		}

		/* TODO ======================================== SPIDER ===================================== /
		 * EntitySpider
		 * EntityCaveSpider
		 */

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getSpider(drops, rarity, stack, (EntitySpider) target, attacker);
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getCaveSpider(drops, rarity, stack, (EntityCaveSpider) target, attacker);
		}

		/* TODO ======================================== SLIME ===================================== /
		 * EntitySlime
		 * EntityMagmaCube
		 */

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getSlime(drops, rarity, stack, (EntitySlime) target, attacker);
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getMagmaCube(drops, rarity, stack, (EntityMagmaCube) target, attacker);
		}

		/* TODO ======================================== GOLEM ===================================== /
		 * EntitySnowman
		 * EntityIronGolem
		 */

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getSnowman(drops, rarity, stack, (EntitySnowman) target, attacker);
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getIronGolem(drops, rarity, stack, (EntityIronGolem) target, attacker);
		}

		/* TODO ======================================== ILLAGER ===================================== /
		 * EntityEvoker
		 * EntityVindicator
		 * EntityVex
		 * EntityIllusionIllager
		 */

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getEvoker(drops, rarity, stack, (EntityEvoker) target, attacker);
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getVindicator(drops, rarity, stack, (EntityVindicator) target, attacker);
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getVex(drops, rarity, stack, (EntityVex) target, attacker);
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getIllusionIllager(drops, rarity, stack, (EntityIllusionIllager) target, attacker);
		}

		/* TODO ======================================== GUARDIAN ===================================== /
		 * EntityGuardian
		 * EntityElderGuardian
		 */

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getGuardian(drops, rarity, stack, (EntityGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			return TargetDropsMonsterSpecial.getElderGuardian(drops, rarity, stack, (EntityElderGuardian) target, attacker);
		}

		/* TODO ======================================== OVERWORLD ===================================== /
		 * EntityBat
		 * EntityChicken
		 * EntityRabbit
		 * EntitySheep
		 */

		if (EntityList.getKey(EntityBat.class).equals(targetKey))
		{
			return TargetDropsPassive.getBat(drops, rarity, stack, (EntityBat) target, attacker);
		}

		if (EntityList.getKey(EntityChicken.class).equals(targetKey))
		{
			return TargetDropsPassive.getChicken(drops, rarity, stack, (EntityChicken) target, attacker);
		}

		if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
		{
			return TargetDropsPassive.getRabbit(drops, rarity, stack, (EntityRabbit) target, attacker);
		}

		if (EntityList.getKey(EntitySheep.class).equals(targetKey))
		{
			return TargetDropsPassive.getSheep(drops, rarity, stack, (EntitySheep) target, attacker);
		}

		/* TODO ======================================== COW ===================================== /
		 * EntityCow
		 * EntityMooshroom
		 */

		if (EntityList.getKey(EntityCow.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getCow(drops, rarity, stack, (EntityCow) target, attacker);
		}

		if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getMooshroom(drops, rarity, stack, (EntityMooshroom) target, attacker);
		}

		/* TODO ======================================== HORSE ===================================== /
		 * EntityHorse
		 * EntityDonkey
		 * EntityMule
		 * EntitySkeletonHorse
		 * EntityZombieHorse
		 */

		if (EntityList.getKey(EntityHorse.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getHorse(drops, rarity, stack, (EntityHorse) target, attacker);
		}

		if (EntityList.getKey(EntityDonkey.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getDonkey(drops, rarity, stack, (EntityDonkey) target, attacker);
		}

		if (EntityList.getKey(EntityMule.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getMule(drops, rarity, stack, (EntityMule) target, attacker);
		}

		if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getSkeletonHorse(drops, rarity, stack, (EntitySkeletonHorse) target, attacker);
		}

		if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getZombieHorse(drops, rarity, stack, (EntityZombieHorse) target, attacker);
		}

		/* TODO ======================================== RIDER ===================================== /
		 * EntityLlama
		 * EntityPig
		 */

		if (EntityList.getKey(EntityLlama.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getLlama(drops, rarity, stack, (EntityLlama) target, attacker);
		}

		if (EntityList.getKey(EntityPig.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getPig(drops, rarity, stack, (EntityPig) target, attacker);
		}

		/* TODO ======================================== TAMEABLE ===================================== /
		 * EntityOcelot
		 * EntityParrot
		 * EntityWolf
		 */

		if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getOcelot(drops, rarity, stack, (EntityOcelot) target, attacker);
		}

		if (EntityList.getKey(EntityParrot.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getParrot(drops, rarity, stack, (EntityParrot) target, attacker);
		}

		if (EntityList.getKey(EntityWolf.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getWolf(drops, rarity, stack, (EntityWolf) target, attacker);
		}

		/* TODO ======================================== WATER ===================================== /
		 * EntitySquid
		 */

		if (EntityList.getKey(EntitySquid.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getSquid(drops, rarity, stack, (EntitySquid) target, attacker);
		}

		/* TODO ======================================== VILLAGER ===================================== /
		 * EntityVillager
		 */

		if (EntityList.getKey(EntityVillager.class).equals(targetKey))
		{
			return TargetDropsPassiveSpecial.getVillager(drops, rarity, stack, (EntityVillager) target, attacker);
		}

		return drops;
	}

	protected static Random getRandom()
	{
		return new SecureRandom();
	}

	protected static int getUsedItemDamage(ItemStack stackUsed, int sharpnessAmount)
	{
		Random random = getRandom();
		int usedDamage = (stackUsed.getMaxDamage() / sharpnessAmount);
		int itemDamage = (stackUsed.getMaxDamage() - usedDamage);

		itemDamage = Math.max(itemDamage, usedDamage);

		return random.nextInt(usedDamage);
	}

	protected static EnumRarity getDropRarity(int sharpnessAmount)
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

	protected static EnumRarity getDropRarity()
	{
		return getDropRarity(0);
	}

	protected static boolean isSmelting(ItemStack stack, EntityLivingBase target)
	{
		return ((0 < EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)) || target.isBurning());
	}

	protected static ArrayList<ItemStack> getDrops(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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

		if (MinecraftForge.EVENT_BUS.post(new TargetDropsEvent(drops, rarity, stack, target, attacker)))
		{
			drops.clear();
		}

		return drops;
	}

}
