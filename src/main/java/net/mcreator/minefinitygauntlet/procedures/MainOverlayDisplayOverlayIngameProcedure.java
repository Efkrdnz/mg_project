package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

public class MainOverlayDisplayOverlayIngameProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((entity instanceof LivingEntity _entity) ? _entity.isHolding(MinefinityGauntletModItems.INFINITY_GAUNTLET.get()) : false) {
			return true;
		}
		return false;
	}
}
