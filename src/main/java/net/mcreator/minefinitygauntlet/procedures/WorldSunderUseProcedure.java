package net.mcreator.minefinitygauntlet.procedures;

import org.checkerframework.checker.units.qual.h;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class WorldSunderUseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double h = 0;
		Entity ent = null;
		if (world.isClientSide()) {
			h = x;
			h = y;
			h = z;
			ent = entity;
		}
		WorldSunderActivationProcedure.execute(world, x, y, z, entity);
	}
}
