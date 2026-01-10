package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class CosmiRodPowerHasItemGlowingEffectProcedure {
	public static boolean execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return false;
		if (!world.isClientSide() && entity.getPersistentData().getBoolean("cosmirod_charge")) {
			return true;
		}
		return false;
	}
}
