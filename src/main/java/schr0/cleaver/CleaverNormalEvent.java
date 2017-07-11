package schr0.cleaver;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CleaverNormalEvent
{

	@Cancelable
	public static class CleaveDropsEvent extends Event
	{
		private final ArrayList<ItemStack> drops;
		private final int rarity;
		private final ItemStack stack;
		private final EntityLivingBase target;
		private final EntityLivingBase attacker;

		public CleaveDropsEvent(ArrayList<ItemStack> drops, int rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
		{
			this.drops = drops;
			this.rarity = rarity;
			this.stack = stack;
			this.target = target;
			this.attacker = attacker;;
		}

		public ArrayList<ItemStack> getDrops()
		{
			return this.drops;
		}

		public int getRarity()
		{
			return this.rarity;
		}

		public ItemStack getStack()
		{
			return this.stack;
		}

		public EntityLivingBase getTarget()
		{
			return this.target;
		}

		public EntityLivingBase getAttacker()
		{
			return this.attacker;
		}
	}

}
