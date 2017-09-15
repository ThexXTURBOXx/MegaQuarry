package de.thexxturboxx.megaquarry.blocks;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Optional;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import de.thexxturboxx.megaquarry.packets.UpdateClientQuarryPacket;
import de.thexxturboxx.megaquarry.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class TileFilteredQuarry extends TileQuarry {

	private boolean isFiltered(Item i, int m) {
		for (Pair<Item, Optional<Integer>> p : CommonProxy.filtered) {
			if (p.getKey() == i) {
				return !p.getValue().isPresent() || p.getValue().get() == m;
			}
		}
		return false;
	}

	@Override
	public void quarryPart() {
		boolean flag;
		switch (redstoneControl) {
		case 0:
			flag = !world.isBlockPowered(pos);
			break;
		case 1:
			flag = world.isBlockPowered(pos);
			break;
		case 2:
			flag = true;
			break;
		default:
			flag = false;
		}
		if (flag && !getWorld().isRemote && active && getPos() != null) {
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
					boolean flag1 = false;
					if (yDig < 0) {
						yDig = 0;
						active = false;
						flag1 = true;
					}
					MegaQuarryMod.PACKETWRAPPER.sendToAll(new UpdateClientQuarryPacket(yDig, redstoneControl, active,
							getPos().getX(), getPos().getY(), getPos().getZ()));
					if(flag1) return;
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
						if (!isFiltered(i1.getItem(), i1.getMetadata())) {
							ItemStack is = fillItemsToInv(i1, this);
							if (is != null) {
								if (getWorld().getTileEntity(getPos().up()) != null
										&& getWorld().getTileEntity(getPos().up()) instanceof IInventory) {
									ItemStack s = fillItemsToInv(is,
											(IInventory) getWorld().getTileEntity(getPos().up()));
									if (s != null)
										Block.spawnAsEntity(getWorld(), getPos().up().up(), is);
								} else {
									Block.spawnAsEntity(getWorld(), getPos().up(), is);
								}
							}
						}
					}
				}
				xDig++;
			}
		}
	}

}