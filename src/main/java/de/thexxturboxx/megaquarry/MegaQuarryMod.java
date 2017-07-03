package de.thexxturboxx.megaquarry;

import de.thexxturboxx.megaquarry.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MegaQuarryMod.MODID, version = MegaQuarryMod.VERSION, name = MegaQuarryMod.NAME, updateJSON = MegaQuarryMod.UPDATE_JSON, dependencies = "required-after:forge@[14.21.1.2387,);", acceptedMinecraftVersions = "[1.12,1.13)")
public class MegaQuarryMod {

	public static final String MODID = "megaquarry";
	public static final String VERSION = "1.3";
	public static final String NAME = "Mega Quarry";
	public static final String UPDATE_JSON = "https://raw.githubusercontent.com/ThexXTURBOXx/UpdateJSONs/master/megaquarry.json";

	public static final CreativeTabs tabMegaQuarry = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(MegaQuarryRegistry.quarry);
		}
	};

	@Instance(MODID)
	public static MegaQuarryMod instance;

	@SidedProxy(modId = MODID, clientSide = "de.thexxturboxx.megaquarry.proxy.ClientProxy", serverSide = "de.thexxturboxx.megaquarry.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@EventHandler
	public void preinit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new MegaQuarryRegistry());
		proxy.preInit(e);
	}

}