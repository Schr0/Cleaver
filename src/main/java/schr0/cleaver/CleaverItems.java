package schr0.cleaver;

import java.util.ArrayList;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

public class CleaverItems
{

	public static final Item CLEAVER_NORMAL;

	public static final String NAME_CLEAVER_NORMAL = "cleaver_normal";

	static
	{
		CLEAVER_NORMAL = new ItemCleaverNormal().setUnlocalizedName(NAME_CLEAVER_NORMAL).setCreativeTab(CleaverCreativeTabs.ITEM);
	}

	public void registerItems(IForgeRegistry<Item> registry)
	{
		registerItem(registry, CLEAVER_NORMAL, NAME_CLEAVER_NORMAL);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		registerModel(CLEAVER_NORMAL);
	}

	// TODO /* ======================================== MOD START =====================================*/

	private static void registerItem(IForgeRegistry<Item> registry, Item item, int meta, String name)
	{
		item.setRegistryName(Cleaver.MOD_ID, name);

		registry.register(item);

		String domain = Cleaver.MOD_ID + ".";

		if (meta <= 0)
		{
			OreDictionary.registerOre(domain + name, item);
		}
		else
		{
			for (int i = 0; i < meta; i++)
			{
				OreDictionary.registerOre(domain + name + "_" + i, new ItemStack(item, 1, i));
			}
		}
	}

	private static void registerItem(IForgeRegistry<Item> registry, Item item, String name)
	{
		registerItem(registry, item, 0, name);
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Item item, int meta)
	{
		if (meta == 0)
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
			ModelBakery.registerItemVariants(item, item.getRegistryName());
		}
		else
		{
			ArrayList<ResourceLocation> models = Lists.newArrayList();

			for (int i = 0; i <= meta; i++)
			{
				ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + "_" + i, "inventory"));

				models.add(new ResourceLocation(item.getRegistryName() + "_" + i));
			}

			ModelBakery.registerItemVariants(item, models.toArray(new ResourceLocation[models.size()]));
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerModel(Item item)
	{
		registerModel(item, 0);
	}

}
