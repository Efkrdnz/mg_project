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

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityInfinityMenu;
import net.mcreator.minefinitygauntlet.network.SelectAbilityInfinityButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SelectAbilityInfinityScreen extends AbstractContainerScreen<SelectAbilityInfinityMenu> {
	private final static HashMap<String, Object> guistate = SelectAbilityInfinityMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_1;
	Button button_2;
	Button button_3;
	Button button_4;
	Button button_5;
	Button button_6;
	Button button_reset_universe;

	public SelectAbilityInfinityScreen(SelectAbilityInfinityMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 168;
		this.imageHeight = 178;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof SelectAbilityInfinityScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/select_ability_infinity.png");

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
	}

	@Override
	public void init() {
		super.init();
		button_1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 13, 30, 20).build();
		guistate.put("button:button_1", button_1);
		this.addRenderableWidget(button_1);
		button_2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_2"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(1, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 1, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 36, 30, 20).build();
		guistate.put("button:button_2", button_2);
		this.addRenderableWidget(button_2);
		button_3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_3"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(2, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 2, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 59, 30, 20).build();
		guistate.put("button:button_3", button_3);
		this.addRenderableWidget(button_3);
		button_4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_4"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(3, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 3, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 83, 30, 20).build();
		guistate.put("button:button_4", button_4);
		this.addRenderableWidget(button_4);
		button_5 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_5"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(4, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 4, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 106, 30, 20).build();
		guistate.put("button:button_5", button_5);
		this.addRenderableWidget(button_5);
		button_6 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_6"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(5, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 5, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 129, 30, 20).build();
		guistate.put("button:button_6", button_6);
		this.addRenderableWidget(button_6);
		button_reset_universe = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_reset_universe"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(6, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 6, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 35, this.topPos + 11, 98, 20).build();
		guistate.put("button:button_reset_universe", button_reset_universe);
		this.addRenderableWidget(button_reset_universe);
	}
}
