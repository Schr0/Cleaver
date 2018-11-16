package schr0.cleaver;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CleaverCreativeTabs
{

	public static final CreativeTabs ITEM = new CreativeTabs(Cleaver.MOD_ID + "." + "item")
	{

		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(CleaverItems.CLEAVER);
		}

	};

}
