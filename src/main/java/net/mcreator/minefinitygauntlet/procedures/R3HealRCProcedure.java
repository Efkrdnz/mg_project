package net.mcreator.minefinitygauntlet.procedures;

import org.checkerframework.checker.units.qual.m;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

import java.util.Comparator;

public class R3HealRCProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double width = 0;
		double length = 0;
		double i = 0;
		double j = 0;
		double k = 0;
		double l = 0;
		double m = 0;
		double speed = 0;
		double raytrace_distance = 0;
		String found_entity_name = "";
		boolean entity_found = false;
		if ((entity instanceof LivingEntity _entUseItem0 ? _entUseItem0.getUseItem() : ItemStack.EMPTY).getItem() == MinefinityGauntletModItems.INFINITY_GAUNTLET.get()) {
			if (!entity.isShiftKeyDown()) {
				raytrace_distance = 0;
				entity_found = false;
				for (int index0 = 0; index0 < 15; index0++) {
					if (!world
							.getEntitiesOfClass(LivingEntity.class,
									AABB.ofSize(
											new Vec3(
													(entity.level()
															.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
																	entity))
															.getBlockPos().getX()),
													(entity.level().clip(
															new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
															.getBlockPos().getY()),
													(entity.level()
															.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
																	entity))
															.getBlockPos().getZ())),
											1, 1, 1),
									e -> true)
							.isEmpty()
							&& !(((Entity) world
									.getEntitiesOfClass(LivingEntity.class,
											AABB.ofSize(new Vec3(
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
						if (!(((Entity) world
								.getEntitiesOfClass(LivingEntity.class,
										AABB.ofSize(
												new Vec3(
														(entity.level()
																.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
																		entity))
																.getBlockPos().getX()),
														(entity.level()
																.clip(new ClipContext(
																		entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity))
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
								.findFirst().orElse(null)) == (null))) {
							if (((Entity) world
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
															(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER,
																	ClipContext.Fluid.NONE, entity)).getBlockPos().getZ())),
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
									.findFirst().orElse(null)) instanceof LivingEntity _entity)
								_entity.setHealth(
										(float) ((((Entity) world
												.getEntitiesOfClass(LivingEntity.class,
														AABB.ofSize(
																new Vec3(
																		(entity.level()
																				.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER,
																						ClipContext.Fluid.NONE, entity))
																				.getBlockPos().getX()),
																		(entity.level()
																				.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER,
																						ClipContext.Fluid.NONE, entity))
																				.getBlockPos().getY()),
																		(entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(raytrace_distance)), ClipContext.Block.COLLIDER,
																				ClipContext.Fluid.NONE, entity)).getBlockPos().getZ())),
																1, 1, 1),
														e -> true)
												.stream().sorted(new Object() {
													Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
														return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
													}
												}.compareDistOf(
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
																.getBlockPos().getZ())))
												.findFirst().orElse(null)) instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 1));
							break;
						}
					} else {
						entity_found = false;
						raytrace_distance = raytrace_distance + 1;
					}
				}
			} else {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth((float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 1));
			}
		}
	}
}
