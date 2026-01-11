package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;

import java.util.Comparator;

public class P3BeamOnTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double raytrace_distance = 0;
		String found_entity_name = "";
		Entity ent = null;
		boolean entity_found = false;
		if (!world.isClientSide() && entity.getPersistentData().getBoolean("beamPower")) {
			raytrace_distance = 0;
			entity_found = false;
			for (int index0 = 0; index0 < 50; index0++) {
				if (!((world
						.getBlockState(BlockPos.containing(entity.getX() + raytrace_distance * entity.getLookAngle().x, entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y, entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
						.getBlock() == Blocks.AIR)) {
					if (world instanceof ServerLevel _level)
						_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_1.get()), (entity.getX() + raytrace_distance * entity.getLookAngle().x),
								(entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z), 1, 0.2, 0.2, 0.2, 0);
					break;
				}
				if (!world
						.getEntitiesOfClass(LivingEntity.class,
								AABB.ofSize(new Vec3((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)),
										1, 1, 1),
								e -> true)
						.isEmpty()
						&& !(((Entity) world.getEntitiesOfClass(LivingEntity.class,
								AABB.ofSize(new Vec3((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.6 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)),
										1, 1, 1),
								e -> true).stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
									}
								}.compareDistOf((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.6 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
								.findFirst().orElse(null)) == entity)) {
					entity_found = true;
					if (!(((Entity) world.getEntitiesOfClass(LivingEntity.class, AABB
							.ofSize(new Vec3((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)), 1, 1, 1),
							e -> true).stream().sorted(new Object() {
								Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
									return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
								}
							}.compareDistOf((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
							.findFirst().orElse(null)) == (null))) {
						if (!(((Entity) world.getEntitiesOfClass(LivingEntity.class,
								AABB.ofSize(new Vec3((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)),
										1, 1, 1),
								e -> true).stream().sorted(new Object() {
									Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
										return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
									}
								}.compareDistOf((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
								.findFirst().orElse(null)) == entity)) {
							ent = (Entity) world.getEntitiesOfClass(LivingEntity.class,
									AABB.ofSize(
											new Vec3((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)),
											1, 1, 1),
									e -> true).stream().sorted(new Object() {
										Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
											return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
										}
									}.compareDistOf((entity.getX() + raytrace_distance * entity.getLookAngle().x), (entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
									.findFirst().orElse(null);
						}
						if (!(ent == null)) {
							if (ent instanceof LivingEntity livingEntity) {
								livingEntity.invulnerableTime = 0; // Reset hurt timer
							}
							ent.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 2);
							if (world instanceof ServerLevel _level)
								_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_2.get()), (entity.getX() + raytrace_distance * entity.getLookAngle().x),
										(entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y), (entity.getZ() + raytrace_distance * entity.getLookAngle().z), 1, 0.2, 0.2, 0.2, 0);
							break;
						}
					}
				} else {
					entity_found = false;
					raytrace_distance = raytrace_distance + 0.3;
				}
			}
		}
	}
}
