package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class SlotSelectPower2Procedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.apending = 2;
			_vars.syncPlayerVariables(entity);
		}
	}
}
