package schr0.cleaver;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CleaverNormalEvent extends Event
{

	private final ItemStack itemStack;
	private final EntityLivingBase owner;

	public CleaverNormalEvent(ItemStack stack, EntityLivingBase owner)
	{
		this.itemStack = stack;
		this.owner = owner;
	}

	public ItemStack getItemStack()
	{
		return this.itemStack;
	}

	public EntityLivingBase getOwner()
	{
		return this.owner;
	}

	@Cancelable
	public static class CleaveDropsEvent extends CleaverNormalEvent
	{
		private final ArrayList<ItemStack> drops;
		private final EntityLivingBase target;

		public CleaveDropsEvent(ArrayList<ItemStack> drops, EntityLivingBase target, ItemStack stack, EntityLivingBase owner)
		{
			super(stack, owner);
			this.drops = drops;
			this.target = target;
		}

		public ArrayList<ItemStack> getDrops()
		{
			return this.drops;
		}

		public EntityLivingBase getTarget()
		{
			return this.target;
		}
	}

}
