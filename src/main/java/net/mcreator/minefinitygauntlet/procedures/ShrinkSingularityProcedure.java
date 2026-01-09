package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.entity.BlackholeEntity;

import java.util.List;

@EventBusSubscriber
public class ShrinkSingularityProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event.getEntity());
	}

	public static void execute(Entity entity) {
		if (entity == null)
			return;
		Level level = entity.level();
		boolean shrinking = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).singularityShrink;
		if (!shrinking) {
			return;
		}
		if (level.isClientSide())
			return;
		// find all blackholes owned by this player
		Vec3 playerPos = entity.position();
		AABB searchBox = new AABB(playerPos.x - 100, playerPos.y - 100, playerPos.z - 100, playerPos.x + 100, playerPos.y + 100, playerPos.z + 100);
		List<BlackholeEntity> nearbyHoles = level.getEntitiesOfClass(BlackholeEntity.class, searchBox, e -> {
			Entity owner = e.getOwner();
			return owner != null && owner.getUUID().equals(entity.getUUID());
		});
		BlackholeEntity targetBlackhole = null;
		Vec3 playerLook = entity.getLookAngle();
		Vec3 eyePos = entity.getEyePosition();
		// find blackhole player is looking at
		for (BlackholeEntity blackhole : nearbyHoles) {
			if (!isPlayerLookingAtBlackhole(eyePos, playerLook, blackhole)) {
				continue;
			}
			if (targetBlackhole == null || blackhole.distanceTo(entity) < targetBlackhole.distanceTo(entity)) {
				targetBlackhole = blackhole;
			}
		}
		if (targetBlackhole != null) {
			// rapidly shrink the blackhole player is looking at
			int currentSize = targetBlackhole.getEntityData().get(BlackholeEntity.DATA_size);
			int shrinkAmount = Math.max(1, currentSize / 50);
			int newSize = Math.max(0, currentSize - shrinkAmount);
			targetBlackhole.getEntityData().set(BlackholeEntity.DATA_size, newSize);
			// remove if shrunk to nothing
			if (newSize <= 0) {
				targetBlackhole.discard();
			}
		}
		// shrinking particles around player's hand
		if (level instanceof ServerLevel serverLevel && entity.tickCount % 2 == 0) {
			Vec3 handPos = playerPos.add(0, entity.getEyeHeight() - 0.3, 0);
			Vec3 rightVec = playerLook.cross(new Vec3(0, 1, 0)).normalize();
			Vec3 particlePos = handPos.add(rightVec.scale(0.5)).add(playerLook.scale(0.3));
			serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, particlePos.x, particlePos.y, particlePos.z, 3, 0.1, 0.1, 0.1, 0.02);
		}
	}

	// checks if player is looking at blackhole with angle tolerance based on size and distance
	private static boolean isPlayerLookingAtBlackhole(Vec3 eyePos, Vec3 lookVec, BlackholeEntity blackhole) {
		Vec3 blackholePos = blackhole.position().add(0, blackhole.getBbHeight() * 0.5, 0);
		Vec3 toBlackhole = blackholePos.subtract(eyePos);
		double distance = toBlackhole.length();
		if (distance < 0.1) {
			return true;
		}
		Vec3 toBlackholeNorm = toBlackhole.normalize();
		double dotProduct = lookVec.dot(toBlackholeNorm);
		double angle = Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct)));
		double angleDegrees = Math.toDegrees(angle);
		// calculate visual size of blackhole
		int size = blackhole.getEntityData().get(BlackholeEntity.DATA_size);
		float visualRadius = 0.5f + (size * 0.115f);
		// calculate angle tolerance based on visual size and distance
		double angularSize = Math.atan(visualRadius / distance);
		double angularSizeDegrees = Math.toDegrees(angularSize);
		// add extra tolerance for easier targeting
		double tolerance = angularSizeDegrees + 5.0;
		return angleDegrees <= tolerance;
	}
}
