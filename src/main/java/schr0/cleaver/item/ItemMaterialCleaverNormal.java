package schr0.cleaver.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import schr0.cleaver.util.CleaverLang;

public class ItemMaterialCleaverNormal extends Item
{

	public ItemMaterialCleaverNormal()
	{
		this.setMaxStackSize(16);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(TextFormatting.ITALIC + new TextComponentTranslation(CleaverLang.ITEM_MATERIAL_CLEAVER_NORMAL_TIPS, new Object[0]).getFormattedText());
	}

}
