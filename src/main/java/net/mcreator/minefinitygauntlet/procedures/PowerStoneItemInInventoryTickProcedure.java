package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModMobEffects;

public class PowerStoneItemInInventoryTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
			_entity.addEffect(new MobEffectInstance(MinefinityGauntletModMobEffects.DECAY, 5, 0));
		if (!(itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK)) != 0)) {
			itemstack.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.KNOCKBACK), 4);
		}
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MinefinityGauntletModParticleTypes.PURPLE_AURA_1.get()), x, y, z, 3, 1, 1, 1, 1);
	}
}
