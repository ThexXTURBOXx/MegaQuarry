package de.thexxturboxx.megaquarry.blocks;

import java.util.List;

import javax.annotation.Nullable;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import de.thexxturboxx.megaquarry.packets.UpdateClientQuarryPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class TileQuarry extends TileEntityLockableLoot implements ITickable, IInventory {

	public static ItemStack fillItemsToInv(ItemStack stack, IInventory inv) {
		if (!stack.isEmpty()) {
			int i = 0;
			int f = stack.getCount();
			while (i < inv.getSizeInventory()) {
				if (inv.getStackInSlot(i).isEmpty() || inv.getStackInSlot(i).isItemEqual(stack)) {
					int c;
					int u;
					if (inv.getStackInSlot(i).isEmpty()) {
						c = stack.getMaxStackSize();
						u = 0;
					} else {
						c = inv.getStackInSlot(i).getMaxStackSize();
						u = inv.getStackInSlot(i).getCount();
					}
					int z = c - u;
					if (f - z < 0) {
						z = f;
					}
					ItemStack is = stack.copy();
					is.setCount(u + z);
					inv.setInventorySlotContents(i, is);
					f = f - z;
					if (f == 0) {
						return null;
					}
				}
				i++;
			}
			ItemStack is = stack.copy();
			is.setCount(f);
			return is;
		}
		return stack;
	}

	public int xDig, yDig, zDig;

	/**
	 * 0: On without Signal 1: On with Signal 2: Always on 3: Always off
	 */
	public int redstoneControl;

	public boolean active;

	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(54, ItemStack.EMPTY);

	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;

	/** The angle of the lid last tick */
	public float prevLidAngle;

	/** The number of players currently using this chest */
	public int numPlayersUsing;

	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;

	public TileQuarry() {
		this(100, 0);
	}

	public TileQuarry(int maxReceive, int maxExtract) {
		super();
		xDig = 0;
		yDig = -10;
		zDig = 0;
		redstoneControl = 0;
		active = false;
	}

	@Override
	public void clear() {
		this.fillWithLoot((EntityPlayer) null);

		for (int i = 0; i < this.chestContents.size(); ++i) {
			this.chestContents.set(i, ItemStack.EMPTY);
		}
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		this.fillWithLoot(playerIn);
		return new ContainerChest(playerInventory, this, playerIn);
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public String getGuiID() {
		return "minecraft:chest";
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be 64,
	 * possibly will be extended.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return chestContents;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return "Mega Quarry";
	}

	public String getQuarryName() {
		return world.getBlockState(getPos()).getBlock().getLocalizedName();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return chestContents.size();
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	@Nullable
	public ItemStack getStackInSlot(int index) {
		this.fillWithLoot((EntityPlayer) null);
		return chestContents.get(index);
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return true;
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot. For guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) != this ? false
				: player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}

			++this.numPlayersUsing;
			this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
			this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), true);
		}
	}

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
					getWorld().setBlockToAir(new BlockPos(chunkX * 16 + xDig, yDig, chunkZ * 16 + zDig));
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
			world.scheduleBlockUpdate(pos, world.getBlockState(pos).getBlock(), 0, 0);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		chestContents = NonNullList.<ItemStack>withSize(54, ItemStack.EMPTY);

		if (!this.checkLootAndRead(compound)) {
			NBTTagList nbttaglist = compound.getTagList("Items", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound.getByte("Slot") & 255;

				if (j >= 0 && j < this.chestContents.size()) {
					this.chestContents.set(j, new ItemStack(nbttagcompound));
				}
			}
		}
		if (compound.hasKey("xDig")) {
			xDig = compound.getInteger("xDig");
		} else {
			xDig = 0;
		}
		if (compound.hasKey("yDig")) {
			yDig = compound.getInteger("yDig");
		} else {
			yDig = getPos().getY();
		}
		if (compound.hasKey("zDig")) {
			zDig = compound.getInteger("zDig");
		} else {
			zDig = 0;
		}
		if (compound.hasKey("active")) {
			active = compound.getBoolean("active");
		} else {
			active = true;
		}
		if (compound.hasKey("redstoneCtrl")) {
			redstoneControl = compound.getInteger("redstoneCtrl");
		} else {
			redstoneControl = 0;
		}
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			this.numPlayersUsing = type;
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

	@Override
	public void setField(int id, int value) {
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		this.fillWithLoot((EntityPlayer) null);
		this.chestContents.set(index, stack);
		if (stack != null && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
		this.markDirty();
	}

	@Override
	public void setPos(BlockPos posIn) {
		super.setPos(posIn);
		if (yDig == -10) {
			yDig = posIn.getY();
			active = true;
		}
	}

	@Override
	public void update() {
		++this.ticksSinceSync;
		this.prevLidAngle = this.lidAngle;
		if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
			if (this.numPlayersUsing > 0) {
				this.lidAngle += 0.1F;
			} else {
				this.lidAngle -= 0.1F;
			}
			if (this.lidAngle > 1.0F) {
				this.lidAngle = 1.0F;
			}
			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
		quarryPart();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (!this.checkLootAndWrite(compound)) {
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.chestContents.size(); ++i) {
				if (this.chestContents.get(i) != null) {
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte) i);
					this.chestContents.get(i).writeToNBT(nbttagcompound);
					nbttaglist.appendTag(nbttagcompound);
				}
			}

			compound.setTag("Items", nbttaglist);
		}
		compound.setInteger("xDig", xDig);
		compound.setInteger("yDig", yDig);
		compound.setInteger("zDig", zDig);
		compound.setBoolean("active", active);
		compound.setInteger("redstoneCtrl", redstoneControl);
		return compound;
	}

}