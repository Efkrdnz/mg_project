package net.mcreator.minefinitygauntlet.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.FindTpGUIMenu;
import net.mcreator.minefinitygauntlet.network.FindTpGUIButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class FindTpGUIScreen extends AbstractContainerScreen<FindTpGUIMenu> {
	private final static HashMap<String, Object> guistate = FindTpGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox rad;
	EditBox type;
	EditBox dur;
	Button button_empty;

	public FindTpGUIScreen(FindTpGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 86;
		this.imageHeight = 152;
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof FindTpGUIScreen sc) {
			textstate.put("textin:rad", sc.rad.getValue());
			textstate.put("textin:type", sc.type.getValue());
			textstate.put("textin:dur", sc.dur.getValue());

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/find_tp_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		rad.render(guiGraphics, mouseX, mouseY, partialTicks);
		type.render(guiGraphics, mouseX, mouseY, partialTicks);
		dur.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (mouseX > leftPos + 32 && mouseX < leftPos + 56 && mouseY > topPos + 118 && mouseY < topPos + 142)
			guiGraphics.renderTooltip(font, Component.translatable("gui.minefinity_gauntlet.find_tp_gui.tooltip_confirm_the_portal"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(ResourceLocation.parse("minefinity_gauntlet:textures/screens/gui_gate.png"), this.leftPos + 31, this.topPos + 117, 0, 0, 25, 25, 25, 25);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		if (rad.isFocused())
			return rad.keyPressed(key, b, c);
		if (type.isFocused())
			return type.keyPressed(key, b, c);
		if (dur.isFocused())
			return dur.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String radValue = rad.getValue();
		String typeValue = type.getValue();
		String durValue = dur.getValue();
		super.resize(minecraft, width, height);
		rad.setValue(radValue);
		type.setValue(typeValue);
		dur.setValue(durValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		rad = new EditBox(this.font, this.leftPos + 7, this.topPos + 7, 72, 18, Component.translatable("gui.minefinity_gauntlet.find_tp_gui.rad")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.rad").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.rad").getString());
				else
					setSuggestion(null);
			}
		};
		rad.setMaxLength(32767);
		rad.setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.rad").getString());
		guistate.put("text:rad", rad);
		this.addWidget(this.rad);
		type = new EditBox(this.font, this.leftPos + 7, this.topPos + 29, 72, 18, Component.translatable("gui.minefinity_gauntlet.find_tp_gui.type")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.type").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.type").getString());
				else
					setSuggestion(null);
			}
		};
		type.setMaxLength(32767);
		type.setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.type").getString());
		guistate.put("text:type", type);
		this.addWidget(this.type);
		dur = new EditBox(this.font, this.leftPos + 7, this.topPos + 51, 72, 18, Component.translatable("gui.minefinity_gauntlet.find_tp_gui.dur")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.dur").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.dur").getString());
				else
					setSuggestion(null);
			}
		};
		dur.setMaxLength(32767);
		dur.setSuggestion(Component.translatable("gui.minefinity_gauntlet.find_tp_gui.dur").getString());
		guistate.put("text:dur", dur);
		this.addWidget(this.dur);
		button_empty = new PlainTextButton(this.leftPos + 31, this.topPos + 119, 25, 20, Component.translatable("gui.minefinity_gauntlet.find_tp_gui.button_empty"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new FindTpGUIButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				FindTpGUIButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}, this.font);
		guistate.put("button:button_empty", button_empty);
		this.addRenderableWidget(button_empty);
	}
}
