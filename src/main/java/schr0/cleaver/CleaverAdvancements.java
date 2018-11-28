package schr0.cleaver;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CleaverAdvancements
{

	private static final String RES_KEY = Cleaver.MOD_ID;

	public static boolean completeChop(EntityPlayer player)
	{
		ResourceLocation key = new ResourceLocation(RES_KEY, "adventure/chop");

		return complete(player, key);
	}

	public static boolean completeDisarmament(EntityPlayer player)
	{
		ResourceLocation key = new ResourceLocation(RES_KEY, "adventure/disarmament");

		return complete(player, key);
	}

	private static boolean complete(EntityPlayer player, ResourceLocation key)
	{
		if (player instanceof EntityPlayerMP)
		{
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			Advancement advancement = playerMP.getServer().getAdvancementManager().getAdvancement(key);

			if (advancement != null)
			{
				playerMP.getAdvancements().grantCriterion(advancement, "mod_trigger");

				return true;
			}
		}

		return false;
	}

}
