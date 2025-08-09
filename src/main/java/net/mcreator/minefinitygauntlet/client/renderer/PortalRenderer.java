
package net.mcreator.minefinitygauntlet.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minefinitygauntlet.entity.PortalEntity;
import net.mcreator.minefinitygauntlet.client.model.Modelinv;

public class PortalRenderer extends MobRenderer<PortalEntity, Modelinv<PortalEntity>> {
	public PortalRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelinv(context.bakeLayer(Modelinv.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(PortalEntity entity) {
		return ResourceLocation.parse("minefinity_gauntlet:textures/entities/inv.png");
	}
}
