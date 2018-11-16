package schr0.cleaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import schr0.cleaver.api.ICleaverItem;

public class ItemCleaver extends ItemSword implements ICleaverItem
{

	private static final int SHARPNESS_AMOUNT_MIN = 2;
	private static final int SHARPNESS_AMOUNT_MAX = 8;

	private static final int USED_AMOUNT_MIN = 2;
	private static final int USED_AMOUNT_MAX = 10;

	private static final int CHANCE_PERCENT = 100;
	private static final int REALITY_PERCENT = 100;

	private float attackDamage;

	public ItemCleaver()
	{
		super(CleaverItems.TOOLMATERIAL_CLEAVER);

		this.attackDamage = CleaverItems.TOOLMATERIAL_CLEAVER.getAttackDamage();
	}

	@Override
	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0.0D, 0));
		}

		return multimap;
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

	// TODO /* ======================================== MOD START =====================================*/

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		int chance = (this.getSharpnessAmount(attackAmmount, stack, attacker) * 10);

		return (this.getRandom(attacker).nextInt(CHANCE_PERCENT) < chance);
	}

	@Override
	public boolean shouldAttackTarget(float attackAmmount, boolean canCleaveTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);

		if (canCleaveTarget)
		{
			Random random = this.getRandom(attacker);
			int sharpnessAmount = this.getSharpnessAmount(attackAmmount, stack, attacker);

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			ArrayList<ItemStack> equipments = ItemCleaverHelper.getCleaveEquipments(getUsedAmmount(random, sharpnessAmount), target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEquipment : equipments)
				{
					this.onEntityDropItem(stackEquipment, target);
				}

				CleaverPackets.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticle.ENTITY_DISARMAMENT));

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				return true;
			}

			ArrayList<ItemStack> drops = ItemCleaverHelper.getCleaveDrops(getCleaveRarity(random, sharpnessAmount), stack, target, attacker);

			if (!drops.isEmpty())
			{
				for (ItemStack stackDrop : drops)
				{
					this.onEntityDropItem(stackDrop, target);
				}

				CleaverPackets.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticle.ENTITY_CLEAVE));

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

	@Override
	public List<EntityItem> getDropsTarget(List<EntityItem> rawDrops, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawDrops;
	}

	private Random getRandom(Entity entity)
	{
		return entity.getEntityWorld().rand;
	}

	private void onEntityDropItem(ItemStack stack, EntityLivingBase target)
	{
		Random random = this.getRandom(target);
		EntityItem entityItem = target.entityDropItem(stack, 1.0F);
		entityItem.motionX += (random.nextFloat() - random.nextFloat()) * 0.1F;
		entityItem.motionY += random.nextFloat() * 0.05F;
		entityItem.motionZ += (random.nextFloat() - random.nextFloat()) * 0.1F;

		entityItem.setDefaultPickupDelay();
	}

	private int getSharpnessAmount(float attackAmmount, ItemStack stack, EntityLivingBase attacker)
	{
		int lootingAmmount = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);

		if (attacker instanceof EntityPlayer)
		{
			lootingAmmount += (int) ((EntityPlayer) attacker).getLuck();
		}

		int sharpnessAmount = ((int) Math.round(attackAmmount) * lootingAmmount);
		sharpnessAmount = Math.min(sharpnessAmount, SHARPNESS_AMOUNT_MAX);
		sharpnessAmount = Math.max(sharpnessAmount, SHARPNESS_AMOUNT_MIN);

		return sharpnessAmount;
	}

	private static int getUsedAmmount(Random random, int sharpnessAmount)
	{
		int usedAmmount = (sharpnessAmount + random.nextInt(sharpnessAmount));
		usedAmmount = Math.min(usedAmmount, USED_AMOUNT_MAX);
		usedAmmount = Math.max(usedAmmount, USED_AMOUNT_MIN);

		return usedAmmount;
	}

	private static EnumRarity getCleaveRarity(Random random, int sharpnessAmount)
	{
		int rarityAmount = random.nextInt(REALITY_PERCENT - random.nextInt(sharpnessAmount * 10));

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

}
