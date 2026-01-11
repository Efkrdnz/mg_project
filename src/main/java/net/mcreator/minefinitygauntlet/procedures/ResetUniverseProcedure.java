package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

public class ResetUniverseProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Entity ent = null;
		ent = entity;
		if (CheckInfinityBackupProcedure.hasBackup(entity) == true) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("Universe is being reset!"), false);
			MinefinityGauntletMod.queueServerWork(60, () -> {
				InfinityWorldResetProcedure.restoreSnapshot(entity, true);
			});
		} else {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("\u00A7cNo backup!"), false);
		}
	}
}
