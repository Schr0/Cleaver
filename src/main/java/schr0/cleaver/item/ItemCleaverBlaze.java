package schr0.cleaver.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;

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
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		Block block = state.getBlock();
		Random random = this.getRandom(entityLiving);

		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;

			if (player.canHarvestBlock(state))
			{
				NonNullList<ItemStack> drops = NonNullList.create();

				block.getDrops(drops, worldIn, pos, state, EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack));

				if (!drops.isEmpty())
				{
					boolean isSmelting = false;

					for (ItemStack stackDrop : drops)
					{
						float r = 0.7F;
						double xR = (double) pos.getX() + ((random.nextFloat() * r) + (double) (1.0F - r) * 0.5D);
						double yR = (double) pos.getY() + ((random.nextFloat() * r) + (double) (1.0F - r) * 0.5D);
						double zR = (double) pos.getZ() + ((random.nextFloat() * r) + (double) (1.0F - r) * 0.5D);

						if (!FurnaceRecipes.instance().getSmeltingResult(stackDrop).isEmpty())
						{
							ItemStack stackResult = FurnaceRecipes.instance().getSmeltingResult(stackDrop).copy();
							int xpValue = EntityXPOrb.getXPSplit((int) FurnaceRecipes.instance().getSmeltingExperience(stackDrop));

							EntityItem entityItem = new EntityItem(worldIn, xR, yR, zR, stackResult);
							entityItem.setDefaultPickupDelay();

							if (!worldIn.isRemote)
							{
								worldIn.spawnEntity(entityItem);
								worldIn.spawnEntity(new EntityXPOrb(worldIn, xR, yR, zR, xpValue));
							}

							isSmelting = true;
						}
						else
						{
							EntityItem entityItem = new EntityItem(worldIn, xR, yR, zR, stackDrop);
							entityItem.setDefaultPickupDelay();

							if (!worldIn.isRemote)
							{
								worldIn.spawnEntity(entityItem);
							}
						}
					}

					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);

					if (isSmelting)
					{
						if (!worldIn.isRemote)
						{
							stack.damageItem(1, entityLiving);
						}

						worldIn.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);

						return true;
					}
				}
			}
		}

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
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount)
	{
		return false;
	}

	@Override
	public boolean onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount, boolean isCleaveTarget)
	{
		return true;
	}

	// TODO /* ======================================== MOD START =====================================*/

	private Random getRandom(EntityLivingBase owner)
	{
		return owner.getEntityWorld().rand;
	}

}
