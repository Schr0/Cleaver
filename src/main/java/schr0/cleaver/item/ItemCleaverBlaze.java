package schr0.cleaver.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.packet.MessageParticlePosition;

public class ItemCleaverBlaze extends ItemCleaver
{

	public ItemCleaverBlaze()
	{
		super(CleaverMaterial.BLAZE);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Block block = state.getBlock();

		if (block == Blocks.WEB)
		{
			return 15.0F;
		}

		Material material = state.getMaterial();

		if ((material == Material.PLANTS) || (material == Material.VINE) || (material == Material.CORAL) || (material == Material.LEAVES) || (material == Material.GOURD))
		{
			return 1.5F;
		}

		return 1.0F;
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return (blockIn.getBlock() == Blocks.WEB);
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

				CleaverPacket.DISPATCHER.sendToAll(new MessageParticlePosition(pos, 0));
			}

			player.addStat(StatList.getBlockStats(block));

			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

			return true;
		}

		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if ((double) state.getBlockHardness(worldIn, pos) != 0.0D)
		{
			if (!worldIn.isRemote)
			{
				stack.damageItem(2, entityLiving);
			}

			return true;
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (!(entityIn instanceof EntityLivingBase) || !isSelected)
		{
			return;
		}

		Random random = this.getRandom(entityIn);
		EntityLivingBase owner = (EntityLivingBase) entityIn;

		if (owner.isBurning())
		{
			owner.extinguish();
		}

		particleFireBarrier(worldIn, owner, random);
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, EntityLivingBase target, ItemStack stack, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean isCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return false;
	}

	@Override
	public boolean onAttackTarget(float attackAmmount, boolean isCleaveTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (!attacker.getEntityWorld().isRemote)
		{
			stack.damageItem(1, attacker);
		}

		return true;
	}

	@Override
	public boolean shouldDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner)
	{
		if (isSelected && damageSource.isFireDamage())
		{
			return false;
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

	private static void particleFireBarrier(World world, EntityLivingBase owner, Random random)
	{
		world.spawnParticle(EnumParticleTypes.FLAME, owner.posX + (double) (random.nextFloat() * owner.width * 2.0F) - (double) owner.width, owner.posY + (double) (random.nextFloat() * owner.height), owner.posZ + (double) (random.nextFloat() * owner.width * 2.0F) - (double) owner.width, 0.0D, 0.0D, 0.0D, new int[0]);
	}

}
