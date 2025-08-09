package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;

public class PowerStoneLivingEntityIsHitWithItemProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_2.get()), x, y, z, 5, 1, 1, 1, 1);
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_1.get()), x, y, z, 5, 1, 1, 1, 1);
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_3.get()), x, y, z, 5, 1, 1, 1, 1);
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_4.get()), x, y, z, 5, 1, 1, 1, 1);
		if (world instanceof Level _level) {
			if (!_level.isClientSide()) {
				_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.NEUTRAL, 2, 1);
			} else {
				_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.NEUTRAL, 2, 1, false);
			}
		}
	}
}
