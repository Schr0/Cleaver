package schr0.cleaver.api;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public abstract class ItemSimpleCleaver extends ItemSword implements ICleaverItem
{

	private float attackDamage;
	private float attackSpeed;

	public ItemSimpleCleaver(Item.ToolMaterial material)
	{
		super(material);

		this.attackDamage = material.getAttackDamage();
		this.attackSpeed = 0.0F;
	}

	@Override
	public float getAttackDamage()
	{
		return this.attackDamage;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();

		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.attackDamage, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) this.attackSpeed, 0));
		}

		return multimap;
	}

	@Override
	public float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawAttackAmmount;
	}

	@Override
	public boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return false;
	}

	@Override
	public boolean onAttackTarget(float attackAmmount, boolean canCleaveTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return true;
	}

	@Override
	public boolean onDeathTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return true;
	}

	@Override
	public List<EntityItem> getDropsTarget(List<EntityItem> rawDrops, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		return rawDrops;
	}

}
