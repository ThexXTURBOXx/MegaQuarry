package de.thexxturboxx.megaquarry.packets;

import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateClientHandler implements IMessageHandler<UpdateClientQuarryPacket, IMessage> {

	@Override
	public IMessage onMessage(UpdateClientQuarryPacket message, MessageContext ctx) {
		TileEntity te = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(message.x, message.y, message.z));
		if (te instanceof TileQuarry) {
			TileQuarry tq = (TileQuarry) te;
			tq.redstoneControl = message.redstone;
			tq.yDig = message.height;
			tq.active = message.active;
		}
		return null;
	}

}