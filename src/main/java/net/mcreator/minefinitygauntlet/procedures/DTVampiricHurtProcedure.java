package net.mcreator.minefinitygauntlet.procedures;

import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;

import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

import javax.annotation.Nullable;

@EventBusSubscriber
public class DTVampiricHurtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingDamageEvent.Post event) {
		if (event.getEntity() != null) {
			execute(event, event.getEntity().level(), event.getSource(), event.getEntity(), event.getSource().getEntity(), event.getOriginalDamage());
		}
	}

	public static void execute(LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		execute(null, world, damagesource, entity, sourceentity, amount);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity, double amount) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		double dmg = 0;
		dmg = amount;
		if (!world.isClientSide()) {
			if (damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("minefinity_gauntlet:vampiric")))) {
				entity.setDeltaMovement(new Vec3(0, 0, 0));
				if (sourceentity instanceof LivingEntity _entity)
					_entity.setHealth((float) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 0.5));
				MinefinityGauntletMod.queueServerWork(1, () -> {
					entity.setDeltaMovement(new Vec3(0, 0, 0));
				});
			}
		}
	}
}
