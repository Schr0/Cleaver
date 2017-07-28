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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import schr0.cleaver.api.CleaverMaterial;
import schr0.cleaver.api.ItemCleaver;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.init.CleaverParticles;
import schr0.cleaver.packet.MessageParticleEntity;
import schr0.cleaver.packet.MessageParticlePosition;

public class ItemCleaverBlaze extends ItemCleaver
{

	private static final int POTION_DURATION = 100;
	private static final int POTION_DURATION_LIMIT = (11 * 20);

	public ItemCleaverBlaze()
	{
		super(CleaverMaterial.BLAZE);
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
		if (!isSelected || !(entityIn instanceof EntityLivingBase) || worldIn.isRemote)
		{
			return;
		}

		Random random = this.getRandom(entityIn);
		EntityLivingBase owner = (EntityLivingBase) entityIn;
		int heatAmount = this.getHeatAmount(stack, owner);

		if (owner.isBurning())
		{
			owner.extinguish();
		}

		for (Potion potion : Potion.REGISTRY)
		{
			if (potion.isInstant())
			{
				continue;
			}

			if (potion.isBadEffect())
			{
				if (owner.isPotionActive(potion))
				{
					owner.removePotionEffect(potion);
				}

				continue;
			}
			else
			{
				if (owner.isPotionActive(potion))
				{
					PotionEffect potionEffect = owner.getActivePotionEffect(potion);

					if (potionEffect.getDuration() < POTION_DURATION_LIMIT)
					{
						int duration = (potionEffect.getDuration() + (heatAmount * POTION_DURATION));

						duration = Math.min(duration, (60 * 20));
						duration = Math.max(duration, (15 * 20));

						owner.addPotionEffect(new PotionEffect(potion, (20 + duration), potionEffect.getAmplifier()));

						stack.damageItem(1, owner);
					}
				}
				else
				{
					continue;
				}
			}
		}

		CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(owner, CleaverParticles.ENTITY_BLAZE_SHIELD));
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		int chance = Math.min(((this.getHeatAmount(stack, attacker) * 4) + 10), 80);

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
			int heatAmount = this.getHeatAmount(stack, attacker);

			target.clearActivePotions();

			target.setFire(heatAmount * 20);

			for (Potion potion : Potion.REGISTRY)
			{
				if (potion.isInstant())
				{
					continue;
				}

				if (potion.isBadEffect())
				{
					continue;
				}
				else
				{
					if (attacker.isPotionActive(potion))
					{
						continue;
					}
					else
					{
						int duration = (heatAmount * POTION_DURATION);

						duration = Math.min(duration, (60 * 20));
						duration = Math.max(duration, (15 * 20));

						attacker.addPotionEffect(new PotionEffect(potion, (20 + duration)));

						CleaverPacket.DISPATCHER.sendToAll(new MessageParticleEntity(target, CleaverParticles.ENTITY_BLAZE_CLEAVE));

						target.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);

						return true;
					}
				}
			}
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

	private int getHeatAmount(ItemStack stack, EntityLivingBase owner)
	{
		int potionCount = 0;

		for (Potion potion : Potion.REGISTRY)
		{
			if (owner.isPotionActive(potion))
			{
				potionCount++;
			}
		}

		int lootingAmmount = Math.min(EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack), 1);

		if (owner instanceof EntityPlayer)
		{
			lootingAmmount += (int) ((EntityPlayer) owner).getLuck();
		}

		return (potionCount + lootingAmmount);
	}

}
