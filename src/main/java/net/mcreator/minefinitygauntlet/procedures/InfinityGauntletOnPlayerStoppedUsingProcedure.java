package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class InfinityGauntletOnPlayerStoppedUsingProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		P3OnPlayerStoppedUsingProcedure.execute(entity);
		R1OnPlayerStoppedUsingProcedure.execute(entity);
		So1UseFinishRCProcedure.execute(entity);
		So2VampiricBeamStopUsingProcedure.execute(entity);
	}
}
