package schr0.cleaver.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import schr0.cleaver.Cleaver;

public class CleaverRecipes
{

	public static final String KEY_RES = Cleaver.MOD_ID;
	public static final ResourceLocation RES_CLEAVER_NORMAL = new ResourceLocation(KEY_RES, CleaverItems.NAME_CLEAVER_NORMAL);
	public static final ResourceLocation RES_MATERIAL_CLEAVER_NORMAL = new ResourceLocation(KEY_RES, CleaverItems.NAME_MATERIAL_CLEAVER_NORMAL);

	public void registerRecipes(IForgeRegistry<IRecipe> registry)
	{
		registry.register(getItemCleaverNormal());
		registry.register(getItemMaterialCleaverNormal());
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static IRecipe getItemCleaverNormal()
	{
		return new ShapedOreRecipe(RES_CLEAVER_NORMAL, new ItemStack(CleaverItems.CLEAVER_NORMAL), new Object[]
		{
				" X ",
				"Y  ",
				"   ",

				'X', new ItemStack(CleaverItems.MATERIAL_CLEAVER_NORMAL),
				'Y', new ItemStack(Items.STICK),

		}).setRegistryName(RES_CLEAVER_NORMAL);
	}

	private static IRecipe getItemMaterialCleaverNormal()
	{
		return new ShapedOreRecipe(RES_MATERIAL_CLEAVER_NORMAL, new ItemStack(CleaverItems.MATERIAL_CLEAVER_NORMAL), new Object[]
		{
				" XX",
				"XX ",
				"   ",

				'X', new ItemStack(Items.IRON_INGOT),

		}).setRegistryName(RES_MATERIAL_CLEAVER_NORMAL);
	}

}
