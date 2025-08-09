
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

public class MinefinityGauntletModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, MinefinityGauntletMod.MODID);
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_EXP_1 = REGISTRY.register("power_exp_1", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_EXP_2 = REGISTRY.register("power_exp_2", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_EXP_3 = REGISTRY.register("power_exp_3", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> POWER_EXP_4 = REGISTRY.register("power_exp_4", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURPLE_AURA_1 = REGISTRY.register("purple_aura_1", () -> new SimpleParticleType(true));
	public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURPLE_BEAM_1 = REGISTRY.register("purple_beam_1", () -> new SimpleParticleType(true));
}
