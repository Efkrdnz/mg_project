package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import java.util.Comparator;

public class So2VampiricBeamOnTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double raytrace_distance = 0;
		Entity ent = null;
		boolean entity_found = false;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).beamSoul) {
			raytrace_distance = 0;
			entity_found = false;
			for (int index0 = 0; index0 < 50; index0++) {
				if (!((world
						.getBlockState(BlockPos.containing(entity.getX() + raytrace_distance * entity.getLookAngle().x, entity.getY() + 1.4 + raytrace_distance * entity.getLookAngle().y, entity.getZ() + raytrace_distance * entity.getLookAngle().z)))
						.getBlock() == Blocks.AIR)) {
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
							ent.invulnerableTime = 4;
							ent.hurt(new DamageSource(world.holderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("minefinity_gauntlet:vampiric"))), entity), (float) 0.5);
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
