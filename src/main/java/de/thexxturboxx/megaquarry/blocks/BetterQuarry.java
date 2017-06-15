package de.thexxturboxx.megaquarry.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BetterQuarry extends Quarry {

	public BetterQuarry() {
		super("blockBetterQuarry");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBetterQuarry();
	}

}