package schr0.cleaver.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class CleaverAPI
{

	/**
	 * 「Cleaver」のModID.
	 */
	public static final String MOD_ID = "schr0cleaver";

	/**
	 * 「CleaverMaterial」の登録.
	 *
	 * @param name
	 *            名称.
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

	/**
	 * 「ItemCleaverNormal」の判定.
	 *
	 * @param stak
	 *            判定するItemStack.
	 *
	 * @return 「ItemCleaverNormal」の判定.
	 */
	public static boolean isItemCleaverNormal(ItemStack stak)
	{
		if (stak.isEmpty())
		{
			return false;
		}

		return stak.getItem().getRegistryName().equals(new ResourceLocation(MOD_ID, "cleaver_normal"));
	}

	/**
	 * 「ItemMaterialCleaverNormal」の判定.
	 *
	 * @param stak
	 *            判定するItemStack.
	 *
	 * @return 「ItemMaterialCleaverNormal」の判定.
	 */
	public static boolean isItemMaterialCleaverNormal(ItemStack stak)
	{
		if (stak.isEmpty())
		{
			return false;
		}

		return stak.getItem().getRegistryName().equals(new ResourceLocation(MOD_ID, "material_cleaver_normal"));
	}

}
