package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class So2VampiricBeamStopUsingProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.beamSoul = false;
			_vars.syncPlayerVariables(entity);
		}
	}
}
