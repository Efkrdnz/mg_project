package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.PlayerTrackerGUI;

public class PlayerTrackerOpenProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		// CRITICAL: only run on the physical client side
		if (!world.isClientSide()) {
			return; // stop here if we're on server
		}
		// double check we're on the actual client
		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.equals(entity)) {
			PlayerTrackerGUI.openGUI();
		}
	}
}
