package schr0.cleaver.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.init.CleaverParticles;
import schr0.cleaver.packet.MessageParticleEntity;

public class ItemCleaverNormal extends ItemCleaver
{

	public ItemCleaverNormal()
	{
		super(CleaverMaterial.NORMAL);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		if (state.getMaterial() == Material.LEAVES)
		{
			return 15.0F;
		}

		if (state.getBlock() == Blocks.WOOL)
		{
			return 5.0F;
		}

		return super.getStrVsBlock(stack, state);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		Block block = blockIn.getBlock();

		if ((block == Blocks.REDSTONE_WIRE) || (block == Blocks.TRIPWIRE))
		{
			return true;
		}

		return super.canHarvestBlock(blockIn);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		World world = player.getEntityWorld();

		if (world.isRemote || player.capabilities.isCreativeMode)
		{
			return false;
		}

		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof IShearable)
		{
			IShearable shearable = (IShearable) block;

			if (shearable.isShearable(itemstack, world, pos))
			{
				Random random = this.getRandom(player);
				List<ItemStack> drops = shearable.onSheared(itemstack, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, itemstack));

				for (ItemStack stackDrop : drops)
				{
					float randomPos = 0.5F;
					double posXdrop = (double) pos.getX() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
					double posYdrop = (double) pos.getY() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
					double posZdrop = (double) pos.getZ() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
					EntityItem entityItem = new EntityItem(world, posXdrop, posYdrop, posZdrop, stackDrop);

					entityItem.setDefaultPickupDelay();

					world.spawnEntity(entityItem);
				}

				itemstack.damageItem(1, player);

				player.addStat(StatList.getBlockStats(block));

				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand)
	{
		World world = entity.getEntityWorld();

		if (world.isRemote)
		{
			return false;
		}

		if (entity instanceof IShearable)
		{
			IShearable target = (IShearable) entity;
			BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);

			if (target.isShearable(itemstack, entity.world, pos))
			{
				List<ItemStack> drops = target.onSheared(itemstack, world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, itemstack));

				for (ItemStack stack : drops)
				{
					this.onEntityDropItem(stack, entity);
				}

				itemstack.damageItem(1, entity);
			}

			return true;
		}

		return false;
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		int chance = (this.getSharpnessAmount(attackAmmount, stack, attacker) * 10);
		chance = Math.min(chance, 80);
		chance = Math.max(chance, 10);

		return (this.getRandom(attacker).nextInt(100) < chance);
	}

	@Override
	public boolean shouldAttackTarget(float attackAmmount, boolean canCleaveTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (!attacker.getEntityWorld().isRemote)
		{
			stack.damageItem(1, attacker);
		}

		if (canCleaveTarget)
		{
			Random random = this.getRandom(attacker);
			int sharpnessAmount = this.getSharpnessAmount(attackAmmount, stack, attacker);

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			int usedAmmount = sharpnessAmount;
			usedAmmount = Math.min(usedAmmount, 7);
			usedAmmount = Math.max(usedAmmount, 3);

			ArrayList<ItemStack> equipments = getCleaveEquipments(random.nextInt(usedAmmount), stack, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					this.onEntityDropItem(stackEquipment, target);
				}

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.ENTITY_NORMAL_DISARMAMENT));
				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return true;
			}

			int rarityAmmount = (100 - (sharpnessAmount * 2));
			rarityAmmount = Math.min(rarityAmmount, 70);
			rarityAmmount = Math.max(rarityAmmount, 10);

			ArrayList<ItemStack> drops = getCleaveDrops(random.nextInt(rarityAmmount), stack, target, attacker);

			if (!drops.isEmpty())
			{
				for (ItemStack stackDrop : drops)
				{
					this.onEntityDropItem(stackDrop, target);
				}

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.ENTITY_NORMAL_CLEAVE));
				target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);

				return true;
			}
		}

		return true;
	}

	@Override
	public boolean shouldDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner)
	{
		return true;
	}

	@Override
	public float onDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner)
	{
		return rawDamageAmmount;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private Random getRandom(Entity owner)
	{
		return owner.getEntityWorld().rand;
	}

	private int getSharpnessAmount(float attackAmmount, ItemStack stack, EntityLivingBase attacker)
	{
		int sharpnessAmount = Math.round(attackAmmount);

		int lootingAmmount = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
		lootingAmmount = Math.min(lootingAmmount, 3);
		lootingAmmount = Math.max(lootingAmmount, 1);

		if (attacker instanceof EntityPlayer)
		{
			lootingAmmount += (int) ((EntityPlayer) attacker).getLuck();
		}

		return (sharpnessAmount * lootingAmmount);
	}

	private static EnumRarity getCleaveRarity(int rarityAmount)
	{
		if (rarityAmount < 10)
		{
			return EnumRarity.EPIC;// 10
		}

		if (rarityAmount < 30)
		{
			return EnumRarity.RARE;// 20
		}

		if (rarityAmount < 60)
		{
			return EnumRarity.UNCOMMON;// 30
		}

		return EnumRarity.COMMON;// 40
	}

	private void onEntityDropItem(ItemStack stack, EntityLivingBase target)
	{
		EntityItem entityItem = target.entityDropItem(stack, 1.0F);
		Random random = this.getRandom(target);
		entityItem.motionY += random.nextFloat() * 0.05F;
		entityItem.motionX += (random.nextFloat() - random.nextFloat()) * 0.1F;
		entityItem.motionZ += (random.nextFloat() - random.nextFloat()) * 0.1F;

		entityItem.setDefaultPickupDelay();
	}

	private static ArrayList<ItemStack> getCleaveEquipments(int usedAmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> equipments = new ArrayList<ItemStack>();

		for (EntityEquipmentSlot equipmentSlot : EntityEquipmentSlot.values())
		{
			ItemStack stackEquipment = target.getItemStackFromSlot(equipmentSlot);

			if (!stackEquipment.isEmpty())
			{
				if (stackEquipment.isItemStackDamageable() && !stackEquipment.getItem().isDamaged(stackEquipment))
				{
					int stackAmount = (stackEquipment.getMaxDamage() - (stackEquipment.getMaxDamage() / usedAmount));

					stackEquipment.setItemDamage(Math.max(stackAmount, 0));
				}

				equipments.add(stackEquipment);

				target.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY);

				return equipments;
			}
		}

		return equipments;
	}

	private static ArrayList<ItemStack> getCleaveDrops(int rarityAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		World world = target.getEntityWorld();

		if (world.isRemote)
		{
			return drops;
		}

		ResourceLocation targetKey = EntityList.getKey(target);
		EnumRarity rarity = getCleaveRarity(rarityAmmount);

		// TODO /* ======================================== BOSS =====================================*/

		if (EntityList.getKey(EntityDragon.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsDragon(drops, rarity, stack, (EntityDragon) target, attacker);
		}

		if (EntityList.getKey(EntityWither.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsWither(drops, rarity, stack, (EntityWither) target, attacker);
		}

		// TODO /* ======================================== MONSTER =====================================*/

		if (EntityList.getKey(EntityBlaze.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsBlaze(drops, rarity, stack, (EntityBlaze) target, attacker);
		}

		if (EntityList.getKey(EntityCaveSpider.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsCaveSpider(drops, rarity, stack, (EntityCaveSpider) target, attacker);
		}

		if (EntityList.getKey(EntityCreeper.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsCreeper(drops, rarity, stack, (EntityCreeper) target, attacker);
		}

		if (EntityList.getKey(EntityElderGuardian.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsElderGuardian(drops, rarity, stack, (EntityElderGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityEnderman.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsEnderman(drops, rarity, stack, (EntityEnderman) target, attacker);
		}

		if (EntityList.getKey(EntityEndermite.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsEndermite(drops, rarity, stack, (EntityEndermite) target, attacker);
		}

		if (EntityList.getKey(EntityEvoker.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsEvoker(drops, rarity, stack, (EntityEvoker) target, attacker);
		}

		if (EntityList.getKey(EntityGhast.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsGhast(drops, rarity, stack, (EntityGhast) target, attacker);
		}

		if (EntityList.getKey(EntityGiantZombie.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsGiantZombie(drops, rarity, stack, (EntityGiantZombie) target, attacker);
		}

		if (EntityList.getKey(EntityGuardian.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsGuardian(drops, rarity, stack, (EntityGuardian) target, attacker);
		}

		if (EntityList.getKey(EntityHusk.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsHusk(drops, rarity, stack, (EntityHusk) target, attacker);
		}

		if (EntityList.getKey(EntityIllusionIllager.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsIllusionIllager(drops, rarity, stack, (EntityIllusionIllager) target, attacker);
		}

		if (EntityList.getKey(EntityIronGolem.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsIronGolem(drops, rarity, stack, (EntityIronGolem) target, attacker);
		}

		if (EntityList.getKey(EntityMagmaCube.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsMagmaCube(drops, rarity, stack, (EntityMagmaCube) target, attacker);
		}

		if (EntityList.getKey(EntityPigZombie.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsPigZombie(drops, rarity, stack, (EntityPigZombie) target, attacker);
		}

		if (EntityList.getKey(EntityPolarBear.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsPolarBear(drops, rarity, stack, (EntityPolarBear) target, attacker);
		}

		if (EntityList.getKey(EntityShulker.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsShulker(drops, rarity, stack, (EntityShulker) target, attacker);
		}

		if (EntityList.getKey(EntitySilverfish.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySkeleton.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSilverfish(drops, rarity, stack, (EntitySilverfish) target, attacker);
		}

		if (EntityList.getKey(EntitySlime.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSlime(drops, rarity, stack, (EntitySlime) target, attacker);
		}

		if (EntityList.getKey(EntitySnowman.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSnowman(drops, rarity, stack, (EntitySnowman) target, attacker);
		}

		if (EntityList.getKey(EntitySpider.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSpider(drops, rarity, stack, (EntitySpider) target, attacker);
		}

		if (EntityList.getKey(EntityStray.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsStray(drops, rarity, stack, (EntityStray) target, attacker);
		}

		if (EntityList.getKey(EntityVex.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsVex(drops, rarity, stack, (EntityVex) target, attacker);
		}

		if (EntityList.getKey(EntityVindicator.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsVindicator(drops, rarity, stack, (EntityVindicator) target, attacker);
		}

		if (EntityList.getKey(EntityWitch.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsWitch(drops, rarity, stack, (EntityWitch) target, attacker);
		}

		if (EntityList.getKey(EntityWitherSkeleton.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsWitherSkeleton(drops, rarity, stack, (EntityWitherSkeleton) target, attacker);
		}

		if (EntityList.getKey(EntityZombie.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsZombie(drops, rarity, stack, (EntityZombie) target, attacker);
		}

		if (EntityList.getKey(EntityZombieVillager.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsZombieVillager(drops, rarity, stack, (EntityZombieVillager) target, attacker);
		}

		// TODO /* ======================================== PASSIVE =====================================*/

		if (EntityList.getKey(EntityBat.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsBat(drops, rarity, stack, (EntityBat) target, attacker);
		}

		if (EntityList.getKey(EntityChicken.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsChicken(drops, rarity, stack, (EntityChicken) target, attacker);
		}

		if (EntityList.getKey(EntityCow.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsCow(drops, rarity, stack, (EntityCow) target, attacker);
		}

		if (EntityList.getKey(EntityDonkey.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsDonkey(drops, rarity, stack, (EntityDonkey) target, attacker);
		}

		if (EntityList.getKey(EntityHorse.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsHorse(drops, rarity, stack, (EntityHorse) target, attacker);
		}

		if (EntityList.getKey(EntityLlama.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsLlama(drops, rarity, stack, (EntityLlama) target, attacker);
		}

		if (EntityList.getKey(EntityMooshroom.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsMooshroom(drops, rarity, stack, (EntityMooshroom) target, attacker);
		}

		if (EntityList.getKey(EntityMule.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsMule(drops, rarity, stack, (EntityMule) target, attacker);
		}

		if (EntityList.getKey(EntityOcelot.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsOcelot(drops, rarity, stack, (EntityOcelot) target, attacker);
		}

		if (EntityList.getKey(EntityParrot.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsParrot(drops, rarity, stack, (EntityParrot) target, attacker);
		}

		if (EntityList.getKey(EntityPig.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsPig(drops, rarity, stack, (EntityPig) target, attacker);
		}

		if (EntityList.getKey(EntityRabbit.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsRabbit(drops, rarity, stack, (EntityRabbit) target, attacker);
		}

		if (EntityList.getKey(EntitySheep.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSheep(drops, rarity, stack, (EntitySheep) target, attacker);
		}

		if (EntityList.getKey(EntitySkeletonHorse.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSkeletonHorse(drops, rarity, stack, (EntitySkeletonHorse) target, attacker);
		}

		if (EntityList.getKey(EntitySquid.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsSquid(drops, rarity, stack, (EntitySquid) target, attacker);
		}

		if (EntityList.getKey(EntityVillager.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsVillager(drops, rarity, stack, (EntityVillager) target, attacker);
		}

		if (EntityList.getKey(EntityWolf.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsWolf(drops, rarity, stack, (EntityWolf) target, attacker);
		}

		if (EntityList.getKey(EntityZombieHorse.class).equals(targetKey))
		{
			return ItemCleaverNormalHelper.getDropsZombieHorse(drops, rarity, stack, (EntityZombieHorse) target, attacker);
		}

		return drops;
	}

}
