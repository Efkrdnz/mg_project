package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class SlowTimeProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
	}

	public static void execute(LevelAccessor world, double x, double y, double z) {
		execute(null, world, x, y, z);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z) {
		if (MinefinityGauntletModVariables.WorldVariables.get(world).TimeSlow) {
			new Object() {
				void timedLoop(int timedloopiterator, int timedlooptotal, int ticks) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"tick rate 5");
					final int tick2 = ticks;
					MinefinityGauntletMod.queueServerWork(tick2, () -> {
						if (timedlooptotal > timedloopiterator + 1) {
							timedLoop(timedloopiterator + 1, timedlooptotal, tick2);
						}
					});
				}
			}.timedLoop(0, 15, 1);
		} else {
			new Object() {
				void timedLoop(int timedloopiterator, int timedlooptotal, int ticks) {
					if (world instanceof ServerLevel _level)
						_level.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", Component.literal(""), _level.getServer(), null).withSuppressedOutput(),
								"tick rate 20");
					final int tick2 = ticks;
					MinefinityGauntletMod.queueServerWork(tick2, () -> {
						if (timedlooptotal > timedloopiterator + 1) {
							timedLoop(timedloopiterator + 1, timedlooptotal, tick2);
						}
					});
				}
			}.timedLoop(0, 15, 1);
		}
	}
}
