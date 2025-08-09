package net.mcreator.minefinitygauntlet.client.gui;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.LawCreationGuiMenu;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class LawCreationGuiScreen extends AbstractContainerScreen<LawCreationGuiMenu> {
	private final static HashMap<String, Object> guistate = LawCreationGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox lawNameField;

	public LawCreationGuiScreen(LawCreationGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 230;
		this.imageHeight = 200;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof LawCreationGuiScreen sc) {
			textstate.put("textin:lawNameField", sc.lawNameField.getValue());

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/law_creation_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		lawNameField.render(guiGraphics, mouseX, mouseY, partialTicks);
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
		if (lawNameField.isFocused())
			return lawNameField.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String lawNameFieldValue = lawNameField.getValue();
		super.resize(minecraft, width, height);
		lawNameField.setValue(lawNameFieldValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.law_creation_gui.label_law_creation"), 80, 6, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		lawNameField = new EditBox(this.font, this.leftPos + 13, this.topPos + 25, 118, 18, Component.translatable("gui.minefinity_gauntlet.law_creation_gui.lawNameField")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.law_creation_gui.lawNameField").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.law_creation_gui.lawNameField").getString());
				else
					setSuggestion(null);
			}
		};
		lawNameField.setMaxLength(32767);
		lawNameField.setSuggestion(Component.translatable("gui.minefinity_gauntlet.law_creation_gui.lawNameField").getString());
		guistate.put("text:lawNameField", lawNameField);
		this.addWidget(this.lawNameField);
	}
}
