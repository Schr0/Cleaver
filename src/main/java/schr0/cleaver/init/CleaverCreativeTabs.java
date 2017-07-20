package schr0.cleaver.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import schr0.cleaver.Cleaver;

public class CleaverCreativeTabs
{

	public static final CreativeTabs ITEM = new CreativeTabs(Cleaver.MOD_ID + "." + "item")
	{

		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(CleaverItems.CLEAVER_NORMAL);
		}

	};

}
