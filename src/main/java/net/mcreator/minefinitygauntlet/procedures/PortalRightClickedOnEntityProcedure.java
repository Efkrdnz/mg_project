package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.entity.PortalEntity;

public class PortalRightClickedOnEntityProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if ((sourceentity.getStringUUID()).equals(entity instanceof PortalEntity _datEntS ? _datEntS.getEntityData().get(PortalEntity.DATA_master) : "")) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}
