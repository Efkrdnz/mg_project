
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import net.mcreator.minefinitygauntlet.potion.ScreenShakeMobEffect;
import net.mcreator.minefinitygauntlet.potion.DecayMobEffect;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

public class MinefinityGauntletModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, MinefinityGauntletMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> SCREEN_SHAKE = REGISTRY.register("screen_shake", () -> new ScreenShakeMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> DECAY = REGISTRY.register("decay", () -> new DecayMobEffect());
}
