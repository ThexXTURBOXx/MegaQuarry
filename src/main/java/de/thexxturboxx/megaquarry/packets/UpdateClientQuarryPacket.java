package de.thexxturboxx.megaquarry.packets;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UpdateClientQuarryPacket implements IMessage {

	public int height, redstone, x, y, z;

	boolean active;
	public UpdateClientQuarryPacket() {

	}

	public UpdateClientQuarryPacket(int height, int redstone, boolean active, int x, int y, int z) {
		this.height = height;
		this.redstone = redstone;
		this.active = active;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		height = buf.readInt();
		redstone = buf.readInt();
		active = buf.readBoolean();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(height);
		buf.writeInt(redstone);
		buf.writeBoolean(active);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

}