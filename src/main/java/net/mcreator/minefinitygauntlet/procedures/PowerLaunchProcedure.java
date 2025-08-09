package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class PowerLaunchProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + 3 * entity.getLookAngle().x), (entity.getDeltaMovement().y() + 3 * entity.getLookAngle().y), (entity.getDeltaMovement().z() + 3 * entity.getLookAngle().z)));
		if (!world.isClientSide()) {
			entity.getPersistentData().putBoolean("powerleap", true);
		}
	}
}
