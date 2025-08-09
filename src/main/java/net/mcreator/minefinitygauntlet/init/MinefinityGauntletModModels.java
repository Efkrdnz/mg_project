
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minefinitygauntlet.client.model.Modelinv;
import net.mcreator.minefinitygauntlet.client.model.Modelfakeblockentity;
import net.mcreator.minefinitygauntlet.client.model.Modelbeam1;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class MinefinityGauntletModModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(Modelbeam1.LAYER_LOCATION, Modelbeam1::createBodyLayer);
		event.registerLayerDefinition(Modelinv.LAYER_LOCATION, Modelinv::createBodyLayer);
		event.registerLayerDefinition(Modelfakeblockentity.LAYER_LOCATION, Modelfakeblockentity::createBodyLayer);
	}
}
