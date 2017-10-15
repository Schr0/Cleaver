package schr0.cleaver;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import schr0.cleaver.init.CleaverEvent;
import schr0.cleaver.init.CleaverItems;
import schr0.cleaver.init.CleaverPacket;
import schr0.cleaver.init.CleaverRecipe;

@Mod(modid = Cleaver.MOD_ID, name = Cleaver.MOD_NAME, version = Cleaver.MOD_VERSION)
public class Cleaver
{

	/**
	 * ModのID.
	 */
	public static final String MOD_ID = "schr0cleaver";

	/**
	 * Modの名前.
	 */
	public static final String MOD_NAME = "Cleaver";

	/**
	 * Modのバージョン.
	 */
	public static final String MOD_VERSION = "1.1.0";

	/**
	 * ResourceLocationのDomain.
	 */
	public static final String MOD_RESOURCE_DOMAIN = MOD_ID + ":";

	@Mod.Instance(Cleaver.MOD_ID)
	public static Cleaver instance;

	/**
	 * 初期・設定イベント.
	 */
	@Mod.EventHandler
	public void construction(FMLConstructionEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);

		if (event.getSide().isClient())
		{
			// none
		}
	}

	/**
	 * 事前・設定イベント.
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// none

		if (event.getSide().isClient())
		{
			// none
		}
	}

	/**
	 * 事中・設定イベント.
	 */
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		(new CleaverEvent()).registerEvents();

		if (event.getSide().isClient())
		{
			(new CleaverPacket()).registerClientMessages();
		}
	}

	/**
	 * 事後・設定イベント.
	 */
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		// none

		if (event.getSide().isClient())
		{
			// none
		}
	}
	// TODO /* ======================================== MOD START =====================================*/

	/**
	 * Itemの登録.
	 */
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();

		(new CleaverItems()).registerItems(registry);
	}

	/**
	 * Item / Blockモデルの登録.
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent event)
	{
		(new CleaverItems()).registerModels();
	}

	/**
	 * Recipeの登録.
	 */
	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		IForgeRegistry<IRecipe> registry = event.getRegistry();

		(new CleaverRecipe()).registerRecipes(registry);
	}

}
