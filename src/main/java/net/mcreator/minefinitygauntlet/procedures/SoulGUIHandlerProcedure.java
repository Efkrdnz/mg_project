package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class SoulGUIHandlerProcedure {
	private static int scrollOffset = 0;

	// resets scroll when gui opens
	public static void onOpen(Entity sourceEntity) {
		scrollOffset = 0;
	}

	// scroll up
	public static void scrollUp(Entity sourceEntity) {
		if (scrollOffset > 0) {
			scrollOffset--;
		}
	}

	// scroll down
	public static void scrollDown(Entity sourceEntity) {
		if (!(sourceEntity instanceof Player player))
			return;
		int total = (int) SoulCaptureSystemProcedure.getCapturedCount(player);
		int maxScroll = Math.max(0, total - 10);
		if (scrollOffset < maxScroll) {
			scrollOffset++;
		}
	}

	// gets name for slot
	public static String getSlotName(Entity sourceEntity, double slotNum) {
		int slot = (int) slotNum;
		int actualIndex = scrollOffset + slot;
		if (!(sourceEntity instanceof Player player))
			return "";
		int total = (int) SoulCaptureSystemProcedure.getCapturedCount(player);
		if (actualIndex >= total)
			return "";
		return SoulCaptureSystemProcedure.getEntityName(player, actualIndex);
	}

	// releases from slot
	public static void releaseSlot(Entity sourceEntity, double slotNum) {
		int slot = (int) slotNum;
		int actualIndex = scrollOffset + slot;
		SoulCaptureSystemProcedure.releaseAtIndex(sourceEntity, actualIndex);
	}

	public static double getScrollOffset() {
		return scrollOffset;
	}
}
