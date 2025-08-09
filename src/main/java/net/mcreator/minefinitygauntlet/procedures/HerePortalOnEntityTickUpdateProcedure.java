package net.mcreator.minefinitygauntlet.procedures;

import org.joml.Vector3f;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.DustParticleOptions;

import net.mcreator.minefinitygauntlet.entity.HerePortalEntity;

import java.util.List;
import java.util.Comparator;

public class HerePortalOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double rad = 0;
		String target = "";
		String master = "";
		rad = entity instanceof HerePortalEntity _datEntI ? _datEntI.getEntityData().get(HerePortalEntity.DATA_radius) : 0;
		target = entity instanceof HerePortalEntity _datEntS ? _datEntS.getEntityData().get(HerePortalEntity.DATA_type) : "";
		master = entity instanceof HerePortalEntity _datEntS ? _datEntS.getEntityData().get(HerePortalEntity.DATA_master) : "";
		if (entity instanceof HerePortalEntity _datEntSetI)
			_datEntSetI.getEntityData().set(HerePortalEntity.DATA_life, (int) ((entity instanceof HerePortalEntity _datEntI ? _datEntI.getEntityData().get(HerePortalEntity.DATA_life) : 0) + 1));
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles((new DustParticleOptions(new Vector3f(0 / 255.0F, 0 / 255.0F, 255 / 255.0F), 2)), x, (y + 1.2), z, 25, 0.5, 0.5, 0.5, 1);
		if (world instanceof ServerLevel)
			((ServerLevel) world).sendParticles((new DustParticleOptions(new Vector3f(0 / 255.0F, 23 / 255.0F, 61 / 255.0F), 2)), x, (y + 1.2), z, 12, 0.65, 0.65, 0.65, 1);
		if ((entity instanceof HerePortalEntity _datEntI ? _datEntI.getEntityData().get(HerePortalEntity.DATA_life) : 0) >= (entity instanceof HerePortalEntity _datEntI ? _datEntI.getEntityData().get(HerePortalEntity.DATA_maxlife) : 0)) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 * rad / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator.getStringUUID()).equals(master)) {
					if (entityiterator.getType().getDescriptionId().toLowerCase().contains(target.toLowerCase())) {
						if ((entityiterator != null ? entity.distanceTo(entityiterator) : -1) > 8) {
							{
								Entity _ent = entityiterator;
								_ent.teleportTo(x, y, z);
								if (_ent instanceof ServerPlayer _serverPlayer)
									_serverPlayer.connection.teleport(x, y, z, _ent.getYRot(), _ent.getXRot());
							}
							if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 1));
						}
					}
				}
			}
		}
	}
}
