package de.thexxturboxx.megaquarry.gui;

import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MegaQuarryGuiHandler implements IGuiHandler {

	public static final int QUARRY_GUI_ID = 0;

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == QUARRY_GUI_ID)
			return new GuiQuarry(player.inventory, (TileQuarry) world.getTileEntity(new BlockPos(x, y, z)), player);
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == QUARRY_GUI_ID)
			return new ContainerChest(player.inventory, (TileQuarry) world.getTileEntity(new BlockPos(x, y, z)),
					player);
		return null;
	}

}