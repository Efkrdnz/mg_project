package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class P3RightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double raytrace_distance = 0;
		String found_entity_name = "";
		boolean entity_found = false;
		Entity ent = null;
		entity.getPersistentData().putBoolean("beamPower", true);
	}
}
