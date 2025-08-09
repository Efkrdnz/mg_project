package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModMobEffects;

import javax.annotation.Nullable;

import java.util.List;
import java.util.Comparator;

@EventBusSubscriber
public class PowerLaunchLandingProcedure {
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
		if (!world.isClientSide()) {
			if (entity.getPersistentData().getBoolean("powerleap") == true) {
				entity.fallDistance = 0;
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.PURPLE_AURA_1.get()), (entity.getX()), (entity.getY() + entity.getBbHeight() / 2), (entity.getZ()), 3, 0.05, (entity.getBbHeight() / 1.75), 0.05, 0);
				if (entity.onGround()) {
					entity.getPersistentData().putBoolean("powerleap", false);
					if (world instanceof Level _level && !_level.isClientSide())
						_level.explode(entity, (entity.getX()), (entity.getY()), (entity.getZ()), 5, false, Level.ExplosionInteraction.NONE);
					{
						final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
						List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
						for (Entity entityiterator : _entfound) {
							if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
								_entity.addEffect(new MobEffectInstance(MinefinityGauntletModMobEffects.SCREEN_SHAKE, 20, 1, false, false));
						}
					}
					if (world instanceof ServerLevel _level)
						_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_1.get()), (entity.getX()), (entity.getY()), (entity.getZ()), 6, 2, 2, 2, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_2.get()), (entity.getX()), (entity.getY()), (entity.getZ()), 6, 2, 2, 2, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_3.get()), (entity.getX()), (entity.getY()), (entity.getZ()), 6, 2, 2, 2, 0);
					if (world instanceof ServerLevel _level)
						_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_4.get()), (entity.getX()), (entity.getY()), (entity.getZ()), 6, 2, 2, 2, 0);
				}
			}
		}
	}
}
