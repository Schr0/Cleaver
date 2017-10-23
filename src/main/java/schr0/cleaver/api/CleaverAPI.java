package schr0.cleaver.api;

import net.minecraftforge.common.util.EnumHelper;

public class CleaverAPI
{

	// CleaverMaterialの登録.
	public static CleaverMaterial addCleaverMaterial(String name, int maxUses, float damageVsEntity, int enchantability)
	{
		final Class<?>[] paramTypes =
		{
				int.class, float.class, int.class
		};

		return EnumHelper.addEnum(CleaverMaterial.class, name, paramTypes, maxUses, damageVsEntity, enchantability);
	}

}
