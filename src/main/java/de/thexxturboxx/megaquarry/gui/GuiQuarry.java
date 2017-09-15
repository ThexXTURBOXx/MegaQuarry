package de.thexxturboxx.megaquarry.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import de.thexxturboxx.megaquarry.MegaQuarryMod;
import de.thexxturboxx.megaquarry.blocks.TileQuarry;
import de.thexxturboxx.megaquarry.packets.UpdateServerQuarryPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiQuarry extends GuiContainer {

	static final double scale = 0.75d;
	static final double ascale = 1d / scale;
	private IInventory playerInv;
	private TileQuarry te;

	public GuiQuarry(IInventory playerInv, TileQuarry te, EntityPlayer p) {
		super(new ContainerChest(playerInv, te, p));
		this.playerInv = playerInv;
		this.te = te;
		MegaQuarryMod.PACKETWRAPPER.sendToServer(new UpdateServerQuarryPacket(te.redstoneControl,
				te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
		xSize = 256;
		ySize = 222;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			MegaQuarryMod.PACKETWRAPPER.sendToServer(new UpdateServerQuarryPacket(getNextState(te.redstoneControl),
					te.getPos().getX(), te.getPos().getY(), te.getPos().getZ()));
			buttonList.get(0).displayString = getRedstoneString(te.redstoneControl);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(new ResourceLocation("megaquarry:textures/gui/quarryinv.png"));
		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		TileQuarry te = (TileQuarry) this.te.getWorld().getTileEntity(this.te.getPos());
		fontRenderer.drawString(te.getQuarryName(), 8, 6, 4210752);
		fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 128, 4210752);
		GL11.glScaled(scale, scale, scale);
		fontRenderer.drawString("Finished: " + (te.active ? "\u2718" : "\u2714"), (int) (ascale * 180), (int) (ascale * 41),
				4210752);
		fontRenderer.drawString("Mining Level: " + te.yDig, (int) (ascale * 180), (int) (ascale * 49), 4210752);
		GL11.glScaled(ascale, ascale, ascale);
		buttonList.get(0).displayString = getRedstoneString(te.redstoneControl);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public int getNextState(int ctrl) {
		return (ctrl < 3 ? ctrl + 1 : 0);
	}

	public String getRedstoneString(int ctrl) {
		switch (ctrl) {
		case 0:
			return "On if: No Signal";
		case 1:
			return "On if: Signal";
		case 2:
			return "On if: Always";
		case 3:
			return "On if: Never";
		default:
			return "Illegal State";
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		addButton(new GuiButton(0, guiLeft + 180, guiTop + 57, 80, 20, ""));
	}

}