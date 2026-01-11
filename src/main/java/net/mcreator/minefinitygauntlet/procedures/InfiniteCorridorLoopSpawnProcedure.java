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
public class InfiniteCorridorLoopSpawnProcedure {
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
		// only check every 20 ticks (once per second) to reduce load
		if (entity.tickCount % 20 != 0) {
			return;
		}
		// check if in corridor dimension
		if ((entity.level().dimension()) != ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minefinity_gauntlet:infinite_corridor"))) {
			return;
		}
		// get player X position
		double playerX = entity.getX();
		// get current loop count
		double currentLoopCount = MinefinityGauntletModVariables.WorldVariables.get(world).corridor_loop_count;
		// calculate where next loop should be
		double nextLoopX = 24 * (currentLoopCount + 1);
		// if player is within 64 blocks of where next loop should spawn
		if (playerX > (nextLoopX - 64)) {
			// check if structure already exists
			if ((world.getBlockState(BlockPos.containing(nextLoopX + 2, 63, 5))).isAir()) {
				// spawn the loop
				if (world instanceof ServerLevel _serverworld) {
					StructureTemplate template = _serverworld.getStructureManager().getOrCreate(ResourceLocation.fromNamespaceAndPath("minefinity_gauntlet", "infinite_corridor_loop"));
					if (template != null) {
						template.placeInWorld(_serverworld, BlockPos.containing(nextLoopX, 60, 0), BlockPos.containing(nextLoopX, 60, 0), new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false),
								_serverworld.random, 3);
						// increment counter
						MinefinityGauntletModVariables.WorldVariables.get(world).corridor_loop_count = currentLoopCount + 1;
						MinefinityGauntletModVariables.MapVariables.get(world).syncData(world);
					}
				}
			} else {
				// structure exists but counter wasn't updated - fix it
				MinefinityGauntletModVariables.WorldVariables.get(world).corridor_loop_count = currentLoopCount + 1;
				MinefinityGauntletModVariables.MapVariables.get(world).syncData(world);
			}
		}
	}
}
