package schr0.cleaver.api;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CleaverEvent extends Event
{

	/**
	 * ItemCleaverNormalのEvent.
	 */
	public static class Normal extends CleaverEvent
	{
		@Cancelable
		public static class CleaveDrops extends Normal
		{
			private final ArrayList<ItemStack> drops;
			private final EnumRarity rarity;
			private final ItemStack stack;
			private final EntityLivingBase target;
			private final EntityLivingBase attacker;

			/**
			 * 剥ぎ取りEvent.
			 *
			 * @param drops
			 *            剥ぎ取りされるArrayList<ItemStack>.
			 * @param rarity
			 *            剥ぎ取りレアリティ（COMMON < UNCOMMON < RARE < EPIC）
			 * @param stack
			 *            ICleaverItemのItemStack.
			 * @param target
			 *            剥ぎ取りをされるEntityLivingBase.
			 * @param attacker
			 *            剥ぎ取りをするEntityLivingBase.
			 */
			public CleaveDrops(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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

			public EnumRarity getRarity()
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

}
