package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

public class SoulStorageLabel4Procedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		String lmaotext = "";
		Entity ent = null;
		ent = entity;
		lmaotext = SoulGUIScrollManagerProcedure.getEntityNameAtSlot(entity, 4);
		return lmaotext;
	}
}
