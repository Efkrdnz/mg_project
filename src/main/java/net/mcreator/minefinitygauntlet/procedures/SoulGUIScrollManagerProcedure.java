package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class SoulGUIScrollManagerProcedure {
	// gets current scroll offset
	public static double getScrollOffset(Entity entity) {
		if (!(entity instanceof Player player))
			return 0;
		return player.getPersistentData().getDouble("SoulGUIScrollOffset");
	}

	// sets scroll offset
	public static void setScrollOffset(Entity entity, double offset) {
		if (!(entity instanceof Player player))
			return;
		// clamp to valid range
		int total = (int) SoulCaptureSystemProcedure.getCapturedCount(player);
		int maxScroll = Math.max(0, total - 10);
		offset = Math.max(0, Math.min(offset, maxScroll));
		player.getPersistentData().putDouble("SoulGUIScrollOffset", offset);
	}

	// scroll up
	public static void scrollUp(Entity entity) {
		double current = getScrollOffset(entity);
		setScrollOffset(entity, current - 1);
	}

	// scroll down
	public static void scrollDown(Entity entity) {
		double current = getScrollOffset(entity);
		setScrollOffset(entity, current + 1);
	}

	// reset on open
	public static void resetScroll(Entity entity) {
		if (!(entity instanceof Player player))
			return;
		player.getPersistentData().putDouble("SoulGUIScrollOffset", 0);
	}

	// get entity name at slot (with scroll offset applied)
	public static String getEntityNameAtSlot(Entity entity, double slotNum) {
		int slot = (int) slotNum;
		int offset = (int) getScrollOffset(entity);
		int actualIndex = offset + slot;
		return SoulCaptureSystemProcedure.getEntityName(entity, actualIndex);
	}

	// release entity at slot (with scroll offset applied)
	public static void releaseAtSlot(Entity entity, double slotNum) {
		int slot = (int) slotNum;
		int offset = (int) getScrollOffset(entity);
		int actualIndex = offset + slot;
		SoulCaptureSystemProcedure.releaseAtIndex(entity, actualIndex);
	}

	// get page info text
	public static String getPageInfo(Entity entity) {
		if (!(entity instanceof Player player))
			return "";
		int total = (int) SoulCaptureSystemProcedure.getCapturedCount(player);
		int offset = (int) getScrollOffset(entity);
		if (total == 0) {
			return "No entities";
		}
		int showing = Math.min(10, total - offset);
		return (offset + 1) + "-" + (offset + showing) + "/" + total;
	}
}
