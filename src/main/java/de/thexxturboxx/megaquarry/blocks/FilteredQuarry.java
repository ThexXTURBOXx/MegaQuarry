package de.thexxturboxx.megaquarry.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FilteredQuarry extends Quarry {

	public FilteredQuarry() {
		super("blockFilteredQuarry");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFilteredQuarry();
	}

}