package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;

public class SoulCaptureSystemProcedure {
	// captures entity and stores all data
	public static void captureEntity(Entity sourceEntity, Entity targetEntity) {
		if (!(sourceEntity instanceof ServerPlayer player))
			return;
		if (targetEntity instanceof Player || !targetEntity.isAlive())
			return;
		ServerLevel level = (ServerLevel) player.level();
		CompoundTag entityData = new CompoundTag();
		targetEntity.save(entityData);
		CompoundTag playerData = player.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		capturedList.add(entityData);
		playerData.put("SoulStoneCaptured", capturedList);
		// mark data as dirty to sync to client
		player.onUpdateAbilities();
		// properly remove entity
		targetEntity.discard();
		// effects
		level.sendParticles(ParticleTypes.SOUL, targetEntity.getX(), targetEntity.getY() + 1, targetEntity.getZ(), 30, 0.5, 0.5, 0.5, 0.1);
		player.displayClientMessage(Component.literal("Entity captured! Total: " + getCapturedCount(player)), true);
	}

	// releases newest entity
	public static void releaseNewest(Entity sourceEntity) {
		if (!(sourceEntity instanceof ServerPlayer player))
			return;
		ServerLevel level = (ServerLevel) player.level();
		CompoundTag playerData = player.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		if (capturedList.isEmpty()) {
			player.displayClientMessage(Component.literal("No captured entities!"), true);
			return;
		}
		int index = capturedList.size() - 1;
		CompoundTag entityData = capturedList.getCompound(index);
		EntityType<?> type = EntityType.by(entityData).orElse(null);
		if (type == null)
			return;
		Entity entity = type.create(level);
		if (entity != null) {
			// calculate spawn position
			double x = player.getX() + player.getLookAngle().x * 3;
			double y = player.getY();
			double z = player.getZ() + player.getLookAngle().z * 3;
			// load all saved data
			entity.load(entityData);
			// override position with new spawn location
			entity.absMoveTo(x, y, z, player.getYRot(), 0);
			// spawn entity
			level.addFreshEntity(entity);
			// remove from list
			capturedList.remove(index);
			playerData.put("SoulStoneCaptured", capturedList);
			// mark data as dirty to sync to client
			player.onUpdateAbilities();
			// effects
			level.sendParticles(ParticleTypes.SOUL, x, y + 1, z, 30, 0.5, 0.5, 0.5, 0.1);
			player.displayClientMessage(Component.literal("Entity released! Remaining: " + getCapturedCount(player)), true);
		}
	}

	// releases specific entity by index
	public static void releaseAtIndex(Entity sourceEntity, double indexNum) {
		if (!(sourceEntity instanceof ServerPlayer player))
			return;
		ServerLevel level = (ServerLevel) player.level();
		int index = (int) indexNum;
		CompoundTag playerData = player.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		if (index < 0 || index >= capturedList.size()) {
			return;
		}
		CompoundTag entityData = capturedList.getCompound(index);
		EntityType<?> type = EntityType.by(entityData).orElse(null);
		if (type == null) {
			return;
		}
		Entity entity = type.create(level);
		if (entity != null) {
			// calculate spawn position
			double x = player.getX() + player.getLookAngle().x * 3;
			double y = player.getY();
			double z = player.getZ() + player.getLookAngle().z * 3;
			// load all saved data
			entity.load(entityData);
			// override position with new spawn location
			entity.absMoveTo(x, y, z, player.getYRot(), 0);
			// spawn entity
			level.addFreshEntity(entity);
			// remove from list
			capturedList.remove(index);
			playerData.put("SoulStoneCaptured", capturedList);
			// mark data as dirty to sync to client
			player.onUpdateAbilities();
			// effects
			level.sendParticles(ParticleTypes.SOUL, x, y + 1, z, 30, 0.5, 0.5, 0.5, 0.1);
			player.displayClientMessage(Component.literal("Entity released!"), true);
		}
	}

	// gets count of captured entities
	public static double getCapturedCount(Entity sourceEntity) {
		if (!(sourceEntity instanceof Player player))
			return 0;
		// try to get from persistent data
		CompoundTag playerData = player.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		return capturedList.size();
	}

	// gets entity name for display - works on both client and server
	public static String getEntityName(Entity sourceEntity, double indexNum) {
		if (!(sourceEntity instanceof Player player))
			return "";
		int index = (int) indexNum;
		// get data from persistent storage
		CompoundTag playerData = player.getPersistentData();
		ListTag capturedList = playerData.getList("SoulStoneCaptured", Tag.TAG_COMPOUND);
		if (index < 0 || index >= capturedList.size())
			return "";
		try {
			CompoundTag entityData = capturedList.getCompound(index);
			String entityId = entityData.getString("id");
			if (entityId == null || entityId.isEmpty()) {
				return "";
			}
			EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entityId));
			if (type != null) {
				String name = type.getDescription().getString();
				return name != null ? name : "";
			}
		} catch (Exception e) {
			// silently fail and return empty
			return "";
		}
		return "";
	}
}
