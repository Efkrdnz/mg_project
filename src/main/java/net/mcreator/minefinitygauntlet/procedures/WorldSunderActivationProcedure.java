
package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ArrayList;

public class WorldSunderActivationProcedure {
	private static SpreadState activeSpread = null;

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null || world == null)
			return;
		if (!(world instanceof Level level))
			return;
		// prevent multiple simultaneous World Sunders
		if (activeSpread != null && !activeSpread.isComplete()) {
			return;
		}
		BlockPos startPos = BlockPos.containing(x, y, z);
		BlockPos surfacePos = findNearestSolidBlock(level, startPos);
		if (surfacePos == null)
			return;
		createImpactEffect(level, surfacePos);
		// start gradual spread
		activeSpread = new SpreadState(level, surfacePos, entity);
	}

	public static void tickSpread(LevelAccessor world) {
		// Store reference locally to avoid race conditions
		SpreadState spread = activeSpread;
		if (spread != null) {
			if (!spread.isComplete()) {
				spread.tick(world);
			}
			// Check again after tick
			if (spread.isComplete()) {
				activeSpread = null;
			}
		}
	}

	private static BlockPos findNearestSolidBlock(Level level, BlockPos start) {
		if (!level.isEmptyBlock(start)) {
			return start;
		}
		for (int radius = 1; radius <= 10; radius++) {
			for (int dx = -radius; dx <= radius; dx++) {
				for (int dy = -radius; dy <= radius; dy++) {
					for (int dz = -radius; dz <= radius; dz++) {
						if (Math.abs(dx) == radius || Math.abs(dy) == radius || Math.abs(dz) == radius) {
							BlockPos checkPos = start.offset(dx, dy, dz);
							if (!level.isEmptyBlock(checkPos)) {
								return checkPos;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static void createImpactEffect(Level level, BlockPos pos) {
		if (level instanceof ServerLevel serverLevel) {
			for (int i = 0; i < 150; i++) {
				double offsetX = (Math.random() - 0.5) * 3;
				double offsetY = (Math.random() - 0.5) * 3;
				double offsetZ = (Math.random() - 0.5) * 3;
				serverLevel.sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5 + offsetX, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5 + offsetZ, 0, 0, 0.3, 0, 0.5);
				serverLevel.sendParticles(ParticleTypes.PORTAL, pos.getX() + 0.5 + offsetX, pos.getY() + 0.5 + offsetY, pos.getZ() + 0.5 + offsetZ, 0, 0, 0.2, 0, 0.3);
			}
			level.playSound(null, pos, SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 3.0f, 0.5f);
			level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 2.0f, 0.7f);
		}
	}

	private static List<BlockPos> getAllAdjacentExposedBlocks(Level level, BlockPos pos) {
		List<BlockPos> adjacent = new ArrayList<>();
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (dx == 0 && dy == 0 && dz == 0)
						continue;
					BlockPos checkPos = pos.offset(dx, dy, dz);
					if (!level.isEmptyBlock(checkPos) && isExposedToAir(level, checkPos)) {
						adjacent.add(checkPos);
					} else if (level.isEmptyBlock(checkPos)) {
						for (int stretch = 1; stretch <= 2; stretch++) {
							BlockPos stretchPos = pos.offset(dx * stretch, dy * stretch, dz * stretch);
							if (!level.isEmptyBlock(stretchPos) && isExposedToAir(level, stretchPos)) {
								adjacent.add(stretchPos);
								break;
							}
						}
					}
				}
			}
		}
		return adjacent;
	}

	private static boolean isExposedToAir(Level level, BlockPos pos) {
		for (Direction dir : Direction.values()) {
			BlockPos adjacentPos = pos.relative(dir);
			if (level.isEmptyBlock(adjacentPos)) {
				return true;
			}
		}
		return false;
	}

	private static void obliterateNearbyEntities(Level level, BlockPos pos, Entity caster) {
		// ONLY run on server side to prevent desyncs
		if (!(level instanceof ServerLevel serverLevel)) {
			return;
		}
		AABB area = new AABB(pos).inflate(1.5);
		List<Entity> entities = level.getEntitiesOfClass(Entity.class, area);
		for (Entity target : entities) {
			if (target == caster)
				continue;
			if (!target.isAlive())
				continue; // skip already dead entities
			// death particles
			for (int i = 0; i < 20; i++) {
				double vx = (Math.random() - 0.5) * 0.5;
				double vy = (Math.random() - 0.5) * 0.5;
				double vz = (Math.random() - 0.5) * 0.5;
				serverLevel.sendParticles(ParticleTypes.SOUL, target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(), 0, vx, vy, vz, 0.2);
			}
			// remove entity properly
			target.remove(Entity.RemovalReason.KILLED);
		}
	}

	private static class SpreadState {
		private final Set<BlockPos> visited;
		private final Queue<BlockPos> queue;
		private final BlockPos origin;
		private final Entity caster;
		private final int maxSpread;
		private final int maxBlocks;
		private final int blocksPerTick;
		private int totalProcessed;
		private boolean complete;
		private final boolean isServerSide;

		SpreadState(Level level, BlockPos origin, Entity caster) {
			this.visited = new HashSet<>();
			this.queue = new LinkedList<>();
			this.origin = origin;
			this.caster = caster;
			this.maxSpread = 150;
			this.maxBlocks = 20000;
			this.blocksPerTick = 400;
			this.totalProcessed = 0;
			this.complete = false;
			this.isServerSide = !level.isClientSide();
			queue.add(origin);
			visited.add(origin);
		}

		void tick(LevelAccessor world) {
			if (complete || !(world instanceof Level level))
				return;
			int processedThisTick = 0;
			// FIXED: Check queue is not empty AND poll returns non-null
			while (processedThisTick < blocksPerTick && totalProcessed < maxBlocks) {
				BlockPos current = queue.poll();
				// CRITICAL FIX: Stop if queue is empty
				if (current == null) {
					complete = true;
					break;
				}
				processedThisTick++;
				totalProcessed++;
				double distFromOrigin = Math.sqrt(current.distSqr(origin));
				if (distFromOrigin > maxSpread)
					continue;
				float intensity = 1.0f - (float) (distFromOrigin / maxSpread);
				intensity = Math.max(0.3f, intensity);
				// add to renderer (works on both sides due to ConcurrentHashMap)
				WorldSunderRendererProcedure.CLIENT_VOID_BLOCKS.put(current, intensity);
				// FIXED: Only obliterate on server side, every 3rd block
				if (isServerSide && totalProcessed % 3 == 0) {
					obliterateNearbyEntities(level, current, caster);
				}
				for (BlockPos neighbor : getAllAdjacentExposedBlocks(level, current)) {
					if (!visited.contains(neighbor)) {
						visited.add(neighbor);
						queue.add(neighbor);
					}
				}
			}
			// check if complete
			if (queue.isEmpty() || totalProcessed >= maxBlocks) {
				complete = true;
			}
		}

		boolean isComplete() {
			return complete;
		}
	}
}
