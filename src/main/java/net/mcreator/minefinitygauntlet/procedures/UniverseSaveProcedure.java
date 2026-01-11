package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class UniverseSaveProcedure {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		execute(event, event.getEntity().level(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Entity ent = null;
		if (!MinefinityGauntletModVariables.MapVariables.get(world).world_backup_taken) {
			InfinityWorldResetProcedure.createSnapshot(entity, true);
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(Component.literal("world backup taken!"), false);
			MinefinityGauntletModVariables.MapVariables.get(world).world_backup_taken = true;
			MinefinityGauntletModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
