package schr0.cleaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import schr0.cleaver.api.item.ItemSimpleCleaver;
import schr0.cleaver.api.itemcleaver.ItemCleaverHelper;

public class ItemCleaver extends ItemSimpleCleaver
{

	private static final int SHARPNESS_AMOUNT_MIN = 2;
	private static final int SHARPNESS_AMOUNT_MAX = 8;
	private static final int CHANCE_PERCENT = 100;

	public ItemCleaver()
	{
		super(CleaverItems.TOOLMATERIAL_CLEAVER);
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
					float posRandom = 0.5F;
					double posXdrop = (double) pos.getX() + ((random.nextFloat() * posRandom) + (double) (1.0F - posRandom) * 0.5D);
					double posYdrop = (double) pos.getY() + ((random.nextFloat() * posRandom) + (double) (1.0F - posRandom) * 0.5D);
					double posZdrop = (double) pos.getZ() + ((random.nextFloat() * posRandom) + (double) (1.0F - posRandom) * 0.5D);
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
					this.onDropItems(entity, stack);
				}

				itemstack.damageItem(1, entity);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean canChopTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		int sharpnessAmount = this.getSharpnessAmount(attackAmmount, stack, attacker);

		return (this.getRandom(attacker).nextInt(CHANCE_PERCENT) < (sharpnessAmount * 10));
	}

	@Override
	public boolean onAttackTarget(float attackAmmount, boolean canChopTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);

		if (canChopTarget)
		{
			Random random = this.getRandom(attacker);
			int sharpnessAmount = this.getSharpnessAmount(attackAmmount, stack, attacker);

			if (target instanceof EntityLiving)
			{
				((EntityLiving) target).setCanPickUpLoot(false);
			}

			ArrayList<ItemStack> equipments = ItemCleaverHelper.getDisarmamentEquipments(sharpnessAmount, target, attacker);

			if (!equipments.isEmpty())
			{
				for (ItemStack stackEq : equipments)
				{
					this.onDropItems(target, stackEq);
				}

				CleaverPackets.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.TARGET_DISARMAMENT));

				target.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

				if (attacker instanceof EntityPlayer)
				{
					CleaverAdvancements.completeDisarmament((EntityPlayer) attacker);
				}

				return true;
			}

			ArrayList<ItemStack> drops = ItemCleaverHelper.getChopDrops(sharpnessAmount, stack, target, attacker);

			if (!drops.isEmpty())
			{
				for (ItemStack stackDrop : drops)
				{
					this.onDropItems(target, stackDrop);
				}

				CleaverPackets.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.TARGET_CHOP));

				target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);

				if (attacker instanceof EntityPlayer)
				{
					CleaverAdvancements.completeChop((EntityPlayer) attacker);
				}

				return true;
			}
		}

		return true;
	}

	// TODO /* ======================================== MOD START =====================================*/

	public int getSharpnessAmount(float attackAmmount, ItemStack stack, EntityLivingBase attacker)
	{
		attackAmmount = Math.max(attackAmmount, 1.0F);
		int lootingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);

		if (attacker instanceof EntityPlayer)
		{
			lootingLevel += (int) ((EntityPlayer) attacker).getLuck();
		}

		int sharpnessAmount = (Math.round(attackAmmount) * lootingLevel);

		sharpnessAmount = Math.min(sharpnessAmount, SHARPNESS_AMOUNT_MAX);
		sharpnessAmount = Math.max(sharpnessAmount, SHARPNESS_AMOUNT_MIN);

		// TODO
		// FMLLog.info("sharpnessAmount : %d", sharpnessAmount);

		return sharpnessAmount;
	}

	public int getSharpnessAmount(ItemStack stack, EntityLivingBase attacker)
	{
		return getSharpnessAmount(1.0F, stack, attacker);
	}

	private void onDropItems(EntityLivingBase target, ItemStack stack)
	{
		EntityItem entityItem = target.entityDropItem(stack, 1.0F);
		Random random = this.getRandom(target);
		entityItem.motionX += (random.nextFloat() - random.nextFloat()) * 0.1F;
		entityItem.motionY += random.nextFloat() * 0.05F;
		entityItem.motionZ += (random.nextFloat() - random.nextFloat()) * 0.1F;

		entityItem.setDefaultPickupDelay();
	}

	private Random getRandom(Entity entity)
	{
		return entity.getEntityWorld().rand;
	}

}
