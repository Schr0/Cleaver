package schr0.cleaver.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.init.CleaverParticles;
import schr0.cleaver.packet.particleentity.MessageParticleEntity;
import schr0.cleaver.packet.particleposition.MessageParticlePosition;

public class ItemCleaverBlaze extends ItemCleaver
{

	private static final int HEAT_AMOUNT_MIN = 5;
	private static final int HEAT_AMOUNT_MAX = 20;

	private static final int POTION_DURATION = 100;
	private static final int POTION_DURATION_LIMIT = (11 * 20);

	private static final int CHAGE_COUNT_MIN = (1 * 20);
	private static final int CHAGE_COUNT_MAX = (8 * 20);
	private static final int CHAGE_COUNT_INTERVAL = (5 * 20);

	private static final int CHAGE_AMOUNT_MIN = 2;
	private static final int CHAGE_AMOUNT_MAX = 8;

	public ItemCleaverBlaze()
	{
		super(CleaverMaterial.BLAZE);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if (handIn == EnumHand.OFF_HAND)
		{
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}

		ItemStack itemstack = playerIn.getHeldItem(handIn);

		playerIn.setActiveHand(handIn);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		int timeCount = this.getMaxItemUseDuration(stack) - count;
		World world = player.getEntityWorld();

		if ((timeCount < CHAGE_COUNT_MIN) || world.isRemote)
		{
			return;
		}

		ItemCleaverBlazeHelper.onUpdateRestraint(this.getChageAmmount(timeCount), stack, player, timeCount);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		int timeCount = this.getMaxItemUseDuration(stack) - timeLeft;

		if (timeCount < CHAGE_COUNT_MIN)
		{
			return;
		}

		ItemCleaverBlazeHelper.attackBlazeExplosion(this.getHeatAmount(stack, entityLiving), this.getChageAmmount(timeCount), stack, entityLiving);

		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;

			player.getCooldownTracker().setCooldown(this, CHAGE_COUNT_INTERVAL);
		}

		entityLiving.swingArm(entityLiving.getActiveHand());

		stack.damageItem(10, entityLiving);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
	{
		World world = player.getEntityWorld();
		IBlockState state = world.getBlockState(pos);

		if (world.isRemote || player.capabilities.isCreativeMode || !player.canHarvestBlock(state))
		{
			return false;
		}

		Block block = state.getBlock();
		NonNullList<ItemStack> drops = NonNullList.create();

		block.getDrops(drops, world, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, itemstack));

		if (!drops.isEmpty())
		{
			Random random = this.getRandom(player);
			boolean isSmelting = false;

			for (ItemStack stackDrop : drops)
			{
				float randomPos = 0.5F;
				double posXdrop = (double) pos.getX() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
				double posYdrop = (double) pos.getY() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);
				double posZdrop = (double) pos.getZ() + ((random.nextFloat() * randomPos) + (double) (1.0F - randomPos) * 0.5D);

				if (FurnaceRecipes.instance().getSmeltingResult(stackDrop).isEmpty())
				{
					EntityItem entityItem = new EntityItem(world, posXdrop, posYdrop, posZdrop, stackDrop);
					entityItem.setDefaultPickupDelay();

					world.spawnEntity(entityItem);
				}
				else
				{
					ItemStack stackResult = FurnaceRecipes.instance().getSmeltingResult(stackDrop).copy();
					int xpValue = EntityXPOrb.getXPSplit((int) FurnaceRecipes.instance().getSmeltingExperience(stackDrop));
					EntityItem entityItem = new EntityItem(world, posXdrop, posYdrop, posZdrop, stackResult);
					entityItem.setDefaultPickupDelay();

					world.spawnEntity(entityItem);
					world.spawnEntity(new EntityXPOrb(world, posXdrop, posYdrop, posZdrop, xpValue));

					isSmelting = true;
				}
			}

			if (isSmelting)
			{
				itemstack.damageItem(1, player);

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(pos, CleaverParticles.POSITION_BLAZE_SMELTING));
			}

			player.addStat(StatList.getBlockStats(block));

			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

			return true;
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!(entityIn instanceof EntityLivingBase) || worldIn.isRemote)
		{
			return;
		}

		EntityLivingBase owner = (EntityLivingBase) entityIn;

		if (isSelected || owner.getHeldItemOffhand().isItemEqual(stack))
		{
			if (isSelected)
			{
				if (!owner.isHandActive())
				{
					ItemCleaverBlazeHelper.onUpdateBlazeShield(this.getHeatAmount(stack, owner), stack, owner);
				}
			}
			else
			{
				ItemCleaverBlazeHelper.onUpdateBlazeShield(this.getHeatAmount(stack, owner), stack, owner);
			}

			CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(owner, CleaverParticles.ENTITY_BLAZE_SHIELD));
		}
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		int chance = (this.getHeatAmount(stack, attacker) * 4);

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
			ItemCleaverBlazeHelper.cleavePotions(this.getHeatAmount(stack, attacker), stack, target, attacker);

			CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.ENTITY_BLAZE_CLEAVE));

			target.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.25F, 1.0F);

			return true;
		}

		return true;
	}

	@Override
	public boolean shouldDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner)
	{
		if (isSelected || owner.getHeldItemOffhand().isItemEqual(stack))
		{
			if (damageSource.isFireDamage())
			{
				return false;
			}
		}

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

	private int getHeatAmount(ItemStack stack, EntityLivingBase owner)
	{
		int potionAmount = 1;

		for (Potion potion : Potion.REGISTRY)
		{
			if (owner.isPotionActive(potion))
			{
				potionAmount += owner.getActivePotionEffect(potion).getAmplifier();
			}
		}

		int lootingAmmount = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);

		if (owner instanceof EntityPlayer)
		{
			lootingAmmount += (int) ((EntityPlayer) owner).getLuck();
		}

		int heatAmount = (potionAmount * lootingAmmount);
		heatAmount = Math.min(heatAmount, HEAT_AMOUNT_MAX);
		heatAmount = Math.max(heatAmount, HEAT_AMOUNT_MIN);

		return heatAmount;
	}

	private int getChageAmmount(int timeCount)
	{
		int chageAmount = (timeCount / 20);
		chageAmount = Math.min(chageAmount, CHAGE_AMOUNT_MAX);
		chageAmount = Math.max(chageAmount, CHAGE_AMOUNT_MIN);

		return chageAmount;
	}

}
