package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class InfinityGauntletEntitySwingsItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 0) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 5) {
				S5ShootProcedure.execute(world, x, y, z, entity);
			}
		}
	}
}
