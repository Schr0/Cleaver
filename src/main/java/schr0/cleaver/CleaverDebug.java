package schr0.cleaver;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class CleaverDebug
{

	public static void infoBugMessage(EntityPlayer player, Class bugClass)
	{
		player.sendMessage(new TextComponentString(bugClass + " でバグ発生中！ 楽しく遊んでるのにゴメンね！ 報告してくれると助かります！"));
	}

}
