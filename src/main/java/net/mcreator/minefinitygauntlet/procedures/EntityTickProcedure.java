package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;

import javax.annotation.Nullable;

import java.util.Comparator;

@EventBusSubscriber
public class EntityTickProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof FallingBlockEntity) {
			if (entity.getPersistentData().getBoolean("teleblock")) {
				if (!entity.getPersistentData().getBoolean("canthrow")) {
					if (!world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3(x, y, z), 1, 1, 1), e -> true).isEmpty()) {
						((Entity) world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3(x, y, z), 1, 1, 1), e -> true).stream().sorted(new Object() {
							Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
								return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
							}
						}.compareDistOf(x, y, z)).findFirst().orElse(null)).hurt(new DamageSource(world.holderOrThrow(DamageTypes.FALLING_BLOCK)), 10);
						if (!entity.level().isClientSide())
							entity.discard();
					}
				}
			}
		}
	}
}
