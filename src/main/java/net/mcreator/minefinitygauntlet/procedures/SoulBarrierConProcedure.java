package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class SoulBarrierConProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).soul_barrier && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
			return true;
		}
		return false;
	}
}
