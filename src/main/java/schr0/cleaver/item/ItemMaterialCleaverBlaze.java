package schr0.cleaver.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMaterialCleaverBlaze extends Item
{

	public ItemMaterialCleaverBlaze()
	{
		this.setMaxStackSize(16);
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
			owner.setFire(5 * 20);
		}
	}

}
