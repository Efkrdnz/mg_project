
package net.mcreator.minefinitygauntlet.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.minefinitygauntlet.entity.FakeBlockEntityEntity;
import net.mcreator.minefinitygauntlet.client.model.Modelfakeblockentity;

public class FakeBlockEntityRenderer extends MobRenderer<FakeBlockEntityEntity, Modelfakeblockentity<FakeBlockEntityEntity>> {
	public FakeBlockEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelfakeblockentity(context.bakeLayer(Modelfakeblockentity.LAYER_LOCATION)), 1f);
	}

	@Override
	public ResourceLocation getTextureLocation(FakeBlockEntityEntity entity) {
		return ResourceLocation.parse("minefinity_gauntlet:textures/entities/fakeblockentity1.png");
	}
}
