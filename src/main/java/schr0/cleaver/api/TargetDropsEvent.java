package schr0.cleaver.api;

import java.util.ArrayList;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 「ItemCleaver」の「剥ぎ取り」のEvent.
 *
 * @param drops
 *            ドロップアイテムのArrayList<ItemStack>.
 * @param rarity
 *            ドロップアイテムのレアリティ（COMMON < UNCOMMON < RARE < EPIC）
 * @param stack
 *            「ICleaverItem」のItemStack.
 * @param target
 *            「剥ぎ取り」されるEntityLivingBase.
 * @param attacker
 *            「剥ぎ取り」するEntityLivingBase.
 */
@Cancelable
public class TargetDropsEvent extends Event
{
	private final ArrayList<ItemStack> drops;
	private final EnumRarity rarity;
	private final ItemStack stack;
	private final EntityLivingBase target;
	private final EntityLivingBase attacker;

	public TargetDropsEvent(ArrayList<ItemStack> drops, EnumRarity rarity, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
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
