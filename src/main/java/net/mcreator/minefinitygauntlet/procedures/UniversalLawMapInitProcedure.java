package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.StringTag;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import javax.annotation.Nullable;

@EventBusSubscriber
public class UniversalLawMapInitProcedure {
	@SubscribeEvent
	public static void onWorldLoad(net.neoforged.neoforge.event.level.LevelEvent.Load event) {
		execute(event, event.getLevel());
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		if (MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.isEmpty()) {
			MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.put("law1", StringTag.valueOf("empty"));
			MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.put("law2", StringTag.valueOf("empty"));
			MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.put("law3", StringTag.valueOf("empty"));
			MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.put("law4", StringTag.valueOf("empty"));
			MinefinityGauntletModVariables.WorldVariables.get(world).universal_laws.put("law5", StringTag.valueOf("empty"));
			MinefinityGauntletModVariables.WorldVariables.get(world).syncData(world);
		}
	}
}
