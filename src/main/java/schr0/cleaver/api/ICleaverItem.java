package schr0.cleaver.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ICleaverItem
{

	/**
	 * 攻撃力の値.
	 *
	 * @param rawAttackAmmount
	 *            攻撃力の元値.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃をされるEntityLivingBase.
	 * @param attacker
	 *            攻撃をするEntityLivingBase.
	 *
	 * @return 攻撃力の値.
	 */
	float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 剥ぎ取りの判定.
	 *
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            剥ぎ取りをされるEntityLivingBase.
	 * @param attacker
	 *            剥ぎ取りをするEntityLivingBase.
	 * @param attackAmmount
	 *            攻撃力の値.
	 *
	 * @return 剥ぎ取りの判定.
	 */
	boolean isCleaveTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount);

	/**
	 * 攻撃の判定.
	 *
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃をされるEntityLivingBase.
	 * @param attacker
	 *            攻撃をするEntityLivingBase.
	 * @param attackAmmount
	 *            攻撃力の値.
	 * @param isCleaveTarget
	 *            剥ぎ取りの判定.
	 *
	 * @return 攻撃の判定.
	 */
	boolean onAttackTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, float attackAmmount, boolean isCleaveTarget);

}
