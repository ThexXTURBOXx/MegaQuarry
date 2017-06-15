package de.thexxturboxx.megaquarry.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class TileBetterQuarry extends TileQuarry {

	@Override
	public void quarryPart() {
		if (!getWorld().isRemote && active && getPos() != null && yDig >= 0) {
			int chunkX = getPos().getX() >> 4;
			int chunkZ = getPos().getZ() >> 4;
			if (yDig < 0) {
				active = false;
			}
			if (active) {
				if (xDig >= 16) {
					xDig = 0;
					zDig++;
				}
				if (zDig >= 16) {
					zDig = 0;
					yDig--;
				}
				BlockPos block = new BlockPos(chunkX * 16 + xDig, yDig, chunkZ * 16 + zDig);
				if (!block.equals(getPos()) && getWorld().getBlockState(block).getBlock()
						.getBlockHardness(getWorld().getBlockState(block), getWorld(), block) >= 0.0F) {
					List<ItemStack> items = getWorld().getBlockState(block).getBlock().getDrops(getWorld(), block,
							getWorld().getBlockState(block), 1);
					final BlockPos todig = new BlockPos(chunkX * 16 + xDig, yDig, chunkZ * 16 + zDig);
					if (!getWorld().isAirBlock(todig)) {
						if (getWorld().getBlockState(todig).isFullBlock()) {
							getWorld().setBlockState(todig, Blocks.COBBLESTONE.getDefaultState());
						} else {
							getWorld().setBlockToAir(todig);
						}
					}
					for (ItemStack i1 : items) {
						ItemStack is = fillItemsToInv(i1, this);
						if (is != null) {
							if (getWorld().getTileEntity(getPos().up()) != null
									&& getWorld().getTileEntity(getPos().up()) instanceof IInventory) {
								ItemStack s = fillItemsToInv(is, (IInventory) getWorld().getTileEntity(getPos().up()));
								if (s != null)
									Block.spawnAsEntity(getWorld(), getPos().up().up(), is);
							} else {
								Block.spawnAsEntity(getWorld(), getPos().up(), is);
							}
						}
					}
				}
				xDig++;
			}
		}
	}

}