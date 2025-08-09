package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import java.util.Comparator;

public class So3SwapUseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		Entity ent = null;
		boolean entity_found = false;
		double old_z = 0;
		double old_x = 0;
		double old_y = 0;
		double raytrace_distance = 0;
		String found_entity_name = "";
		raytrace_distance = 0;
		entity_found = false;
		for (int index0 = 0; index0 < 15; index0++) {
			if (!world
					.getEntitiesOfClass(LivingEntity.class,
							AABB.ofSize(new Vec3(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
											.getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
											entity)).getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(
											1f).add(
													entity.getViewVector(1f).scale(raytrace_distance)),
											ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ())),
									1, 1, 1),
							e -> true)
					.isEmpty()
					&& !(((Entity) world
							.getEntitiesOfClass(LivingEntity.class,
									AABB.ofSize(
											new Vec3(
													(entity.level()
															.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
																	entity))
															.getBlockPos().getX()),
													(entity.level()
															.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
																	entity))
															.getBlockPos().getY()),
													(entity.level().clip(
															new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
															.getBlockPos().getZ())),
											1, 1, 1),
									e -> true)
							.stream().sorted(new Object() {
								Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
									return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
								}
							}.compareDistOf(
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
											.getBlockPos().getX()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
											.getBlockPos().getY()),
									(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
											.getBlockPos().getZ())))
							.findFirst().orElse(null)) == entity)) {
				entity_found = true;
				if (!(((Entity) world.getEntitiesOfClass(LivingEntity.class,
						AABB.ofSize(new Vec3(
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getX()),
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getY()),
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getZ())),
								1, 1, 1),
						e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getX()),
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getY()),
								(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
										.getBlockPos().getZ())))
						.findFirst().orElse(null)) == (null))) {
					if (entity_found) {
						old_x = x;
						old_y = y;
						old_z = z;
						{
							Entity _ent = entity;
							_ent.teleportTo((ent.getX()), (ent.getY()), (ent.getZ()));
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport((ent.getX()), (ent.getY()), (ent.getZ()), _ent.getYRot(), _ent.getXRot());
						}
						{
							Entity _ent = ent;
							_ent.teleportTo(old_x, old_y, old_z);
							if (_ent instanceof ServerPlayer _serverPlayer)
								_serverPlayer.connection.teleport(old_x, old_y, old_z, _ent.getYRot(), _ent.getXRot());
						}
					}
				}
			} else {
				entity_found = false;
				raytrace_distance = raytrace_distance + 1;
			}
		}
	}
}
