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
	Button button_snap;
	Button button_timeline_erasu;
	Button button_infinity_b;
	Button button_universal;

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
		button_snap = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_snap"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityInfinityButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityInfinityButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 8, 46, 20).build();
		guistate.put("button:button_snap", button_snap);
		this.addRenderableWidget(button_snap);
		button_timeline_erasu = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_timeline_erasu"), e -> {
		}).bounds(this.leftPos + 63, this.topPos + 8, 98, 20).build();
		guistate.put("button:button_timeline_erasu", button_timeline_erasu);
		this.addRenderableWidget(button_timeline_erasu);
		button_infinity_b = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_infinity_b"), e -> {
		}).bounds(this.leftPos + 5, this.topPos + 32, 77, 20).build();
		guistate.put("button:button_infinity_b", button_infinity_b);
		this.addRenderableWidget(button_infinity_b);
		button_universal = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_infinity.button_universal"), e -> {
		}).bounds(this.leftPos + 84, this.topPos + 32, 77, 20).build();
		guistate.put("button:button_universal", button_universal);
		this.addRenderableWidget(button_universal);
	}
}
