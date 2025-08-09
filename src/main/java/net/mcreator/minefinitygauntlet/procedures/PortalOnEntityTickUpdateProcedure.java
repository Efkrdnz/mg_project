package net.mcreator.minefinitygauntlet.procedures;

import org.joml.Vector3f;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.minefinitygauntlet.entity.PortalEntity;

public class PortalOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof PortalEntity _datEntSetI)
			_datEntSetI.getEntityData().set(PortalEntity.DATA_life, (int) ((entity instanceof PortalEntity _datEntI ? _datEntI.getEntityData().get(PortalEntity.DATA_life) : 0) + 1));
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles((new DustParticleOptions(new Vector3f(0 / 255.0F, 0 / 255.0F, 255 / 255.0F), 2)), x, (y + 1.2), z, 25, 0.5, 0.5, 0.5, 1);
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles((new DustParticleOptions(new Vector3f(0 / 255.0F, 23 / 255.0F, 61 / 255.0F), 2)), x, (y + 1.2), z, 12, 0.65, 0.65, 0.65, 1);
		if ((entity instanceof PortalEntity _datEntI ? _datEntI.getEntityData().get(PortalEntity.DATA_life) : 0) >= 100) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
	}
}
