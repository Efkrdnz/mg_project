package net.mcreator.minefinitygauntlet.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.SoulStorageMenu;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel9Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel8Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel7Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel6Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel5Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel4Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel3Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel2Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel1Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageLabel0Procedure;
import net.mcreator.minefinitygauntlet.network.SoulStorageButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SoulStorageScreen extends AbstractContainerScreen<SoulStorageMenu> {
	private final static HashMap<String, Object> guistate = SoulStorageMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_summon;
	Button button_summon1;
	Button button_summon2;
	Button button_summon3;
	Button button_summon4;
	Button button_summon5;
	Button button_summon6;
	Button button_summon7;
	Button button_summon8;
	Button button_summon9;
	Button button_empty;
	Button button_empty1;

	public SoulStorageScreen(SoulStorageMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 190;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof SoulStorageScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/soul_storage.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font,

				SoulStorageLabel0Procedure.execute(entity), 8, 6, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel1Procedure.execute(entity), 8, 25, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel2Procedure.execute(entity), 8, 43, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel3Procedure.execute(entity), 8, 62, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel4Procedure.execute(entity), 8, 81, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel5Procedure.execute(entity), 8, 100, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel6Procedure.execute(entity), 8, 119, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel7Procedure.execute(entity), 8, 138, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel8Procedure.execute(entity), 8, 157, -12829636, false);
		guiGraphics.drawString(this.font,

				SoulStorageLabel9Procedure.execute(entity), 8, 176, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_summon = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 0, 56, 20).build();
		guistate.put("button:button_summon", button_summon);
		this.addRenderableWidget(button_summon);
		button_summon1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(1, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 1, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 19, 56, 20).build();
		guistate.put("button:button_summon1", button_summon1);
		this.addRenderableWidget(button_summon1);
		button_summon2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon2"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(2, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 2, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 37, 56, 20).build();
		guistate.put("button:button_summon2", button_summon2);
		this.addRenderableWidget(button_summon2);
		button_summon3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon3"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(3, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 3, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 56, 56, 20).build();
		guistate.put("button:button_summon3", button_summon3);
		this.addRenderableWidget(button_summon3);
		button_summon4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon4"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(4, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 4, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 75, 56, 20).build();
		guistate.put("button:button_summon4", button_summon4);
		this.addRenderableWidget(button_summon4);
		button_summon5 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon5"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(5, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 5, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 94, 56, 20).build();
		guistate.put("button:button_summon5", button_summon5);
		this.addRenderableWidget(button_summon5);
		button_summon6 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon6"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(6, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 6, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 113, 56, 20).build();
		guistate.put("button:button_summon6", button_summon6);
		this.addRenderableWidget(button_summon6);
		button_summon7 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon7"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(7, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 7, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 132, 56, 20).build();
		guistate.put("button:button_summon7", button_summon7);
		this.addRenderableWidget(button_summon7);
		button_summon8 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon8"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(8, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 8, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 151, 56, 20).build();
		guistate.put("button:button_summon8", button_summon8);
		this.addRenderableWidget(button_summon8);
		button_summon9 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_summon9"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(9, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 9, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 144, this.topPos + 170, 56, 20).build();
		guistate.put("button:button_summon9", button_summon9);
		this.addRenderableWidget(button_summon9);
		button_empty = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_empty"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(10, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 10, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 199, this.topPos + 0, 30, 20).build();
		guistate.put("button:button_empty", button_empty);
		this.addRenderableWidget(button_empty);
		button_empty1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.soul_storage.button_empty1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SoulStorageButtonMessage(11, x, y, z, getEditBoxAndCheckBoxValues()));
				SoulStorageButtonMessage.handleButtonAction(entity, 11, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 199, this.topPos + 170, 30, 20).build();
		guistate.put("button:button_empty1", button_empty1);
		this.addRenderableWidget(button_empty1);
	}
}
