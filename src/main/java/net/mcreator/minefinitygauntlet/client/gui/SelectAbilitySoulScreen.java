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

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilitySoulMenu;
import net.mcreator.minefinitygauntlet.network.SelectAbilitySoulButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SelectAbilitySoulScreen extends AbstractContainerScreen<SelectAbilitySoulMenu> {
	private final static HashMap<String, Object> guistate = SelectAbilitySoulMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_1;
	Button button_2;
	Button button_3;
	Button button_4;
	Button button_5;
	Button button_6;
	Button button_barri;
	Button button_vampiric_b;
	Button button_soul_s;
	Button button_soul_capture;
	Button button_soul_storage;
	ImageButton imagebutton_button_powerstone;
	ImageButton imagebutton_button_realitystone;
	ImageButton imagebutton_button_timestone;
	ImageButton imagebutton_button_mindstone;
	ImageButton imagebutton_button_soulstone;

	public SelectAbilitySoulScreen(SelectAbilitySoulMenu container, Inventory inventory, Component text) {
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
		if (Minecraft.getInstance().screen instanceof SelectAbilitySoulScreen sc) {

		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/select_ability_soul.png");

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
		button_1 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_1"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(0, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 0, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 13, 30, 20).build();
		guistate.put("button:button_1", button_1);
		this.addRenderableWidget(button_1);
		button_2 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_2"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(1, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 1, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 36, 30, 20).build();
		guistate.put("button:button_2", button_2);
		this.addRenderableWidget(button_2);
		button_3 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_3"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(2, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 2, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 59, 30, 20).build();
		guistate.put("button:button_3", button_3);
		this.addRenderableWidget(button_3);
		button_4 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_4"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(3, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 3, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 83, 30, 20).build();
		guistate.put("button:button_4", button_4);
		this.addRenderableWidget(button_4);
		button_5 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_5"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(4, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 4, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 106, 30, 20).build();
		guistate.put("button:button_5", button_5);
		this.addRenderableWidget(button_5);
		button_6 = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_6"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(5, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 5, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + -32, this.topPos + 129, 30, 20).build();
		guistate.put("button:button_6", button_6);
		this.addRenderableWidget(button_6);
		button_barri = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_barri"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(6, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 6, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 9, this.topPos + 40, 51, 20).build();
		guistate.put("button:button_barri", button_barri);
		this.addRenderableWidget(button_barri);
		button_vampiric_b = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_vampiric_b"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(7, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 7, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 77, this.topPos + 40, 77, 20).build();
		guistate.put("button:button_vampiric_b", button_vampiric_b);
		this.addRenderableWidget(button_vampiric_b);
		button_soul_s = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_soul_s"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(8, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 8, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 9, this.topPos + 67, 56, 20).build();
		guistate.put("button:button_soul_s", button_soul_s);
		this.addRenderableWidget(button_soul_s);
		button_soul_capture = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_soul_capture"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(9, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 9, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 71, this.topPos + 67, 87, 20).build();
		guistate.put("button:button_soul_capture", button_soul_capture);
		this.addRenderableWidget(button_soul_capture);
		button_soul_storage = Button.builder(Component.translatable("gui.minefinity_gauntlet.select_ability_soul.button_soul_storage"), e -> {
			if (true) {
				PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(10, x, y, z, getEditBoxAndCheckBoxValues()));
				SelectAbilitySoulButtonMessage.handleButtonAction(entity, 10, x, y, z, getEditBoxAndCheckBoxValues());
			}
		}).bounds(this.leftPos + 9, this.topPos + 93, 87, 20).build();
		guistate.put("button:button_soul_storage", button_soul_storage);
		this.addRenderableWidget(button_soul_storage);
		imagebutton_button_powerstone = new ImageButton(this.leftPos + 5, this.topPos + 5, 30, 30,
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_powerstone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_powerstone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(11, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySoulButtonMessage.handleButtonAction(entity, 11, x, y, z, getEditBoxAndCheckBoxValues());
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
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_space.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_space.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(12, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySoulButtonMessage.handleButtonAction(entity, 12, x, y, z, getEditBoxAndCheckBoxValues());
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
						PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(13, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySoulButtonMessage.handleButtonAction(entity, 13, x, y, z, getEditBoxAndCheckBoxValues());
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
						PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(14, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySoulButtonMessage.handleButtonAction(entity, 14, x, y, z, getEditBoxAndCheckBoxValues());
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
				new WidgetSprites(ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png"), ResourceLocation.parse("minefinity_gauntlet:textures/screens/button_realitystone.png")), e -> {
					if (true) {
						PacketDistributor.sendToServer(new SelectAbilitySoulButtonMessage(15, x, y, z, getEditBoxAndCheckBoxValues()));
						SelectAbilitySoulButtonMessage.handleButtonAction(entity, 15, x, y, z, getEditBoxAndCheckBoxValues());
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
