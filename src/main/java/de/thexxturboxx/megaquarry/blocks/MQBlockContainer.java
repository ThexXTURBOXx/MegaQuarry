package de.thexxturboxx.megaquarry.blocks;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumBlockRenderType;

public abstract class MQBlockContainer extends BlockContainer {

	protected String name;

	public MQBlockContainer(Material mat, String name) {
		super(mat);
		this.name = name;
		setUnlocalizedName(name);
		setCreativeTab(MegaQuarryMod.tabMegaQuarry);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	public void registerItemModel(ItemBlock itemBlock) {
		MegaQuarryMod.proxy.registerItemRenderer(itemBlock, 0, name);
	}

}