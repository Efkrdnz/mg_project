package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class AbilitySelectInfinityProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 0) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.SelectedPower = 1;
				_vars.syncPlayerVariables(entity);
			}
		}
	}
}
