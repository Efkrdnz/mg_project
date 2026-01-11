package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class InfiniteCorridorInitProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double spawnX = 0;
		double spawnY = 0;
		double spawnZ = 0;
		if ((entity.level().dimension()) == ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minefinity_gauntlet:infinite_corridor"))) {
			if (!MinefinityGauntletModVariables.WorldVariables.get(world).corridor_init) {
				spawnX = -1;
				spawnY = 60;
				spawnZ = 0;
				if (world instanceof ServerLevel _serverworld) {
					StructureTemplate template = _serverworld.getStructureManager().getOrCreate(ResourceLocation.fromNamespaceAndPath("minefinity_gauntlet", "infinite_corridor_start"));
					if (template != null) {
						template.placeInWorld(_serverworld, BlockPos.containing(spawnX, spawnY, spawnZ), BlockPos.containing(spawnX, spawnY, spawnZ),
								new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false), _serverworld.random, 3);
					}
				}
				MinefinityGauntletModVariables.WorldVariables.get(world).corridor_init = true;
				MinefinityGauntletModVariables.WorldVariables.get(world).syncData(world);
			}
		}
	}
}
