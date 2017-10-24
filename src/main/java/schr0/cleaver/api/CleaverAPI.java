package schr0.cleaver.api;

import net.minecraftforge.common.util.EnumHelper;

public class CleaverAPI
{

	/**
	 * CleaverMaterialの登録.
	 *
	 * @param name
	 *            内部名称.
	 * @param maxUses
	 *            耐久度.
	 * @param damageVsEntity
	 *            攻撃力.
	 * @param enchantability
	 *            エンチャント適正.
	 *
	 * @return CleaverMaterial.
	 *
	 */
	public static CleaverMaterial addCleaverMaterial(String name, int maxUses, float damageVsEntity, int enchantability)
	{
		final Class<?>[] paramTypes =
		{
				int.class, float.class, int.class
		};

		return EnumHelper.addEnum(CleaverMaterial.class, name, paramTypes, maxUses, damageVsEntity, enchantability);
	}

}
