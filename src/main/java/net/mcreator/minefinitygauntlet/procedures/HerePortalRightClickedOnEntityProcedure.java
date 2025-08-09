package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.entity.HerePortalEntity;

public class HerePortalRightClickedOnEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((sourceentity.getStringUUID()).equals(entity instanceof HerePortalEntity _datEntS ? _datEntS.getEntityData().get(HerePortalEntity.DATA_master) : "")) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}
