package de.thexxturboxx.megaquarry.packets;

import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateServerHandler implements IMessageHandler<UpdateServerQuarryPacket, IMessage> {

	@Override
	public IMessage onMessage(UpdateServerQuarryPacket message, MessageContext ctx) {
		EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
		serverPlayer.getServerWorld().addScheduledTask(() -> {
			TileEntity te = serverPlayer.getServerWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));
			if (te instanceof TileQuarry) {
				TileQuarry tq = (TileQuarry) te;
				tq.redstoneControl = message.redstone;
			}
		});
		TileEntity te = serverPlayer.getServerWorld().getTileEntity(new BlockPos(message.x, message.y, message.z));
		TileQuarry tq = null;
		if (te instanceof TileQuarry) {
			tq = (TileQuarry) te;
		}
		return (tq != null
				? new UpdateClientQuarryPacket(tq.yDig, message.redstone, tq.active, tq.getPos().getX(),
						tq.getPos().getY(), tq.getPos().getZ())
				: null);
	}

}