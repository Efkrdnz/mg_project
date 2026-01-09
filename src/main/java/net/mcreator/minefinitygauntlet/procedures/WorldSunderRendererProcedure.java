package net.mcreator.minefinitygauntlet.procedures;

import org.joml.Matrix4f;

import org.checkerframework.checker.units.qual.g;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Iterator;

import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
public class WorldSunderRendererProcedure {
	private static VertexBuffer VOID_ENERGY_VB = null;
	private static final int SIZE = 12;
	private static long lastMeshRebuild = 0;
	private static final long MESH_REBUILD_INTERVAL = 3;
	public static final Map<BlockPos, Float> CLIENT_VOID_BLOCKS = new ConcurrentHashMap<>();
	private static final ResourceLocation VOID_TEXTURE = ResourceLocation.parse("minecraft:textures/block/soul_sand.png");

	@SubscribeEvent
	public static void onRenderLevel(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES)
			return;
		Minecraft mc = Minecraft.getInstance();
		ClientLevel level = mc.level;
		if (level == null)
			return;
		if (CLIENT_VOID_BLOCKS.isEmpty())
			return;
		Vec3 cam = event.getCamera().getPosition();
		BlockPos camPos = BlockPos.containing(cam.x, cam.y, cam.z);
		double partial = event.getPartialTick().getGameTimeDeltaPartialTick(false);
		double time = level.getGameTime() + partial;
		PoseStack poseStack = event.getPoseStack();
		Matrix4f modelViewMatrix = event.getModelViewMatrix();
		Matrix4f projectionMatrix = event.getProjectionMatrix();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableDepthTest();
		RenderSystem.setShaderTexture(0, VOID_TEXTURE);
		boolean shouldRebuildMesh = (level.getGameTime() - lastMeshRebuild) >= MESH_REBUILD_INTERVAL;
		if (shouldRebuildMesh) {
			lastMeshRebuild = level.getGameTime();
			buildVoidEnergyMesh(0, time, 1.0f);
		}
		java.util.List<BlockRenderData> closeBlocks = new java.util.ArrayList<>();
		java.util.List<BlockRenderData> mediumBlocks = new java.util.ArrayList<>();
		java.util.List<BlockRenderData> farBlocks = new java.util.ArrayList<>();
		Iterator<Map.Entry<BlockPos, Float>> iterator = CLIENT_VOID_BLOCKS.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<BlockPos, Float> entry = iterator.next();
			BlockPos pos = entry.getKey();
			float intensity = entry.getValue();
			float newIntensity = intensity - 0.008f;
			if (newIntensity <= 0.0f) {
				iterator.remove();
				continue;
			}
			entry.setValue(newIntensity);
			double distSq = pos.distSqr(camPos);
			if (distSq > 150 * 150)
				continue;
			if (!level.isLoaded(pos))
				continue;
			BlockRenderData data = new BlockRenderData(pos, newIntensity, distSq);
			if (distSq < 30 * 30) {
				closeBlocks.add(data);
			} else if (distSq < 80 * 80) {
				mediumBlocks.add(data);
			} else {
				farBlocks.add(data);
			}
		}
		if (VOID_ENERGY_VB == null) {
			RenderSystem.enableDepthTest();
			RenderSystem.disableBlend();
			return;
		}
		// render close blocks - only exposed faces
		for (BlockRenderData data : closeBlocks) {
			double cx = data.pos.getX() + 0.5;
			double cy = data.pos.getY() + 0.5;
			double cz = data.pos.getZ() + 0.5;
			// OPTIMIZED: Only render faces that are exposed to air
			for (Direction face : Direction.values()) {
				BlockPos neighborPos = data.pos.relative(face);
				if (level.isLoaded(neighborPos) && level.isEmptyBlock(neighborPos)) {
					renderVoidOnFace(VOID_ENERGY_VB, poseStack, modelViewMatrix, projectionMatrix, cx, cy, cz, face, time, data.pos.asLong(), data.intensity);
				}
			}
		}
		// render medium distance blocks - only top face and one visible side
		for (BlockRenderData data : mediumBlocks) {
			double cx = data.pos.getX() + 0.5;
			double cy = data.pos.getY() + 0.5;
			double cz = data.pos.getZ() + 0.5;
			// always render top if exposed
			BlockPos topNeighbor = data.pos.relative(Direction.UP);
			if (level.isLoaded(topNeighbor) && level.isEmptyBlock(topNeighbor)) {
				renderVoidOnFace(VOID_ENERGY_VB, poseStack, modelViewMatrix, projectionMatrix, cx, cy, cz, Direction.UP, time, data.pos.asLong(), data.intensity);
			}
			// render one side facing camera if exposed
			Vec3 viewDir = cam.subtract(cx, cy, cz).normalize();
			Direction cameraSide = viewDir.x > 0 ? Direction.WEST : Direction.EAST;
			BlockPos sideNeighbor = data.pos.relative(cameraSide);
			if (level.isLoaded(sideNeighbor) && level.isEmptyBlock(sideNeighbor)) {
				renderVoidOnFace(VOID_ENERGY_VB, poseStack, modelViewMatrix, projectionMatrix, cx, cy, cz, cameraSide, time, data.pos.asLong(), data.intensity);
			}
		}
		// render far blocks - only top face if exposed
		for (BlockRenderData data : farBlocks) {
			double cx = data.pos.getX() + 0.5;
			double cy = data.pos.getY() + 0.5;
			double cz = data.pos.getZ() + 0.5;
			BlockPos topNeighbor = data.pos.relative(Direction.UP);
			if (level.isLoaded(topNeighbor) && level.isEmptyBlock(topNeighbor)) {
				renderVoidOnFace(VOID_ENERGY_VB, poseStack, modelViewMatrix, projectionMatrix, cx, cy, cz, Direction.UP, time, data.pos.asLong(), data.intensity);
			}
		}
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
	}

	private static void buildVoidEnergyMesh(long seed, double time, float intensity) {
		BufferBuilder bb = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		float step = 1.0f / (float) SIZE;
		double speed = 0.08;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				float y1 = 0.5f - row * step;
				float y0 = y1 - step;
				float x0 = -0.5f + col * step;
				float x1 = x0 + step;
				float z = 0.0f;
				double uvOffset = time * speed;
				float u0 = (float) ((col + uvOffset) / SIZE);
				float u1 = (float) ((col + 1 + uvOffset) / SIZE);
				float v0 = (float) ((row + uvOffset * 0.7) / SIZE);
				float v1 = (float) ((row + 1 + uvOffset * 0.7) / SIZE);
				long hash = seed ^ (row * 7349L) ^ (col * 9151L);
				double noise = ((hash & 0xFF) / 255.0);
				int r = (int) (100 * (0.7 + noise * 0.6));
				int g = (int) (20 * (0.7 + noise * 0.6));
				int b = (int) (140 * (0.7 + noise * 0.6));
				int a = 255;
				int color = (a << 24) | (r << 16) | (g << 8) | b;
				bb.addVertex(x0, y0, z).setUv(u0, v1).setColor(color);
				bb.addVertex(x1, y0, z).setUv(u1, v1).setColor(color);
				bb.addVertex(x1, y1, z).setUv(u1, v0).setColor(color);
				bb.addVertex(x0, y1, z).setUv(u0, v0).setColor(color);
			}
		}
		MeshData meshData = bb.build();
		if (meshData == null)
			return;
		if (VOID_ENERGY_VB == null) {
			VOID_ENERGY_VB = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
		}
		VOID_ENERGY_VB.bind();
		VOID_ENERGY_VB.upload(meshData);
		VertexBuffer.unbind();
	}

	private static void renderVoidOnFace(VertexBuffer vb, PoseStack poseStack, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, double cx, double cy, double cz, Direction face, double time, long seed, float intensity) {
		if (vb == null || intensity <= 0.0f)
			return;
		Minecraft mc = Minecraft.getInstance();
		Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();
		float dx = (float) (cx - camPos.x());
		float dy = (float) (cy - camPos.y());
		float dz = (float) (cz - camPos.z());
		double phase = (seed & 0xFFFF) * 0.001 + face.ordinal();
		double wobble = 0.02 * Math.sin(time * 0.15 + phase);
		float offset = 0.501f + (float) wobble;
		poseStack.pushPose();
		poseStack.mulPose(modelViewMatrix);
		poseStack.translate(dx, dy, dz);
		switch (face) {
			case SOUTH -> poseStack.translate(0.0f, 0.0f, offset);
			case NORTH -> {
				poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
				poseStack.translate(0.0f, 0.0f, offset);
			}
			case EAST -> {
				poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
				poseStack.translate(0.0f, 0.0f, offset);
			}
			case WEST -> {
				poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
				poseStack.translate(0.0f, 0.0f, offset);
			}
			case UP -> {
				poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
				poseStack.translate(0.0f, 0.0f, offset);
			}
			case DOWN -> {
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
				poseStack.translate(0.0f, 0.0f, offset);
			}
		}
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, intensity);
		vb.bind();
		vb.drawWithShader(poseStack.last().pose(), projectionMatrix, net.minecraft.client.renderer.GameRenderer.getPositionTexColorShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		poseStack.popPose();
	}

	private static class BlockRenderData {
		BlockPos pos;
		float intensity;
		double distanceSq;

		BlockRenderData(BlockPos pos, float intensity, double distanceSq) {
			this.pos = pos;
			this.intensity = intensity;
			this.distanceSq = distanceSq;
		}
	}
}
