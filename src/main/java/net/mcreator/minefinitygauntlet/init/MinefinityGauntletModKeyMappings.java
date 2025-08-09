
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.minefinitygauntlet.network.SelectStoneMessage;
import net.mcreator.minefinitygauntlet.network.SelectAbilityMessage;
import net.mcreator.minefinitygauntlet.network.QuickSlot6Message;
import net.mcreator.minefinitygauntlet.network.QuickSlot5Message;
import net.mcreator.minefinitygauntlet.network.QuickSlot4Message;
import net.mcreator.minefinitygauntlet.network.QuickSlot3Message;
import net.mcreator.minefinitygauntlet.network.QuickSlot2Message;
import net.mcreator.minefinitygauntlet.network.QuickSlot1Message;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class MinefinityGauntletModKeyMappings {
	public static final KeyMapping SELECT_STONE = new KeyMapping("key.minefinity_gauntlet.select_stone", GLFW.GLFW_KEY_R, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new SelectStoneMessage(0, 0));
				SelectStoneMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping SELECT_ABILITY = new KeyMapping("key.minefinity_gauntlet.select_ability", GLFW.GLFW_KEY_V, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new SelectAbilityMessage(0, 0));
				SelectAbilityMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_1 = new KeyMapping("key.minefinity_gauntlet.quick_slot_1", GLFW.GLFW_KEY_1, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot1Message(0, 0));
				QuickSlot1Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_2 = new KeyMapping("key.minefinity_gauntlet.quick_slot_2", GLFW.GLFW_KEY_2, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot2Message(0, 0));
				QuickSlot2Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_3 = new KeyMapping("key.minefinity_gauntlet.quick_slot_3", GLFW.GLFW_KEY_3, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot3Message(0, 0));
				QuickSlot3Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_4 = new KeyMapping("key.minefinity_gauntlet.quick_slot_4", GLFW.GLFW_KEY_4, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot4Message(0, 0));
				QuickSlot4Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_5 = new KeyMapping("key.minefinity_gauntlet.quick_slot_5", GLFW.GLFW_KEY_5, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot5Message(0, 0));
				QuickSlot5Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping QUICK_SLOT_6 = new KeyMapping("key.minefinity_gauntlet.quick_slot_6", GLFW.GLFW_KEY_6, "key.categories.minefinity_gauntlet") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new QuickSlot6Message(0, 0));
				QuickSlot6Message.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SELECT_STONE);
		event.register(SELECT_ABILITY);
		event.register(QUICK_SLOT_1);
		event.register(QUICK_SLOT_2);
		event.register(QUICK_SLOT_3);
		event.register(QUICK_SLOT_4);
		event.register(QUICK_SLOT_5);
		event.register(QUICK_SLOT_6);
	}

	@EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				SELECT_STONE.consumeClick();
				SELECT_ABILITY.consumeClick();
				QUICK_SLOT_1.consumeClick();
				QUICK_SLOT_2.consumeClick();
				QUICK_SLOT_3.consumeClick();
				QUICK_SLOT_4.consumeClick();
				QUICK_SLOT_5.consumeClick();
				QUICK_SLOT_6.consumeClick();
			}
		}
	}
}
