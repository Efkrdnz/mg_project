package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

public class InvWhileProjectileFlyingTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity immediatesourceentity) {
		if (immediatesourceentity == null)
			return;
		immediatesourceentity.setNoGravity(true);
		if (world instanceof ServerLevel _level)
			_level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 3, 0.05, 0.05, 0.05, 0);
		immediatesourceentity.getPersistentData().putDouble("life", (immediatesourceentity.getPersistentData().getDouble("life") + 1));
		if (immediatesourceentity.getPersistentData().getDouble("life") >= 7) {
			if (!immediatesourceentity.level().isClientSide())
				immediatesourceentity.discard();
		}
	}
}
