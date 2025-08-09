package net.mcreator.minefinitygauntlet.procedures;

import org.joml.Matrix4f;

import org.checkerframework.checker.units.qual.m;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

@EventBusSubscriber(value = Dist.CLIENT)
public class So2VampiricBeamProcedure {
	private static BufferBuilder bufferBuilder = null;
	private static VertexBuffer vertexBuffer = null;
	private static VertexFormat.Mode mode = null;
	private static VertexFormat format = null;
	private static PoseStack poseStack = null;
	private static Matrix4f modelViewMatrix = null;
	private static Matrix4f projectionMatrix = null;
	private static boolean worldCoordinate = true;
	private static Vec3 offset = Vec3.ZERO;
	private static int currentStage = 0;
	private static int targetStage = 0; // NONE: 0, SKY: 1, WORLD: 2

	private static void add(float x, float y, float z, int color) {
		add(x, y, z, 0.0F, 0.0F, color);
	}

	private static void add(float x, float y, float z, float u, float v, int color) {
		if (bufferBuilder == null)
			return;
		if (format == DefaultVertexFormat.POSITION_COLOR) {
			bufferBuilder.addVertex(x, y, z).setColor(color);
		} else if (format == DefaultVertexFormat.POSITION_TEX_COLOR) {
			bufferBuilder.addVertex(x, y, z).setUv(u, v).setColor(color);
		}
	}

	private static boolean begin(VertexFormat.Mode mode, VertexFormat format, boolean update) {
		if (So2VampiricBeamProcedure.bufferBuilder == null) {
			if (update)
				clear();
			if (So2VampiricBeamProcedure.vertexBuffer == null) {
				if (format == DefaultVertexFormat.POSITION_COLOR) {
					So2VampiricBeamProcedure.mode = mode;
					So2VampiricBeamProcedure.format = format;
					So2VampiricBeamProcedure.bufferBuilder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION_COLOR);
					return true;
				} else if (format == DefaultVertexFormat.POSITION_TEX_COLOR) {
					So2VampiricBeamProcedure.mode = mode;
					So2VampiricBeamProcedure.format = format;
					So2VampiricBeamProcedure.bufferBuilder = Tesselator.getInstance().begin(mode, DefaultVertexFormat.POSITION_TEX_COLOR);
					return true;
				}
			}
		}
		return false;
	}

	private static void clear() {
		if (vertexBuffer != null) {
			vertexBuffer.close();
			vertexBuffer = null;
		}
	}

	private static void end() {
		if (bufferBuilder == null)
			return;
		if (vertexBuffer != null)
			vertexBuffer.close();
		MeshData meshData = bufferBuilder.build();
		if (meshData == null) {
			vertexBuffer = null;
			bufferBuilder = null;
		} else {
			vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			vertexBuffer.bind();
			vertexBuffer.upload(meshData);
			VertexBuffer.unbind();
			bufferBuilder = null;
		}
	}

	private static void offset(double x, double y, double z) {
		offset = new Vec3(x, y, z);
	}

	private static void release() {
		targetStage = 0;
	}

	private static VertexBuffer shape() {
		return vertexBuffer;
	}

	private static void system(boolean worldCoordinate) {
		So2VampiricBeamProcedure.worldCoordinate = worldCoordinate;
	}

	private static boolean target(int targetStage) {
		if (targetStage == currentStage) {
			So2VampiricBeamProcedure.targetStage = targetStage;
			return true;
		}
		return false;
	}

	private static void renderShape(VertexBuffer vertexBuffer, double x, double y, double z, float yaw, float pitch, float roll, float xScale, float yScale, float zScale, int color) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (poseStack == null || projectionMatrix == null)
			return;
		if (vertexBuffer == null)
			return;
		float i, j, k;
		if (worldCoordinate) {
			Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			i = (float) (x - pos.x());
			j = (float) (y - pos.y());
			k = (float) (z - pos.z());
		} else {
			i = (float) x;
			j = (float) y;
			k = (float) z;
		}
		poseStack.pushPose();
		poseStack.mulPose(modelViewMatrix);
		poseStack.translate(i, j, k);
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(xScale, yScale, zScale);
		poseStack.translate(offset.x(), offset.y(), offset.z());
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
		vertexBuffer.bind();
		vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader() : GameRenderer.getPositionColorShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void renderLevel(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
			currentStage = 1;
			poseStack = new PoseStack();
			RenderSystem.depthMask(false);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
			currentStage = 0;
		} else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			currentStage = 2;
			poseStack = event.getPoseStack();
			RenderSystem.depthMask(true);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
			currentStage = 0;
		}
	}

	private static void renderShapes(RenderLevelStageEvent event) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
		if (level != null && entity != null) {
			modelViewMatrix = event.getModelViewMatrix();
			projectionMatrix = event.getProjectionMatrix();
			Vec3 pos = entity.getPosition(event.getPartialTick().getGameTimeDeltaPartialTick(false));
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			execute(event, entity, event.getPartialTick().getGameTimeDeltaPartialTick(false), event.getRenderTick());
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableDepthTest();
		}
	}

	public static void execute(Entity entity, double partialTick, double ticks) {
		execute(null, entity, partialTick, ticks);
	}

	private static void execute(@Nullable Event event, Entity entity, double partialTick, double ticks) {
		if (entity == null)
			return;
		double width = 0;
		double length = 0;
		double i = 0;
		double j = 0;
		double pitch = 0;
		double k = 0;
		double sneak = 0;
		double l = 0;
		double m = 0;
		double yaw = 0;
		double speed = 0;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).beamSoul) {
			if (entity.isShiftKeyDown()) {
				sneak = 1.2;
			} else {
				sneak = 1.5;
			}
			if (target(2)) {
				yaw = entity.getYRot() * 0.0174533 + Math.toRadians(145);
				pitch = entity.getXRot() * 0.0174533;
				speed = 0.2;
				width = 0.1;
				length = 15;
				RenderSystem.setShaderTexture(0, ResourceLocation.parse(("minecraft" + ":textures/" + "entity/beacon_beam" + ".png")));
				if (begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR, (!Minecraft.getInstance().isPaused()))) {
					i = width / (-2);
					j = width / 2;
					k = length - 0.5;
					l = (ticks + partialTick) * speed;
					m = length + l;
					add((float) i, (float) i, (float) k, 0, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) i, (float) i, 0, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) i, (float) i, 1, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) i, (float) k, 1, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) j, (float) k, 0, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) j, (float) i, 0, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) j, (float) i, 1, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) j, (float) k, 1, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) i, (float) k, 0, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) i, (float) i, 0, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) j, (float) i, 1, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) j, (float) j, (float) k, 1, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) j, (float) k, 0, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) j, (float) i, 0, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) i, (float) i, 1, (float) m, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					add((float) i, (float) i, (float) k, 1, (float) l, 255 << 24 | 255 << 16 | 255 << 8 | 255);
					end();
				}
				renderShape(shape(), (entity.getX() + 0.2 * Math.cos(yaw) * Math.cos(pitch)), (entity.getY() + sneak + 0.2 * Math.sin((-1) * pitch)), (entity.getZ() + 0.2 * Math.sin(yaw) * Math.cos(pitch)), (float) (entity.getYRot() + 0.25),
						entity.getXRot(), 0, (float) (-1.6), (float) 1.6, 1, 32 << 24 | 255 << 16 | 172 << 8 | 28);
				renderShape(shape(), (entity.getX() + 0.2 * Math.cos(yaw) * Math.cos(pitch)), (entity.getY() + sneak + 0.2 * Math.sin((-1) * pitch)), (entity.getZ() + 0.2 * Math.sin(yaw) * Math.cos(pitch)), (float) (entity.getYRot() + 0.25),
						entity.getXRot(), 0, 1, 1, 1, 255 << 24 | 255 << 16 | 172 << 8 | 28);
				release();
			}
		}
	}
}
