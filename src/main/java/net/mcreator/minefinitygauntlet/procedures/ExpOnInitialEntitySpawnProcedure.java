package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class ExpOnInitialEntitySpawnProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("rad", 3);
	}
}
