package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;

import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Files;

import java.io.IOException;
import java.io.FileOutputStream;

public class InfinityWorldResetProcedure {
	private static WorldSnapshot memorySnapshot = null;
	private static final String BACKUP_FOLDER = "infinity_gauntlet_backup";

	// creates both memory and disk backup
	public static void createSnapshot(Entity entity, boolean toDisk) {
		if (!(entity.level() instanceof ServerLevel level))
			return;
		MinecraftServer server = level.getServer();
		if (server == null)
			return;
		server.execute(() -> {
			try {
				WorldSnapshot snapshot = new WorldSnapshot();
				// capture all dimensions
				for (ServerLevel dimension : server.getAllLevels()) {
					dimension.save(null, true, dimension.noSave);
					DimensionSnapshot dimSnapshot = new DimensionSnapshot();
					dimSnapshot.dimensionKey = dimension.dimension().location().toString();
					dimSnapshot.dimensionType = getDimensionFolder(dimension);
					// capture all entities except players
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
					// capture players
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
				// save world data
				snapshot.dayTime = level.getDayTime();
				snapshot.gameTime = level.getGameTime();
				snapshot.timestamp = System.currentTimeMillis();
				// store in memory
				memorySnapshot = snapshot;
				// save to disk if requested
				if (toDisk) {
					saveToDisk(server, snapshot);
				}
				int totalEntities = snapshot.dimensions.stream().mapToInt(d -> d.entities.size()).sum();
				if (entity instanceof ServerPlayer player) {
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§6§l[INFINITY GAUNTLET] §eReality Snapshot Created\n" + "§7Entities: " + totalEntities + (toDisk ? " | §aSaved to disk" : " | §bIn memory only")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// restores from memory or disk
	public static void restoreSnapshot(Entity entity, boolean fromDisk) {
		if (!(entity.level() instanceof ServerLevel level))
			return;
		MinecraftServer server = level.getServer();
		if (server == null)
			return;
		server.execute(() -> {
			try {
				WorldSnapshot snapshot = memorySnapshot;
				// load from disk if requested
				if (fromDisk) {
					boolean success = loadFromDisk(server);
					if (!success) {
						if (entity instanceof ServerPlayer player) {
							player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c§l[INFINITY GAUNTLET] No disk backup found!"));
						}
						return;
					}
					// notify about disk restore
					server.getPlayerList().getPlayers().forEach(player -> {
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§5§l✦ UNIVERSE RESET FROM DISK - RECONNECT NOW ✦"));
					});
					return;
				}
				// restore from memory
				if (snapshot == null) {
					if (entity instanceof ServerPlayer player) {
						player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c§l[INFINITY GAUNTLET] No memory snapshot found!"));
					}
					return;
				}
				// warn all players
				server.getPlayerList().getPlayers().forEach(player -> {
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c§l⚠ REALITY RESET IN PROGRESS ⚠"));
				});
				// restore each dimension
				for (DimensionSnapshot dimSnapshot : snapshot.dimensions) {
					ServerLevel dimension = StreamSupport.stream(server.getAllLevels().spliterator(), false).filter(lvl -> lvl.dimension().location().toString().equals(dimSnapshot.dimensionKey)).findFirst().orElse(null);
					if (dimension == null)
						continue;
					// remove all entities except players
					List<Entity> toRemove = new ArrayList<>();
					dimension.getAllEntities().forEach(ent -> {
						if (!(ent instanceof ServerPlayer)) {
							toRemove.add(ent);
						}
					});
					toRemove.forEach(Entity::discard);
					// restore entities
					dimSnapshot.entities.forEach(entityTag -> {
						try {
							// get entity type
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
					// restore players
					dimSnapshot.players.forEach(playerSnap -> {
						ServerPlayer player = server.getPlayerList().getPlayers().stream().filter(p -> p.getStringUUID().equals(playerSnap.uuid)).findFirst().orElse(null);
						if (player != null) {
							player.load(playerSnap.playerData);
							// get target dimension
							ServerLevel targetDim = StreamSupport.stream(server.getAllLevels().spliterator(), false).filter(lvl -> lvl.dimension().location().toString().equals(playerSnap.dimension)).findFirst().orElse(dimension);
							player.teleportTo(targetDim, playerSnap.position.x, playerSnap.position.y, playerSnap.position.z, player.getYRot(), player.getXRot());
						}
					});
				}
				// restore time
				level.setDayTime(snapshot.dayTime);
				// final notification
				server.getPlayerList().getPlayers().forEach(player -> {
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§5§l✦ REALITY HAS BEEN REWRITTEN ✦"));
				});
			} catch (Exception e) {
				e.printStackTrace();
				if (entity instanceof ServerPlayer player) {
					player.sendSystemMessage(net.minecraft.network.chat.Component.literal("§c§lRestore failed: " + e.getMessage()));
				}
			}
		});
	}

	// saves snapshot to disk using region files
	private static void saveToDisk(MinecraftServer server, WorldSnapshot snapshot) throws IOException {
		Path worldPath = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
		Path backupPath = worldPath.getParent().resolve(BACKUP_FOLDER);
		// create backup folder
		Files.createDirectories(backupPath);
		// save snapshot metadata
		CompoundTag metaTag = new CompoundTag();
		metaTag.putLong("timestamp", snapshot.timestamp);
		metaTag.putLong("dayTime", snapshot.dayTime);
		metaTag.putLong("gameTime", snapshot.gameTime);
		// save entity data
		ListTag dimensionsTag = new ListTag();
		for (DimensionSnapshot dim : snapshot.dimensions) {
			CompoundTag dimTag = new CompoundTag();
			dimTag.putString("key", dim.dimensionKey);
			ListTag entitiesTag = new ListTag();
			for (CompoundTag entityTag : dim.entities) {
				entitiesTag.add(entityTag);
			}
			dimTag.put("entities", entitiesTag);
			ListTag playersTag = new ListTag();
			for (PlayerSnapshot ps : dim.players) {
				CompoundTag playerTag = new CompoundTag();
				playerTag.put("data", ps.playerData);
				playerTag.putString("uuid", ps.uuid);
				playerTag.putDouble("x", ps.position.x);
				playerTag.putDouble("y", ps.position.y);
				playerTag.putDouble("z", ps.position.z);
				playerTag.putString("dimension", ps.dimension);
				playersTag.add(playerTag);
			}
			dimTag.put("players", playersTag);
			dimensionsTag.add(dimTag);
		}
		metaTag.put("dimensions", dimensionsTag);
		// use FileOutputStream for NbtIo.writeCompressed
		try (FileOutputStream fos = new FileOutputStream(backupPath.resolve("snapshot_meta.dat").toFile())) {
			NbtIo.writeCompressed(metaTag, fos);
		}
		// backup each dimension's region files
		for (ServerLevel dimension : server.getAllLevels()) {
			String dimFolder = getDimensionFolder(dimension);
			Path sourcePath = worldPath.resolve(dimFolder);
			Path targetPath = backupPath.resolve(dimFolder);
			// copy region, entities, and poi folders
			copyFolder(sourcePath, targetPath, "region");
			copyFolder(sourcePath, targetPath, "entities");
			copyFolder(sourcePath, targetPath, "poi");
			// copy level.dat if overworld
			if (dimFolder.equals("")) {
				Path levelDat = sourcePath.resolve("level.dat");
				if (Files.exists(levelDat)) {
					Files.copy(levelDat, targetPath.resolve("level.dat"), StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	// loads snapshot from disk
	private static boolean loadFromDisk(MinecraftServer server) {
		try {
			Path worldPath = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
			Path backupPath = worldPath.getParent().resolve(BACKUP_FOLDER);
			if (!Files.exists(backupPath))
				return false;
			// restore region files for each dimension
			for (ServerLevel dimension : server.getAllLevels()) {
				String dimFolder = getDimensionFolder(dimension);
				Path sourcePath = backupPath.resolve(dimFolder);
				Path targetPath = worldPath.resolve(dimFolder);
				if (!Files.exists(sourcePath))
					continue;
				// restore region, entities, and poi folders
				restoreFolder(sourcePath, targetPath, "region");
				restoreFolder(sourcePath, targetPath, "entities");
				restoreFolder(sourcePath, targetPath, "poi");
			}
			// force save and reload
			for (ServerLevel dimension : server.getAllLevels()) {
				dimension.getChunkSource().save(true);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// get dimension folder name
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

	// copy a specific folder
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

	// restore a specific folder
	private static void restoreFolder(Path source, Path target, String folderName) throws IOException {
		Path sourceFolder = source.resolve(folderName);
		Path targetFolder = target.resolve(folderName);
		if (!Files.exists(sourceFolder))
			return;
		if (Files.exists(targetFolder)) {
			deleteDirectory(targetFolder);
		}
		copyDirectory(sourceFolder, targetFolder);
	}

	// copy directory recursively
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

	// delete directory recursively
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

	// check if memory snapshot exists
	public static boolean hasMemorySnapshot() {
		return memorySnapshot != null;
	}

	// data classes
	static class WorldSnapshot {
		List<DimensionSnapshot> dimensions = new ArrayList<>();
		long dayTime;
		long gameTime;
		long timestamp;
	}

	static class DimensionSnapshot {
		String dimensionKey;
		String dimensionType;
		List<CompoundTag> entities = new ArrayList<>();
		List<PlayerSnapshot> players = new ArrayList<>();
	}

	static class PlayerSnapshot {
		CompoundTag playerData;
		Vec3 position;
		String uuid;
		String dimension;
	}
}
