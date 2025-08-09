package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class P3RightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double raytrace_distance = 0;
		String found_entity_name = "";
		boolean entity_found = false;
		Entity ent = null;
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.beamPower = true;
			_vars.syncPlayerVariables(entity);
		}
	}
}
