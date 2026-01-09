
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minefinitygauntlet.client.renderer.PortalRenderer;
import net.mcreator.minefinitygauntlet.client.renderer.InvRenderer;
import net.mcreator.minefinitygauntlet.client.renderer.HerePortalRenderer;
import net.mcreator.minefinitygauntlet.client.renderer.FakeBlockEntityRenderer;
import net.mcreator.minefinitygauntlet.client.renderer.ExpRenderer;
import net.mcreator.minefinitygauntlet.client.renderer.BlackholeRenderer;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MinefinityGauntletModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MinefinityGauntletModEntities.INV.get(), InvRenderer::new);
		event.registerEntityRenderer(MinefinityGauntletModEntities.EXP.get(), ExpRenderer::new);
		event.registerEntityRenderer(MinefinityGauntletModEntities.PORTAL.get(), PortalRenderer::new);
		event.registerEntityRenderer(MinefinityGauntletModEntities.FAKE_BLOCK_ENTITY.get(), FakeBlockEntityRenderer::new);
		event.registerEntityRenderer(MinefinityGauntletModEntities.HERE_PORTAL.get(), HerePortalRenderer::new);
		event.registerEntityRenderer(MinefinityGauntletModEntities.BLACKHOLE.get(), BlackholeRenderer::new);
	}
}
