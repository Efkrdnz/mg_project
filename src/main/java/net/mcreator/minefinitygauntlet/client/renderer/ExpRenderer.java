
package net.mcreator.minefinitygauntlet.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minefinitygauntlet.entity.ExpEntity;
import net.mcreator.minefinitygauntlet.client.model.Modelinv;

public class ExpRenderer extends MobRenderer<ExpEntity, Modelinv<ExpEntity>> {
	public ExpRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelinv(context.bakeLayer(Modelinv.LAYER_LOCATION)), 0.1f);
	}

	@Override
	public ResourceLocation getTextureLocation(ExpEntity entity) {
		return ResourceLocation.parse("minefinity_gauntlet:textures/entities/inv.png");
	}
}
