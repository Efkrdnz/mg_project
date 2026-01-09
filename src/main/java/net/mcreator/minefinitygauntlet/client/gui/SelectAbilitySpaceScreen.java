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

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilitySpaceMenu;
import net.mcreator.minefinitygauntlet.network.SelectAbilitySpaceButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SelectAbilitySpaceScreen extends AbstractContainerScreen<SelectAbilitySpaceMenu> {
	private final static HashMap<String, Object> guistate = SelectAbilitySpaceMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_warp;
	Button button_anchor_set;
	Button button_tp;
	Button button_detailed_teleport;
	Button button_block_telekinesis;
	Button button_1;
	Button button_2;
	Button button_3;
	Button button_4;
	Button button_5;
	Button button_6;
	Button button_vacuum_portal;
	Button button_singularity;
	Button button_normalize_space;
	ImageButton imagebutton_button_powerstone;
	ImageButton imagebutton_button_realitystone;
	ImageButton imagebutton_button_timestone;
	ImageButton imagebutton_button_mindstone;
	ImageButton imagebutton_button_soulstone;

	public SelectAbilitySpaceScreen(SelectAbilitySpaceMenu container, Inventory inventory, Component text) {
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
		if (Minecraft.getInstance().screen instanceof SelectAbilitySpaceScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/select_ability_space.png");

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
		button_warp = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_warp"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 37, 46, 20).build();
		guistate.put("button:button_warp", button_warp);
		this.addRenderableWidget(button_warp);
		button_anchor_set = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_anchor_set"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(1, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 1, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 52, this.topPos + 37, 77, 20).build();
		guistate.put("button:button_anchor_set", button_anchor_set);
		this.addRenderableWidget(button_anchor_set);
		button_tp = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_tp"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(2, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 2, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 130, this.topPos + 37, 35, 20).build();
		guistate.put("button:button_tp", button_tp);
		this.addRenderableWidget(button_tp);
		button_detailed_teleport = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_detailed_teleport"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(3, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 3, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 60, 114, 20).build();
		guistate.put("button:button_detailed_teleport", button_detailed_teleport);
		this.addRenderableWidget(button_detailed_teleport);
		button_block_telekinesis = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_block_telekinesis"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(4, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 4, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 83, 114, 20).build();
		guistate.put("button:button_block_telekinesis", button_block_telekinesis);
		this.addRenderableWidget(button_block_telekinesis);
		button_1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(5, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 5, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 13, 30, 20).build();
		guistate.put("button:button_1", button_1);
		this.addRenderableWidget(button_1);
		button_2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_2"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(6, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 6, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 36, 30, 20).build();
		guistate.put("button:button_2", button_2);
		this.addRenderableWidget(button_2);
		button_3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_3"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(7, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 7, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 59, 30, 20).build();
		guistate.put("button:button_3", button_3);
		this.addRenderableWidget(button_3);
		button_4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_4"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(8, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 8, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 83, 30, 20).build();
		guistate.put("button:button_4", button_4);
		this.addRenderableWidget(button_4);
		button_5 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_5"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(9, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 9, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 106, 30, 20).build();
		guistate.put("button:button_5", button_5);
		this.addRenderableWidget(button_5);
		button_6 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_6"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(10, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 10, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 129, 30, 20).build();
		guistate.put("button:button_6", button_6);
		this.addRenderableWidget(button_6);
		button_vacuum_portal = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_vacuum_portal"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(11, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 11, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 106, 93, 20).build();
		guistate.put("button:button_vacuum_portal", button_vacuum_portal);
		this.addRenderableWidget(button_vacuum_portal);
		button_singularity = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_singularity"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(12, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 12, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 129, 82, 20).build();
		guistate.put("button:button_singularity", button_singularity);
		this.addRenderableWidget(button_singularity);
		button_normalize_space = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_space.button_normalize_space"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(13, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 13, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 5, this.topPos + 152, 103, 20).build();
		guistate.put("button:button_normalize_space", button_normalize_space);
		this.addRenderableWidget(button_normalize_space);
		imagebutton_button_powerstone = new ImageButton(this.leftPos + 5, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_powerstone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_powerstone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(14, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 14, x, y, z, getEditBoxAndCheckBoxValues());
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_button_powerstone", imagebutton_button_powerstone);
		this.addRenderableWidget(imagebutton_button_powerstone);
		imagebutton_button_realitystone = new ImageButton(this.leftPos + 37, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(15, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 15, x, y, z, getEditBoxAndCheckBoxValues());
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
						PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(16, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 16, x, y, z, getEditBoxAndCheckBoxValues());
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
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(17, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 17, x, y, z, getEditBoxAndCheckBoxValues());
					}
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
						PacketDistributor.sendToServer(new SelectAbilitySpaceButtonMessage(18, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySpaceButtonMessage.handleButtonAction(entity, 18, x, y, z, getEditBoxAndCheckBoxValues());
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
