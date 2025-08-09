
package net.mcreator.minefinitygauntlet.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minefinitygauntlet.entity.HerePortalEntity;
import net.mcreator.minefinitygauntlet.client.model.Modelinv;

public class HerePortalRenderer extends MobRenderer<HerePortalEntity, Modelinv<HerePortalEntity>> {
	public HerePortalRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelinv(context.bakeLayer(Modelinv.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(HerePortalEntity entity) {
		return ResourceLocation.parse("minefinity_gauntlet:textures/entities/inv.png");
	}
}
