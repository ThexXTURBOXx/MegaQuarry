package de.thexxturboxx.megaquarry.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateServerQuarryPacket implements IMessage {

	public int redstone, x, y, z;

	public UpdateServerQuarryPacket() {

	}

	public UpdateServerQuarryPacket(int redstone, int x, int y, int z) {
		this.redstone = redstone;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		redstone = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(redstone);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

}