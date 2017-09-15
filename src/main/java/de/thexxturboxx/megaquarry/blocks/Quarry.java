package de.thexxturboxx.megaquarry.blocks;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import de.thexxturboxx.megaquarry.gui.MegaQuarryGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;

public class Quarry extends MQBlockContainer {

	public Quarry() {
		this("blockQuarry");
	}

	public Quarry(String name) {
		super(Material.IRON, name);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof IInventory) {
			InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
			world.updateComparatorOutputLevel(pos, this);
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileQuarry();
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return createNewTileEntity(world, 0);
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		tileentity.setPos(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileQuarry) {
			ILockableContainer ilockablecontainer = (TileQuarry) worldIn.getTileEntity(pos);
			if (ilockablecontainer != null) {
				playerIn.openGui(MegaQuarryMod.instance, MegaQuarryGuiHandler.QUARRY_GUI_ID, worldIn, pos.getX(),
						pos.getY(), pos.getZ());
			}
		}
		return true;
	}

}