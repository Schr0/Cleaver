package schr0.cleaver.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface ICleaverItem
{

	/**
	 * 攻撃力.
	 *
	 * @param rawAttackAmmount
	 *            元の攻撃力.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃をされるEntityLivingBase.
	 * @param attacker
	 *            攻撃をするEntityLivingBase.
	 * @return 攻撃力.
	 */
	float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 剥ぎ取りの判定.
	 *
	 * @param attackAmmount
	 *            攻撃力.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            剥ぎ取りをされるEntityLivingBase.
	 * @param attacker
	 *            剥ぎ取りをするEntityLivingBase.
	 * @return 剥ぎ取りの判定.
	 */
	boolean canCleaveTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 攻撃の判定.
	 *
	 * @param attackAmmount
	 *            攻撃力.
	 * @param canCleaveTarget
	 *            剥ぎ取りの判定.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃をされるEntityLivingBase.
	 * @param attacker
	 *            攻撃をするEntityLivingBase.
	 *
	 * @return 攻撃の判定.
	 */
	boolean shouldAttackTarget(float attackAmmount, boolean canCleaveTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 所持者ダメージの判定.
	 *
	 * @param rawDamageAmmount
	 *            元の所持者ダメージ値.
	 * @param damageSource
	 *            所持者が受けたDamageSource.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param slot
	 *            所持しているスロットの番号.
	 * @param isSelected
	 *            手に持っているかの判定.
	 * @param owner
	 *            所持者のEntityLivingBase.
	 *
	 * @return 所持者のダメージ判定.
	 */
	boolean shouldDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner);

	/**
	 * 所持者ダメージの処理.
	 *
	 * @param rawDamageAmmount
	 *            元の所持者ダメージ値.
	 * @param damageSource
	 *            所持者が受けたDamageSource.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param slot
	 *            所持しているスロットの番号.
	 * @param isSelected
	 *            手に持っているかの判定.
	 * @param owner
	 *            所持者のEntityLivingBase.
	 *
	 * @return 所持者ダメージ値.
	 */
	float onDamageOwner(float rawDamageAmmount, DamageSource damageSource, ItemStack stack, int slot, boolean isSelected, EntityLivingBase owner);

}
