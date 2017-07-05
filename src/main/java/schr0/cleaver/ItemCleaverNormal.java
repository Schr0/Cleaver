package schr0.cleaver;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCleaverNormal extends ItemCleaver
{

	public ItemCleaverNormal()
	{
		super(CleaverMaterial.NORMAL);
	}

	@Override
	public boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		World world = target.getEntityWorld();
		boolean flag = (world.rand.nextInt(100) < 50);

		return flag;
	}

	@Override
	public void onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, boolean isCleaveTarget)
	{
		if (isCleaveTarget)
		{
			target.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
		}

		stack.damageItem(1, attacker);
	}

	// TODO /* ======================================== MOD START =====================================*/

}
