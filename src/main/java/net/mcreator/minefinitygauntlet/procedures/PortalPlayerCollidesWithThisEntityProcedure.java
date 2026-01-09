package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;

import net.mcreator.minefinitygauntlet.entity.PortalEntity;

public class PortalPlayerCollidesWithThisEntityProcedure {
	// handles portal collision and teleportation for any entity
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		double xx = 0;
		double yy = 0;
		double zz = 0;
		xx = (entity instanceof PortalEntity _datEntI ? _datEntI.getEntityData().get(PortalEntity.DATA_targetX) : 0) / 10;
		yy = (entity instanceof PortalEntity _datEntI ? _datEntI.getEntityData().get(PortalEntity.DATA_targetY) : 0) / 10;
		zz = (entity instanceof PortalEntity _datEntI ? _datEntI.getEntityData().get(PortalEntity.DATA_targetZ) : 0) / 10;
		if (sourceentity.level() instanceof ServerLevel) {
			if (sourceentity instanceof ServerPlayer _serverPlayer) {
				_serverPlayer.connection.teleport(xx, yy, zz, sourceentity.getYRot(), sourceentity.getXRot());
			} else {
				sourceentity.teleportTo(xx, yy, zz);
			}
		}
		if ((sourceentity.getStringUUID()).equals(entity instanceof PortalEntity _datEntS ? _datEntS.getEntityData().get(PortalEntity.DATA_master) : "")) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}
