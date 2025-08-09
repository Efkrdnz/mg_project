package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class InfinityGauntletItemInHandTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 1) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
				P3BeamOnTickProcedure.execute(world, entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 2) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				R1ItemInHandTickProcedure.execute(world, x, y, z, entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
				R3HealRCProcedure.execute(world, entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 4) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
				So2VampiricBeamOnTickProcedure.execute(world, entity);
			}
		}
	}
}
