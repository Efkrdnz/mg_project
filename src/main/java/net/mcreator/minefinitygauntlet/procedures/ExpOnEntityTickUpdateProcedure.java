package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModMobEffects;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

import java.util.List;
import java.util.Comparator;

public class ExpOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double rand = 0;
		double rand2 = 0;
		entity.getPersistentData().putDouble("life", (entity.getPersistentData().getDouble("life") + 1));
		if (entity.getPersistentData().getDouble("life") >= 41) {
			if (!entity.level().isClientSide())
				entity.discard();
		}
		if (entity.getPersistentData().getDouble("life") % 4 == 0) {
			entity.getPersistentData().putDouble("rad", (entity.getPersistentData().getDouble("rad") + 1));
			int horizontalRadiusSphere = (int) (entity.getPersistentData().getDouble("rad")) - 1;
			int verticalRadiusSphere = (int) (entity.getPersistentData().getDouble("rad")) - 1;
			int yIterationsSphere = verticalRadiusSphere;
			for (int i = -yIterationsSphere; i <= yIterationsSphere; i++) {
				for (int xi = -horizontalRadiusSphere; xi <= horizontalRadiusSphere; xi++) {
					for (int zi = -horizontalRadiusSphere; zi <= horizontalRadiusSphere; zi++) {
						double distanceSq = (xi * xi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere) + (i * i) / (double) (verticalRadiusSphere * verticalRadiusSphere)
								+ (zi * zi) / (double) (horizontalRadiusSphere * horizontalRadiusSphere);
						if (distanceSq <= 1.0) {
							if (world.getBlockState(BlockPos.containing(x + xi, y + i, z + zi)).getDestroySpeed(world, BlockPos.containing(x + xi, y + i, z + zi)) != -1) {
								rand = Mth.nextInt(RandomSource.create(), 1, 4);
								rand2 = Mth.nextInt(RandomSource.create(), 1, 4);
								if (rand2 == 1) {
									if (world instanceof ServerLevel _level)
										_level.sendParticles(ParticleTypes.EXPLOSION, x + xi, y + i, z + zi, 1, 2, 2, 2, 0);
									if (world instanceof Level _level) {
										if (!_level.isClientSide()) {
											_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.NEUTRAL, 5, 1);
										} else {
											_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.NEUTRAL, 5, 1, false);
										}
									}
								}
								if (rand == 1) {
									if (world instanceof ServerLevel _level)
										_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_1.get()), x + xi, y + i, z + zi, 2, 0.2, 0.2, 0.2, 0);
								}
								if (rand == 2) {
									if (world instanceof ServerLevel _level)
										_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_2.get()), x + xi, y + i, z + zi, 2, 0.2, 0.2, 0.2, 0);
								}
								if (rand == 3) {
									if (world instanceof ServerLevel _level)
										_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_3.get()), x + xi, y + i, z + zi, 2, 0.2, 0.2, 0.2, 0);
								}
								if (rand == 4) {
									if (world instanceof ServerLevel _level)
										_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.POWER_EXP_4.get()), x + xi, y + i, z + zi, 2, 0.2, 0.2, 0.2, 0);
								}
								{
									final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
									List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(35 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
											.toList();
									for (Entity entityiterator : _entfound) {
										if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
											_entity.addEffect(new MobEffectInstance(MinefinityGauntletModMobEffects.SCREEN_SHAKE, 10, 1, false, false));
									}
								}
								{
									final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
									List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate((entity.getPersistentData().getDouble("rad") * 2) / 2d), e -> true).stream()
											.sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
									for (Entity entityiterator : _entfound) {
										if (!(entityiterator instanceof Player _playerHasItem ? _playerHasItem.getInventory().contains(new ItemStack(MinefinityGauntletModItems.TEST_ITEM.get())) : false) && !(entityiterator == entity)
												&& entityiterator instanceof LivingEntity) {
											if (entityiterator instanceof LivingEntity livingTarget) {
												livingTarget.invulnerableTime = 0; // Reset hurt cooldown
											}
											entityiterator.hurt(new DamageSource(world.holderOrThrow(DamageTypes.GENERIC)), 10);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
