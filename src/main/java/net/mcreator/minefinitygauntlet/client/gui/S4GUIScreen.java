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

import net.mcreator.minefinitygauntlet.world.inventory.S4GUIMenu;
import net.mcreator.minefinitygauntlet.network.S4GUIButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class S4GUIScreen extends AbstractContainerScreen<S4GUIMenu> {
	private final static HashMap<String, Object> guistate = S4GUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	EditBox tppx;
	EditBox tppy;
	EditBox tppz;
	Button button_empty;

	public S4GUIScreen(S4GUIMenu container, Inventory inventory, Component text) {
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
		if (Minecraft.getInstance().screen instanceof S4GUIScreen sc) {
			textstate.put("textin:tppx", sc.tppx.getValue());
			textstate.put("textin:tppy", sc.tppy.getValue());
			textstate.put("textin:tppz", sc.tppz.getValue());

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/s_4_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		tppx.render(guiGraphics, mouseX, mouseY, partialTicks);
		tppy.render(guiGraphics, mouseX, mouseY, partialTicks);
		tppz.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (mouseX > leftPos + 32 && mouseX < leftPos + 56 && mouseY > topPos + 118 && mouseY < topPos + 142)
			guiGraphics.renderTooltip(font, Component.translatable("gui.minefinity_gauntlet.s_4_gui.tooltip_confirm_the_portal"), mouseX, mouseY);
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
		if (tppx.isFocused())
			return tppx.keyPressed(key, b, c);
		if (tppy.isFocused())
			return tppy.keyPressed(key, b, c);
		if (tppz.isFocused())
			return tppz.keyPressed(key, b, c);
		return super.keyPressed(key, b, c);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String tppxValue = tppx.getValue();
		String tppyValue = tppy.getValue();
		String tppzValue = tppz.getValue();
		super.resize(minecraft, width, height);
		tppx.setValue(tppxValue);
		tppy.setValue(tppyValue);
		tppz.setValue(tppzValue);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		tppx = new EditBox(this.font, this.leftPos + 7, this.topPos + 7, 72, 18, Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppx")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppx").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppx").getString());
				else
					setSuggestion(null);
			}
		};
		tppx.setMaxLength(32767);
		tppx.setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppx").getString());
		guistate.put("text:tppx", tppx);
		this.addWidget(this.tppx);
		tppy = new EditBox(this.font, this.leftPos + 7, this.topPos + 29, 72, 18, Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppy")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppy").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppy").getString());
				else
					setSuggestion(null);
			}
		};
		tppy.setMaxLength(32767);
		tppy.setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppy").getString());
		guistate.put("text:tppy", tppy);
		this.addWidget(this.tppy);
		tppz = new EditBox(this.font, this.leftPos + 7, this.topPos + 51, 72, 18, Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppz")) {
			@Override
			public void insertText(String text) {
				super.insertText(text);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppz").getString());
				else
					setSuggestion(null);
			}

			@Override
			public void moveCursorTo(int pos, boolean flag) {
				super.moveCursorTo(pos, flag);
				if (getValue().isEmpty())
					setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppz").getString());
				else
					setSuggestion(null);
			}
		};
		tppz.setMaxLength(32767);
		tppz.setSuggestion(Component.translatable("gui.minefinity_gauntlet.s_4_gui.tppz").getString());
		guistate.put("text:tppz", tppz);
		this.addWidget(this.tppz);
		button_empty = new PlainTextButton(this.leftPos + 31, this.topPos + 119, 25, 20, Component.translatable("gui.minefinity_gauntlet.s_4_gui.button_empty"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new S4GUIButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				S4GUIButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}, this.font);
		guistate.put("button:button_empty", button_empty);
		this.addRenderableWidget(button_empty);
	}
}
