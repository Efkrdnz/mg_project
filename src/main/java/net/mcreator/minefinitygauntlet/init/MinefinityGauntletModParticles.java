
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minefinitygauntlet.client.particle.PurpleBeam1Particle;
import net.mcreator.minefinitygauntlet.client.particle.PurpleAura1Particle;
import net.mcreator.minefinitygauntlet.client.particle.PowerExp4Particle;
import net.mcreator.minefinitygauntlet.client.particle.PowerExp3Particle;
import net.mcreator.minefinitygauntlet.client.particle.PowerExp2Particle;
import net.mcreator.minefinitygauntlet.client.particle.PowerExp1Particle;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MinefinityGauntletModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.POWER_EXP_1.get(), PowerExp1Particle::provider);
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.POWER_EXP_2.get(), PowerExp2Particle::provider);
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.POWER_EXP_3.get(), PowerExp3Particle::provider);
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.POWER_EXP_4.get(), PowerExp4Particle::provider);
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.PURPLE_AURA_1.get(), PurpleAura1Particle::provider);
		event.registerSpriteSet(MinefinityGauntletModParticleTypes.PURPLE_BEAM_1.get(), PurpleBeam1Particle::provider);
	}
}
