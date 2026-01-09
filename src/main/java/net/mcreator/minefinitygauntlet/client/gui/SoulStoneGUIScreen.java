package net.mcreator.minefinitygauntlet.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.world.inventory.SoulStoneGUIMenu;
import net.mcreator.minefinitygauntlet.network.SoulStoneGUIButtonMessage;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

public class SoulStoneGUIScreen extends AbstractContainerScreen<SoulStoneGUIMenu> {
	private final static HashMap<String, Object> guistate = SoulStoneGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button btnScrollUp;
	Button btnScrollDown;
	Button[] releaseButtons = new Button[10];
	private List<String> entityNames = new ArrayList<>();
	private int scrollOffset = 0;

	public SoulStoneGUIScreen(SoulStoneGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 200;
		this.imageHeight = 180;
	}

	public static HashMap<String, String> getEditBoxAndCheckBoxValues() {
		HashMap<String, String> textstate = new HashMap<>();
		if (Minecraft.getInstance().screen instanceof SoulStoneGUIScreen sc) {
		}
		return textstate;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("minefinity_gauntlet:textures/screens/soul_stone_gui.png");

	private void updateEntityList() {
		entityNames.clear();
		// read from client-side player data
		CompoundTag playerData = entity.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		for (int i = 0; i < capturedList.size(); i++) {
			CompoundTag entityData = capturedList.getCompound(i);
			String entityId = entityData.getString("id");
			if (!entityId.isEmpty()) {
				try {
					EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entityId));
					if (type != null) {
						entityNames.add(type.getDescription().getString());
					} else {
						entityNames.add("Unknown");
					}
				} catch (Exception e) {
					entityNames.add("Unknown");
				}
			} else {
				entityNames.add("Unknown");
			}
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		// update entity list every frame to catch changes
		updateEntityList();
		// render entity names
		for (int i = 0; i < 10; i++) {
			int actualIndex = scrollOffset + i;
			if (actualIndex < entityNames.size()) {
				String entityName = entityNames.get(actualIndex);
				guiGraphics.drawString(this.font, entityName, this.leftPos + 10, this.topPos + 20 + (i * 15), -1, false);
			}
		}
		// render page info
		int total = entityNames.size();
		if (total > 0) {
			int showing = Math.min(10, total - scrollOffset);
			String pageInfo = (scrollOffset + 1) + "-" + (scrollOffset + showing) + "/" + total;
			guiGraphics.drawString(this.font, pageInfo, this.leftPos + 165, this.topPos + 85, -12829636, false);
		} else {
			guiGraphics.drawString(this.font, "No entities", this.leftPos + 160, this.topPos + 85, -12829636, false);
		}
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
		guiGraphics.drawString(this.font, Component.translatable("gui.minefinity_gauntlet.soul_stone_gui.label_captured_entities"), 55, 6, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		scrollOffset = 0;
		updateEntityList();
		// scroll up button (ID 10)
		btnScrollUp = Button.builder(Component.literal("↑"), e -> {
			if (scrollOffset > 0) {
				scrollOffset--;
			}
		}).bounds(this.leftPos + 175, this.topPos + 20, 15, 15).build();
		guistate.put("button:btnScrollUp", btnScrollUp);
		this.addRenderableWidget(btnScrollUp);
		// scroll down button (ID 11)
		btnScrollDown = Button.builder(Component.literal("↓"), e -> {
			int maxScroll = Math.max(0, entityNames.size() - 10);
			if (scrollOffset < maxScroll) {
				scrollOffset++;
			}
		}).bounds(this.leftPos + 175, this.topPos + 155, 15, 15).build();
		guistate.put("button:btnScrollDown", btnScrollDown);
		this.addRenderableWidget(btnScrollDown);
		// release buttons (IDs 0-9)
		for (int i = 0; i < 10; i++) {
			final int slotIndex = i;
			releaseButtons[i] = Button.builder(Component.literal("Release"), e -> {
				if (this.minecraft != null && this.minecraft.player != null) {
					int actualIndex = scrollOffset + slotIndex;
					PacketDistributor.sendToServer(new SoulStoneGUIButtonMessage(actualIndex, x, y, z, getEditBoxAndCheckBoxValues()));
					SoulStoneGUIButtonMessage.handleButtonAction(entity, actualIndex, x, y, z, getEditBoxAndCheckBoxValues());
				}
			}).bounds(this.leftPos + 130, this.topPos + 18 + (i * 15), 50, 12).build();
			guistate.put("button:release_" + i, releaseButtons[i]);
			this.addRenderableWidget(releaseButtons[i]);
		}
	}
}
