package schr0.cleaver.api.item;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

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
	 *            攻撃されるEntityLivingBase.
	 * @param attacker
	 *            攻撃するEntityLivingBase.
	 *
	 * @return 攻撃力.
	 */
	float getAttackAmmount(float rawAttackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 「剥ぎ取り」の判定.
	 *
	 * @param attackAmmount
	 *            攻撃力.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            「剥ぎ取り」されるEntityLivingBase.
	 * @param attacker
	 *            「剥ぎ取り」するEntityLivingBase.
	 *
	 * @return 「剥ぎ取り」の判定.
	 */
	boolean canChopTarget(float attackAmmount, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * 攻撃の判定.
	 *
	 * @param attackAmmount
	 *            攻撃力.
	 * @param canChopTarget
	 *            「剥ぎ取り」の判定.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃されるEntityLivingBase.
	 * @param attacker
	 *            攻撃するEntityLivingBase.
	 *
	 * @return 攻撃の判定.
	 */
	boolean onAttackTarget(float attackAmmount, boolean canChopTarget, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * ターゲット死亡時の判定.
	 *
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃されるEntityLivingBase.
	 * @param attacker
	 *            攻撃するEntityLivingBase.
	 *
	 * @return ターゲット死亡時の判定.
	 */
	boolean onDeathTarget(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

	/**
	 * ターゲット死亡時のドロップアイテム.
	 *
	 * @param rawDeathDrops
	 *            元のドロップアイテム.
	 * @param stack
	 *            ICleaverItemのItemStack.
	 * @param target
	 *            攻撃されるEntityLivingBase.
	 * @param attacker
	 *            攻撃するEntityLivingBase.
	 *
	 * @return ターゲット死亡時のドロップアイテム.
	 */
	List<EntityItem> getDeathTargetDrops(List<EntityItem> rawDeathDrops, ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);

}
