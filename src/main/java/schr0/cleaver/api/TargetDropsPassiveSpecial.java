package schr0.cleaver.api;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class TargetDropsPassiveSpecial
{

	/* TODO ======================================== COW ===================================== /
	 * EntityCow
	 * EntityMooshroom
	 */

	// EntityCow
	public static ArrayList<ItemStack> getCow(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityCow target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMooshroom
	public static ArrayList<ItemStack> getMooshroom(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMooshroom target, EntityLivingBase attacker)
	{
		World world = target.getEntityWorld();
		EntityCow entityCow = new EntityCow(world);
		entityCow.setLocationAndAngles(target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch);
		entityCow.setHealth(target.getHealth());
		entityCow.renderYawOffset = target.renderYawOffset;

		if (target.hasCustomName())
		{
			entityCow.setCustomNameTag(target.getCustomNameTag());
		}

		for (int amount = 0; amount < 5; ++amount)
		{
			world.spawnEntity(new EntityItem(world, target.posX, target.posY + (double) target.height, target.posZ, new ItemStack(Blocks.RED_MUSHROOM)));
		}

		target.setDead();

		world.spawnEntity(entityCow);

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== HORSE ===================================== /
	 * EntityHorse
	 * EntityDonkey
	 * EntityMule
	 * EntitySkeletonHorse
	 * EntityZombieHorse
	 */

	// EntityHorse
	public static ArrayList<ItemStack> getHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityHorse target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityDonkey
	public static ArrayList<ItemStack> getDonkey(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityDonkey target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityMule
	public static ArrayList<ItemStack> getMule(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityMule target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntitySkeletonHorse
	public static ArrayList<ItemStack> getSkeletonHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySkeletonHorse target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityZombieHorse
	public static ArrayList<ItemStack> getZombieHorse(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityZombieHorse target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== RIDER ===================================== /
	 * EntityPig
	 * EntityLlama
	 */

	// EntityPig
	public static ArrayList<ItemStack> getPig(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityPig target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityLlama
	public static ArrayList<ItemStack> getLlama(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLlama target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== TAMEABLE ===================================== /
	 * EntityOcelot
	 * EntityParrot
	 * EntityWolf
	 */

	// EntityOcelot
	public static ArrayList<ItemStack> getOcelot(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityOcelot target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityParrot
	public static ArrayList<ItemStack> getParrot(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityParrot target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	// EntityWolf
	public static ArrayList<ItemStack> getWolf(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityWolf target, EntityLivingBase attacker)
	{
		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== WATER ===================================== /
	 * EntitySquid
	 */

	// EntitySquid
	public static ArrayList<ItemStack> getSquid(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntitySquid target, EntityLivingBase attacker)
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

	/* TODO ======================================== VILLAGER ===================================== /
	 * EntityVillager
	 */

	// EntityVillager
	public static ArrayList<ItemStack> getVillager(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityVillager target, EntityLivingBase attacker)
	{
		int sizeLimit;

		switch (rarity)
		{
			case UNCOMMON :

				sizeLimit = 2;

				break;

			case RARE :

				sizeLimit = 3;

				break;

			case EPIC :

				sizeLimit = 4;

				break;

			default :

				sizeLimit = 1;
		}

		if (attacker instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) attacker;
			MerchantRecipeList merchantRecipeList = ((EntityVillager) target).getRecipes(player);

			if (merchantRecipeList != null && !merchantRecipeList.isEmpty())
			{
				int merchantRecipeListSize = merchantRecipeList.size();

				for (int size = 0; size < sizeLimit; size++)
				{
					MerchantRecipe merchantRecipe = merchantRecipeList.get(ItemCleaverHelper.getRandom().nextInt(merchantRecipeListSize));
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

		return ItemCleaverHelper.getDrops(drops, rarity, stack, target, attacker);
	}

}
