package net.mcreator.minefinitygauntlet.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityPowerMenu;
import net.mcreator.minefinitygauntlet.network.SelectAbilityPowerButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SelectAbilityPowerScreen extends AbstractContainerScreen<SelectAbilityPowerMenu> {
	private final static HashMap<String, Object> guistate = SelectAbilityPowerMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_powerpunch;
	Button button_launch;
	Button button_powerbeam;
	Button button_1;
	Button button_2;
	Button button_3;
	Button button_4;
	Button button_5;
	Button button_6;
	ImageButton imagebutton_button_space;
	ImageButton imagebutton_button_realitystone;
	ImageButton imagebutton_button_timestone;
	ImageButton imagebutton_button_mindstone;
	ImageButton imagebutton_button_soulstone;

	public SelectAbilityPowerScreen(SelectAbilityPowerMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 168;
		this.imageHeight = 178;
	}

	@Override
	public boolean isPauseScreen() {
		return true;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof SelectAbilityPowerScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/select_ability_power.png");

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
		button_powerpunch = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_powerpunch"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 7, this.topPos + 37, 77, 20).build();
		guistate.put("button:button_powerpunch", button_powerpunch);
		this.addRenderableWidget(button_powerpunch);
		button_launch = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_launch"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(1, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 1, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 100, this.topPos + 37, 56, 20).build();
		guistate.put("button:button_launch", button_launch);
		this.addRenderableWidget(button_launch);
		button_powerbeam = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_powerbeam"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(2, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 2, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 7, this.topPos + 65, 72, 20).build();
		guistate.put("button:button_powerbeam", button_powerbeam);
		this.addRenderableWidget(button_powerbeam);
		button_1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(3, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 3, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 10, 30, 20).build();
		guistate.put("button:button_1", button_1);
		this.addRenderableWidget(button_1);
		button_2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_2"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(4, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 4, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 34, 30, 20).build();
		guistate.put("button:button_2", button_2);
		this.addRenderableWidget(button_2);
		button_3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_3"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(5, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 5, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 58, 30, 20).build();
		guistate.put("button:button_3", button_3);
		this.addRenderableWidget(button_3);
		button_4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_4"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(6, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 6, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 82, 30, 20).build();
		guistate.put("button:button_4", button_4);
		this.addRenderableWidget(button_4);
		button_5 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_5"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(7, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 7, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 106, 30, 20).build();
		guistate.put("button:button_5", button_5);
		this.addRenderableWidget(button_5);
		button_6 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_power.button_6"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(8, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilityPowerButtonMessage.handleButtonAction(entity, 8, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 129, 30, 20).build();
		guistate.put("button:button_6", button_6);
		this.addRenderableWidget(button_6);
		imagebutton_button_space = new ImageButton(this.leftPos + 5, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_space.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_space.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(9, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilityPowerButtonMessage.handleButtonAction(entity, 9, x, y, z, getEditBoxAndCheckBoxValues());
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_space", imagebutton_button_space);
		this.addRenderableWidget(imagebutton_button_space);
		imagebutton_button_realitystone = new ImageButton(this.leftPos + 37, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(10, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilityPowerButtonMessage.handleButtonAction(entity, 10, x, y, z, getEditBoxAndCheckBoxValues());
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_realitystone", imagebutton_button_realitystone);
		this.addRenderableWidget(imagebutton_button_realitystone);
		imagebutton_button_timestone = new ImageButton(this.leftPos + 69, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_timestone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_timestone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(11, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilityPowerButtonMessage.handleButtonAction(entity, 11, x, y, z, getEditBoxAndCheckBoxValues());
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_timestone", imagebutton_button_timestone);
		this.addRenderableWidget(imagebutton_button_timestone);
		imagebutton_button_mindstone = new ImageButton(this.leftPos + 101, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_mindstone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_mindstone.png")), e -> {
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_mindstone", imagebutton_button_mindstone);
		this.addRenderableWidget(imagebutton_button_mindstone);
		imagebutton_button_soulstone = new ImageButton(this.leftPos + 133, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_soulstone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_soulstone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilityPowerButtonMessage(13, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilityPowerButtonMessage.handleButtonAction(entity, 13, x, y, z, getEditBoxAndCheckBoxValues());
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_soulstone", imagebutton_button_soulstone);
		this.addRenderableWidget(imagebutton_button_soulstone);
	}
}
