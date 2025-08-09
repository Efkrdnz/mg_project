package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import java.util.List;
import java.util.Comparator;

public class R1ItemInHandTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).r1rc == true) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(7 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof net.minecraft.world.entity.projectile.Projectile) {
						if (!entityiterator.level().isClientSide())
							entityiterator.discard();
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.BUBBLE, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 8, 0.1, 0.1, 0.1, 1);
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.BUBBLE_POP, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 8, 0.1, 0.1, 0.1, 0);
						if (world instanceof ServerLevel _level)
							_level.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 8, 0.1, 0.1, 0.1, 0);
					}
				}
			}
		}
	}
}
