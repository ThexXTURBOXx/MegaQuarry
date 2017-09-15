package de.thexxturboxx.megaquarry.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import de.thexxturboxx.megaquarry.gui.MegaQuarryGuiHandler;
import de.thexxturboxx.megaquarry.packets.UpdateClientHandler;
import de.thexxturboxx.megaquarry.packets.UpdateClientQuarryPacket;
import de.thexxturboxx.megaquarry.packets.UpdateServerHandler;
import de.thexxturboxx.megaquarry.packets.UpdateServerQuarryPacket;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public static boolean hardmode;

	public static List<Pair<Item, Optional<Integer>>> filtered;

	private static String[] arrf;
	public void init(FMLInitializationEvent e) {
		filtered = parseFiltered(arrf);
		NetworkRegistry.INSTANCE.registerGuiHandler(MegaQuarryMod.instance, new MegaQuarryGuiHandler());
		int id = 0;
		MegaQuarryMod.PACKETWRAPPER.registerMessage(UpdateClientHandler.class, UpdateClientQuarryPacket.class, id++,
				Side.CLIENT);
		MegaQuarryMod.PACKETWRAPPER.registerMessage(UpdateServerHandler.class, UpdateServerQuarryPacket.class, id++,
				Side.SERVER);
	}
	public List<Pair<Item, Optional<Integer>>> parseFiltered(String[] arr) {
		List<Pair<Item, Optional<Integer>>> list = new ArrayList<>();
		for (String s : arr) {
			String[] split = s.split("/");
			String item = split[0];
			String meta = split[1];
			Item i = Item.getByNameOrId(item);
			if (i != null) {
				if (meta.equals("*")) {
					list.add(Pair.of(i, Optional.absent()));
				} else {
					try {
						int m = Integer.parseInt(meta);
						list.add(Pair.of(i, Optional.of(m)));
					} catch (NumberFormatException e) {
					}
				}
			}
		}
		return list;
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void preInit(FMLPreInitializationEvent e) {
		File configdir = new File("./config");
		File configfile = new File(configdir, MegaQuarryMod.MODID + ".cfg");
		if (!configfile.exists())
			configdir.mkdirs();
		Configuration config = new Configuration(configfile);
		config.load();
		hardmode = config.getBoolean("hardmode_recipes", "General", false, "Very hard recipes, only for hard men!");
		arrf = config.getStringList("filtered", "General",
				new String[] { "minecraft:cobblestone/*", "minecraft:dirt/*", "minecraft:sand/*", "minecraft:gravel/*",
						"minecraft:flint/*", "minecraft:sandstone/*", "minecraft:stone/*" },
				"Items, that get sorted out by the Filtered Quarry");
		config.save();
	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}

	public void registerRenderers() {
	}

}
