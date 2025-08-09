package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class R2RightclickedProcedure {
	public static void execute(LevelAccessor world) {
		if (MinefinityGauntletModVariables.MapVariables.get(world).colorshift == true) {
			MinefinityGauntletModVariables.MapVariables.get(world).colorshift = false;
			MinefinityGauntletModVariables.MapVariables.get(world).syncData(world);
		} else if (MinefinityGauntletModVariables.MapVariables.get(world).colorshift == false) {
			MinefinityGauntletModVariables.MapVariables.get(world).colorshift = true;
			MinefinityGauntletModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
