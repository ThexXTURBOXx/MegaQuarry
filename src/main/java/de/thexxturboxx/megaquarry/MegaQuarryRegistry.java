package de.thexxturboxx.megaquarry;

import de.thexxturboxx.megaquarry.blocks.BetterQuarry;
import de.thexxturboxx.megaquarry.blocks.FilteredQuarry;
import de.thexxturboxx.megaquarry.blocks.Quarry;
import de.thexxturboxx.megaquarry.blocks.TileBetterQuarry;
import de.thexxturboxx.megaquarry.blocks.TileFilteredQuarry;
import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class MegaQuarryRegistry {

	public static final String ID = MegaQuarryMod.MODID + ":";

	public static final String QUARRY = ID + "quarry";
	public static final String BETTER_QUARRY = ID + "better_quarry";
	public static final String FILTERED_QUARRY = ID + "filtered_quarry";

	@ObjectHolder(QUARRY)
	public static Quarry quarry = (Quarry) new Quarry().setRegistryName(ID + "quarry");
	@ObjectHolder(BETTER_QUARRY)
	public static BetterQuarry better_quarry = (BetterQuarry) new BetterQuarry().setRegistryName(ID + "better_quarry");
	@ObjectHolder(FILTERED_QUARRY)
	public static FilteredQuarry filtered_quarry = (FilteredQuarry) new FilteredQuarry()
			.setRegistryName(ID + "filtered_quarry");
	@ObjectHolder(QUARRY)
	public static ItemBlock quarry_item = (ItemBlock) new ItemBlock(quarry).setRegistryName(ID + "quarry");
	@ObjectHolder(BETTER_QUARRY)
	public static ItemBlock better_quarry_item = (ItemBlock) new ItemBlock(better_quarry)
			.setRegistryName(ID + "better_quarry");
	@ObjectHolder(FILTERED_QUARRY)
	public static ItemBlock filtered_quarry_item = (ItemBlock) new ItemBlock(filtered_quarry)
			.setRegistryName(ID + "filtered_quarry");

	public static void registerItems(FMLPreInitializationEvent e) {

	}

	@SubscribeEvent
	public void registerBlocks(Register<Block> e) {
		GameRegistry.registerTileEntity(TileQuarry.class, ID + "quarry_tile_entity");
		GameRegistry.registerTileEntity(TileBetterQuarry.class, ID + "better_quarry_tile_entity");
		GameRegistry.registerTileEntity(TileFilteredQuarry.class, ID + "filtered_quarry_tile_entity");
		e.getRegistry().register(quarry);
		e.getRegistry().register(better_quarry);
		e.getRegistry().register(filtered_quarry);
	}

	@SubscribeEvent
	public void registerItems(Register<Item> e) {
		e.getRegistry().register(quarry_item);
		e.getRegistry().register(better_quarry_item);
		e.getRegistry().register(filtered_quarry_item);
	}

	@SubscribeEvent
	public void registerModels(ModelRegistryEvent e) {
		quarry.registerItemModel(quarry_item);
		better_quarry.registerItemModel(better_quarry_item);
		filtered_quarry.registerItemModel(filtered_quarry_item);
	}

}
