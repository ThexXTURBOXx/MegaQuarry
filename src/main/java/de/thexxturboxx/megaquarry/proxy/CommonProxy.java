package de.thexxturboxx.megaquarry.proxy;

import de.thexxturboxx.megaquarry.MegaQuarryRegistry;
import de.thexxturboxx.megaquarry.blocks.TileBetterQuarry;
import de.thexxturboxx.megaquarry.blocks.TileFilteredQuarry;
import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

	public void init(FMLInitializationEvent e) {
		GameRegistry.registerTileEntity(TileQuarry.class, "quarry_tile_entity");
		GameRegistry.registerTileEntity(TileBetterQuarry.class, "better_quarry_tile_entity");
		GameRegistry.registerTileEntity(TileFilteredQuarry.class, "filtered_quarry_tile_entity");
	}

	public void postInit(FMLPostInitializationEvent e) {

	}

	public void preInit(FMLPreInitializationEvent e) {
		MegaQuarryRegistry.registerItems(e);
		MegaQuarryRegistry.registerBlocks(e);
	}

	public void registerItemRenderer(Item item, int meta, String id) {
	}

	public void registerRenderers() {
	}

}
