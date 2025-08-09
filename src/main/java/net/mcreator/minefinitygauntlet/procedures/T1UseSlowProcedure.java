package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class T1UseSlowProcedure {
	public static void execute(LevelAccessor world) {
		if (MinefinityGauntletModVariables.WorldVariables.get(world).TimeSlow) {
			MinefinityGauntletModVariables.WorldVariables.get(world).TimeSlow = false;
			MinefinityGauntletModVariables.WorldVariables.get(world).syncData(world);
		} else {
			MinefinityGauntletModVariables.WorldVariables.get(world).TimeSlow = true;
			MinefinityGauntletModVariables.WorldVariables.get(world).syncData(world);
		}
	}
}
