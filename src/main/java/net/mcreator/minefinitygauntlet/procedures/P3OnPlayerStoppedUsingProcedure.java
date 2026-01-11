package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class P3OnPlayerStoppedUsingProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putBoolean("beamPower", false);
	}
}
