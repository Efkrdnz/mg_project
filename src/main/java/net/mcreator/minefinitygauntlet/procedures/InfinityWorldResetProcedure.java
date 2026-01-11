package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.common.util.BlockSnapshot;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;

import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Files;

import java.io.IOException;
import java.io.FileOutputStream;

public class InfinityWorldResetProcedure {
	private static WorldSnapshot memorySnapshot = null; // Time loop snapshot
	private static WorldSnapshot universeSnapshot = null; // Universe backup
	private static final String BACKUP_FOLDER = "infinity_gauntlet_backup";
	private static final int CHUNK_RADIUS = 12;
	private static final int BLOCKS_PER_TICK = 8192;

	// creates snapshot - if toDisk=true, saves to both memory (universe) and disk
	public static void createSnapshot(Entity entity, boolean toDisk) {
		if (!(entity.level() instanceof ServerLevel level))
			return;
		MinecraftServer server = level.getServer();
		if (server == null)
			return;
		server.execute(() -> {
			try {
				WorldSnapshot snapshot = new WorldSnapshot();
				HolderLookup.Provider registries = level.registryAccess();
				for (ServerLevel dimension : server.getAllLevels()) {
					dimension.save(null, true, dimension.noSave);
					DimensionSnapshot dimSnapshot = new DimensionSnapshot();
					dimSnapshot.dimensionKey = dimension.dimension().location().toString();
					dimSnapshot.dimensionType = getDimensionFolder(dimension);
					List<ChunkPos> chunksToSave = new ArrayList<>();
					for (ServerPlayer player : dimension.players()) {
						ChunkPos playerChunk = new ChunkPos(player.blockPosition());
						for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
							for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
								ChunkPos chunkPos = new ChunkPos(playerChunk.x + x, playerChunk.z + z);
								if (!chunksToSave.contains(chunkPos)) {
									chunksToSave.add(chunkPos);
								}
							}
						}
					}
					// Also add spawn area
					BlockPos spawnPos = dimension.getSharedSpawnPos();
					ChunkPos spawnChunk = new ChunkPos(spawnPos);
					for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
						for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
							ChunkPos chunkPos = new ChunkPos(spawnChunk.x + x, spawnChunk.z + z);
							if (!chunksToSave.contains(chunkPos)) {
								chunksToSave.add(chunkPos);
							}
						}
					}
					for (ChunkPos chunkPos : chunksToSave) {
						ChunkAccess chunkAccess = dimension.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
						if (chunkAccess instanceof LevelChunk chunk) {
							ChunkSnapshot chunkSnap = new ChunkSnapshot();
							chunkSnap.chunkX = chunk.getPos().x;
							chunkSnap.chunkZ = chunk.getPos().z;
							BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
							for (int x = 0; x < 16; x++) {
								for (int z = 0; z < 16; z++) {
									for (int y = dimension.getMinBuildHeight(); y < dimension.getMaxBuildHeight(); y++) {
										pos.set(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z);
										BlockState state = chunk.getBlockState(pos);
										if (!state.isAir()) {
											BlockSnapshot blockSnap = new BlockSnapshot();
											blockSnap.x = x;
											blockSnap.y = y;
											blockSnap.z = z;
											blockSnap.state = state;
											BlockEntity be = chunk.getBlockEntity(pos);
											if (be != null) {
												blockSnap.blockEntityData = be.saveWithoutMetadata(registries);
											}
											chunkSnap.blocks.add(blockSnap);
										}
									}
								}
							}
							dimSnapshot.chunks.add(chunkSnap);
						}
					}
					dimension.getAllEntities().forEach(ent -> {
						if (!(ent instanceof ServerPlayer)) {
							CompoundTag entityTag = new CompoundTag();
							ent.saveWithoutId(entityTag);
							entityTag.putString("id", EntityType.getKey(ent.getType()).toString());
							entityTag.putDouble("pos_x", ent.getX());
							entityTag.putDouble("pos_y", ent.getY());
							entityTag.putDouble("pos_z", ent.getZ());
							dimSnapshot.entities.add(entityTag);
						}
					});
					dimension.players().forEach(player -> {
						PlayerSnapshot playerSnap = new PlayerSnapshot();
						playerSnap.playerData = new CompoundTag();
						player.saveWithoutId(playerSnap.playerData);
						playerSnap.position = player.position();
						playerSnap.uuid = player.getStringUUID();
						playerSnap.dimension = dimSnapshot.dimensionKey;
						dimSnapshot.players.add(playerSnap);
					});
					snapshot.dimensions.add(dimSnapshot);
				}
				snapshot.dayTime = level.getDayTime();
				snapshot.gameTime = level.getGameTime();
				snapshot.timestamp = System.currentTimeMillis();
				// Store in appropriate memory slot
				if (toDisk) {
					// Universe backup - stored in universeSnapshot
					universeSnapshot = snapshot;
					saveToDisk(server, snapshot);
					if (entity instanceof ServerPlayer player) {
						int totalChunks = snapshot.dimensions.stream().mapToInt(d -> d.chunks.size()).sum();
						int totalBlocks = snapshot.dimensions.stream().flatMap(d -> d.chunks.stream()).mapToInt(c -> c.blocks.size()).sum();
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6§l[INFINITY GAUNTLET] §eUniverse Backup Created\n" + "§7Chunks: " + totalChunks + " | Blocks: " + totalBlocks + "\n" + "§aSaved to disk + memory"));
					}
				} else {
					// Time loop - stored in memorySnapshot
					memorySnapshot = snapshot;
					if (entity instanceof ServerPlayer player) {
						int totalChunks = snapshot.dimensions.stream().mapToInt(d -> d.chunks.size()).sum();
						int totalBlocks = snapshot.dimensions.stream().flatMap(d -> d.chunks.stream()).mapToInt(c -> c.blocks.size()).sum();
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6§l[INFINITY GAUNTLET] §eTime Loop Created\n" + "§7Chunks: " + totalChunks + " | Blocks: " + totalBlocks + "\n" + "§bMemory only"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// restores from memory - fromDisk parameter chooses which snapshot
	public static void restoreSnapshot(Entity entity, boolean fromDisk) {
		if (!(entity.level() instanceof ServerLevel level))
			return;
		MinecraftServer server = level.getServer();
		if (server == null)
			return;
		server.execute(() -> {
			try {
				// Choose which snapshot to use
				WorldSnapshot snapshot = fromDisk ? universeSnapshot : memorySnapshot;
				if (snapshot == null) {
					if (entity instanceof ServerPlayer player) {
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal(fromDisk ? "§c§l[INFINITY GAUNTLET] No universe backup found!" : "§c§l[INFINITY GAUNTLET] No time loop found!"));
					}
					return;
				}
				// Send players to loading void
				Map<String, Vec3> playerPositions = new HashMap<>();
				Map<String, String> playerDimensions = new HashMap<>();
				ServerLevel endDimension = server.getLevel(Level.END);
				for (ServerPlayer player : server.getPlayerList().getPlayers()) {
					playerPositions.put(player.getStringUUID(), player.position());
					playerDimensions.put(player.getStringUUID(), player.level().dimension().location().toString());
					player.teleportTo(endDimension, 0, 256, 0, 0, 0);
					player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 600, 0, false, false));
					player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 600, 0, false, false));
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal(fromDisk ? "§5§l⚠ UNIVERSE RESET IN PROGRESS ⚠\n§7Restoring from initial backup..." : "§5§l⚠ REVERSING TIME ⚠\n§7Restoring to loop point..."));
				}
				// Restore world asynchronously
				final WorldSnapshot finalSnapshot = snapshot;
				restoreWorldAsync(server, finalSnapshot, 0, () -> {
					// Bring players back
					for (ServerPlayer player : server.getPlayerList().getPlayers()) {
						String uuid = player.getStringUUID();
						Vec3 originalPos = playerPositions.get(uuid);
						String dimKey = playerDimensions.get(uuid);
						if (originalPos != null && dimKey != null) {
							ServerLevel targetDim = StreamSupport.stream(server.getAllLevels().spliterator(), false).filter(lvl -> lvl.dimension().location().toString().equals(dimKey)).findFirst().orElse(level);
							player.teleportTo(targetDim, originalPos.x, originalPos.y, originalPos.z, player.getYRot(), player.getXRot());
							player.removeEffect(MobEffects.BLINDNESS);
							player.removeEffect(MobEffects.SLOW_FALLING);
						}
					}
					server.getPlayerList().getPlayers().forEach(player -> {
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal(fromDisk ? "§5§l✦ UNIVERSE HAS BEEN RESET ✦" : "§5§l✦ TIME HAS BEEN REVERSED ✦"));
					});
				});
			} catch (Exception e) {
				e.printStackTrace();
				if (entity instanceof ServerPlayer player) {
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c§lRestore failed: " + e.getMessage()));
				}
			}
		});
	}

	private static void restoreWorldAsync(MinecraftServer server, WorldSnapshot snapshot, int dimensionIndex, Runnable onComplete) {
		if (dimensionIndex >= snapshot.dimensions.size()) {
			for (ServerLevel level : server.getAllLevels()) {
				level.setDayTime(snapshot.dayTime);
				break;
			}
			onComplete.run();
			return;
		}
		DimensionSnapshot dimSnapshot = snapshot.dimensions.get(dimensionIndex);
		server.execute(() -> {
			try {
				ServerLevel dimension = StreamSupport.stream(server.getAllLevels().spliterator(), false).filter(lvl -> lvl.dimension().location().toString().equals(dimSnapshot.dimensionKey)).findFirst().orElse(null);
				if (dimension == null) {
					restoreWorldAsync(server, snapshot, dimensionIndex + 1, onComplete);
					return;
				}
				HolderLookup.Provider registries = dimension.registryAccess();
				restoreChunksAsync(server, dimension, registries, dimSnapshot, 0, () -> {
					List<Entity> toRemove = new ArrayList<>();
					dimension.getAllEntities().forEach(ent -> {
						if (!(ent instanceof ServerPlayer)) {
							toRemove.add(ent);
						}
					});
					toRemove.forEach(Entity::discard);
					dimSnapshot.entities.forEach(entityTag -> {
						try {
							String entityId = entityTag.getString("id");
							Optional<EntityType<?>> entityTypeOpt = EntityType.byString(entityId);
							if (entityTypeOpt.isPresent()) {
								EntityType<?> entityType = entityTypeOpt.get();
								Entity ent = entityType.create(dimension);
								if (ent != null) {
									ent.load(entityTag);
									dimension.addFreshEntity(ent);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					dimSnapshot.players.forEach(playerSnap -> {
						ServerPlayer player = server.getPlayerList().getPlayers().stream().filter(p -> p.getStringUUID().equals(playerSnap.uuid)).findFirst().orElse(null);
						if (player != null) {
							player.load(playerSnap.playerData);
						}
					});
					restoreWorldAsync(server, snapshot, dimensionIndex + 1, onComplete);
				});
			} catch (Exception e) {
				e.printStackTrace();
				restoreWorldAsync(server, snapshot, dimensionIndex + 1, onComplete);
			}
		});
	}

	private static void restoreChunksAsync(MinecraftServer server, ServerLevel dimension, HolderLookup.Provider registries, DimensionSnapshot dimSnapshot, int chunkIndex, Runnable onComplete) {
		if (chunkIndex >= dimSnapshot.chunks.size()) {
			onComplete.run();
			return;
		}
		ChunkSnapshot chunkSnap = dimSnapshot.chunks.get(chunkIndex);
		server.execute(() -> {
			try {
				LevelChunk chunk = dimension.getChunk(chunkSnap.chunkX, chunkSnap.chunkZ);
				BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
				for (int x = 0; x < 16; x++) {
					for (int z = 0; z < 16; z++) {
						for (int y = dimension.getMinBuildHeight(); y < dimension.getMaxBuildHeight(); y++) {
							pos.set(chunk.getPos().getMinBlockX() + x, y, chunk.getPos().getMinBlockZ() + z);
							chunk.setBlockState(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), false);
						}
					}
				}
				int batchSize = BLOCKS_PER_TICK;
				int batches = (chunkSnap.blocks.size() + batchSize - 1) / batchSize;
				restoreBlocksInBatches(server, dimension, registries, chunk, chunkSnap.blocks, 0, batches, batchSize, () -> {
					chunk.setUnsaved(true);
					restoreChunksAsync(server, dimension, registries, dimSnapshot, chunkIndex + 1, onComplete);
				});
			} catch (Exception e) {
				e.printStackTrace();
				restoreChunksAsync(server, dimension, registries, dimSnapshot, chunkIndex + 1, onComplete);
			}
		});
	}

	private static void restoreBlocksInBatches(MinecraftServer server, ServerLevel dimension, HolderLookup.Provider registries, LevelChunk chunk, List<BlockSnapshot> blocks, int batchIndex, int totalBatches, int batchSize, Runnable onComplete) {
		if (batchIndex >= totalBatches) {
			onComplete.run();
			return;
		}
		server.execute(() -> {
			int start = batchIndex * batchSize;
			int end = Math.min(start + batchSize, blocks.size());
			BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			for (int i = start; i < end; i++) {
				BlockSnapshot blockSnap = blocks.get(i);
				pos.set(chunk.getPos().getMinBlockX() + blockSnap.x, blockSnap.y, chunk.getPos().getMinBlockZ() + blockSnap.z);
				dimension.setBlock(pos, blockSnap.state, 3);
				if (blockSnap.blockEntityData != null) {
					BlockEntity be = dimension.getBlockEntity(pos);
					if (be != null) {
						be.loadWithComponents(blockSnap.blockEntityData, registries);
					}
				}
			}
			restoreBlocksInBatches(server, dimension, registries, chunk, blocks, batchIndex + 1, totalBatches, batchSize, onComplete);
		});
	}

	private static void saveToDisk(MinecraftServer server, WorldSnapshot snapshot) throws IOException {
		Path worldPath = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
		Path backupPath = worldPath.getParent().resolve(BACKUP_FOLDER);
		Files.createDirectories(backupPath);
		CompoundTag metaTag = new CompoundTag();
		metaTag.putLong("timestamp", snapshot.timestamp);
		metaTag.putLong("dayTime", snapshot.dayTime);
		metaTag.putLong("gameTime", snapshot.gameTime);
		try (FileOutputStream fos = new FileOutputStream(backupPath.resolve("snapshot_meta.dat").toFile())) {
			NbtIo.writeCompressed(metaTag, fos);
		}
		for (ServerLevel dimension : server.getAllLevels()) {
			String dimFolder = getDimensionFolder(dimension);
			Path sourcePath = worldPath.resolve(dimFolder);
			Path targetPath = backupPath.resolve(dimFolder);
			copyFolder(sourcePath, targetPath, "region");
			copyFolder(sourcePath, targetPath, "entities");
			copyFolder(sourcePath, targetPath, "poi");
			if (dimFolder.equals("")) {
				Path levelDat = sourcePath.resolve("level.dat");
				if (Files.exists(levelDat)) {
					Files.copy(levelDat, targetPath.resolve("level.dat"), StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	private static String getDimensionFolder(ServerLevel level) {
		if (level.dimension() == Level.OVERWORLD) {
			return "";
		} else if (level.dimension() == Level.NETHER) {
			return "DIM-1";
		} else if (level.dimension() == Level.END) {
			return "DIM1";
		} else {
			return "dimensions/" + level.dimension().location().getNamespace() + "/" + level.dimension().location().getPath();
		}
	}

	private static void copyFolder(Path source, Path target, String folderName) throws IOException {
		Path sourceFolder = source.resolve(folderName);
		Path targetFolder = target.resolve(folderName);
		if (!Files.exists(sourceFolder))
			return;
		if (Files.exists(targetFolder)) {
			deleteDirectory(targetFolder);
		}
		copyDirectory(sourceFolder, targetFolder);
	}

	private static void copyDirectory(Path source, Path target) throws IOException {
		Files.createDirectories(target);
		try (Stream<Path> paths = Files.walk(source)) {
			paths.forEach(sourcePath -> {
				try {
					Path targetPath = target.resolve(source.relativize(sourcePath));
					if (Files.isDirectory(sourcePath)) {
						Files.createDirectories(targetPath);
					} else {
						Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	private static void deleteDirectory(Path path) throws IOException {
		if (!Files.exists(path))
			return;
		try (Stream<Path> paths = Files.walk(path)) {
			paths.sorted((p1, p2) -> -p1.compareTo(p2)).forEach(p -> {
				try {
					Files.delete(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static boolean hasMemorySnapshot() {
		return memorySnapshot != null;
	}

	public static boolean hasUniverseSnapshot() {
		return universeSnapshot != null;
	}

	static class WorldSnapshot {
		List<DimensionSnapshot> dimensions = new ArrayList<>();
		long dayTime;
		long gameTime;
		long timestamp;
	}

	static class DimensionSnapshot {
		String dimensionKey;
		String dimensionType;
		List<ChunkSnapshot> chunks = new ArrayList<>();
		List<CompoundTag> entities = new ArrayList<>();
		List<PlayerSnapshot> players = new ArrayList<>();
	}

	static class ChunkSnapshot {
		int chunkX;
		int chunkZ;
		List<BlockSnapshot> blocks = new ArrayList<>();
	}

	static class BlockSnapshot {
		int x, y, z;
		BlockState state;
		CompoundTag blockEntityData;
	}

	static class PlayerSnapshot {
		CompoundTag playerData;
		Vec3 position;
		String uuid;
		String dimension;
	}
}
