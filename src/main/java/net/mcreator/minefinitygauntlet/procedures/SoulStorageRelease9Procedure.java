package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class SoulStorageRelease9Procedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		Entity ent = null;
		ent = entity;
		SoulGUIScrollManagerProcedure.releaseAtSlot(entity, 9);
	}
}
