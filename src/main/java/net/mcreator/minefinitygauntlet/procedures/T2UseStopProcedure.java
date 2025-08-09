package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class T2UseStopProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (MinefinityGauntletModVariables.WorldVariables.get(world).TimeStop) {
			TickResumeProcedure.execute(world, x, y, z);
		} else {
			TimeStopProcedure.execute(world, x, y, z);
		}
	}
}
