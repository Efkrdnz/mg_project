package net.mcreator.minefinitygauntlet.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.UniversalLawGuiMenu;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class UniversalLawGuiScreen extends AbstractContainerScreen<UniversalLawGuiMenu> {
	private final static HashMap<String, Object> guistate = UniversalLawGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_cre;
	Button button_empty;
	Button button_empty1;
	Button button_empty2;
	Button button_empty3;
	Button button_empty4;
	Button button_cre1;
	Button button_cre2;
	Button button_cre3;
	Button button_cre4;

	public UniversalLawGuiScreen(UniversalLawGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 0;
		this.imageHeight = 0;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof UniversalLawGuiScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/universal_law_gui.png");

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

		guiGraphics.blit(ResourceLocation.parse("minefinity_gauntlet:textures/screens/universal_law_background.png"), this.leftPos + -100, this.topPos + -91, 0, 0, 200, 182, 200, 182);

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
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_universal_law"), -34, -82, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_empty"), -16, -33, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_empty1"), -16, -6, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_empty2"), -16, 21, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_empty3"), -16, 47, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.universal_law_gui.label_empty4"), -16, 71, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_cre = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_cre"), e -> {
		}).bounds(this.leftPos + -95, this.topPos + -38, 40, 20).build();
		guistate.put("button:button_cre", button_cre);
		this.addRenderableWidget(button_cre);
		button_empty = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_empty"), e -> {
		}).bounds(this.leftPos + 70, this.topPos + -38, 25, 20).build();
		guistate.put("button:button_empty", button_empty);
		this.addRenderableWidget(button_empty);
		button_empty1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_empty1"), e -> {
		}).bounds(this.leftPos + 70, this.topPos + -11, 25, 20).build();
		guistate.put("button:button_empty1", button_empty1);
		this.addRenderableWidget(button_empty1);
		button_empty2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_empty2"), e -> {
		}).bounds(this.leftPos + 70, this.topPos + 16, 25, 20).build();
		guistate.put("button:button_empty2", button_empty2);
		this.addRenderableWidget(button_empty2);
		button_empty3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_empty3"), e -> {
		}).bounds(this.leftPos + 70, this.topPos + 42, 25, 20).build();
		guistate.put("button:button_empty3", button_empty3);
		this.addRenderableWidget(button_empty3);
		button_empty4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_empty4"), e -> {
		}).bounds(this.leftPos + 70, this.topPos + 67, 25, 20).build();
		guistate.put("button:button_empty4", button_empty4);
		this.addRenderableWidget(button_empty4);
		button_cre1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_cre1"), e -> {
		}).bounds(this.leftPos + -95, this.topPos + -11, 40, 20).build();
		guistate.put("button:button_cre1", button_cre1);
		this.addRenderableWidget(button_cre1);
		button_cre2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_cre2"), e -> {
		}).bounds(this.leftPos + -95, this.topPos + 16, 40, 20).build();
		guistate.put("button:button_cre2", button_cre2);
		this.addRenderableWidget(button_cre2);
		button_cre3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_cre3"), e -> {
		}).bounds(this.leftPos + -95, this.topPos + 42, 40, 20).build();
		guistate.put("button:button_cre3", button_cre3);
		this.addRenderableWidget(button_cre3);
		button_cre4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.universal_law_gui.button_cre4"), e -> {
		}).bounds(this.leftPos + -95, this.topPos + 67, 40, 20).build();
		guistate.put("button:button_cre4", button_cre4);
		this.addRenderableWidget(button_cre4);
	}
}
