package net.mcreator.minefinitygauntlet.client.renderer;

import org.checkerframework.checker.units.qual.m;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.ChickenModel;

import net.mcreator.minefinitygauntlet.entity.BlackholeEntity;

public class BlackholeRenderer extends MobRenderer<BlackholeEntity, ChickenModel<BlackholeEntity>> {
	public BlackholeRenderer(EntityRendererProvider.Context context) {
		super(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0f);
	}

	// renders the blackhole with gravitational lensing shader effect
	@Override
	public void render(BlackholeEntity entity, float entityYaw, float partialTick, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight) {
		if (net.mcreator.minefinitygauntlet.client.BlackHoleClientShader.BLACKHOLE_RENDER_TYPE == null)
			return;
		float timeSeconds = (entity.tickCount + partialTick) / 20.0f;
		if (!net.mcreator.minefinitygauntlet.client.BlackHoleClientShader.beginFrameCapture(timeSeconds))
			return;
		poseStack.pushPose();
		poseStack.translate(0.0, entity.getBbHeight() * 0.5, 0.0);
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		// size scales continuously with no limit
		int sizeData = entity.getEntityData().get(BlackholeEntity.DATA_size);
		float size = 0.5f + (sizeData * 0.115f);
		poseStack.scale(size, size, size);
		com.mojang.blaze3d.vertex.VertexConsumer vc = bufferSource.getBuffer(net.mcreator.minefinitygauntlet.client.BlackHoleClientShader.BLACKHOLE_RENDER_TYPE);
		org.joml.Matrix4f m = poseStack.last().pose();
		vc.addVertex(m, -1.0f, -1.0f, 0.0f).setUv(0.0f, 1.0f);
		vc.addVertex(m, 1.0f, -1.0f, 0.0f).setUv(1.0f, 1.0f);
		vc.addVertex(m, 1.0f, 1.0f, 0.0f).setUv(1.0f, 0.0f);
		vc.addVertex(m, -1.0f, 1.0f, 0.0f).setUv(0.0f, 0.0f);
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(BlackholeEntity entity) {
		return ResourceLocation.parse("minefinity_gauntlet:textures/entities/inv.png");
	}
}
