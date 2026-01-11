package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;
import java.nio.file.Files;

public class CheckInfinityBackupProcedure {
	// returns true if a disk backup exists
	public static boolean hasBackup(Entity entity) {
		if (!(entity.level() instanceof ServerLevel level))
			return false;
		MinecraftServer server = level.getServer();
		if (server == null)
			return false;
		Path worldPath = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
		Path backupPath = worldPath.getParent().resolve("infinity_gauntlet_backup");
		return Files.exists(backupPath);
	}

	// returns true if memory snapshot exists
	public static boolean hasMemorySnapshot() {
		return InfinityWorldResetProcedure.hasMemorySnapshot();
	}
}
