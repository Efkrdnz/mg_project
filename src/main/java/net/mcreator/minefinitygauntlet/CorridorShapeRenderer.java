package net.mcreator.minefinitygauntlet.client.renderer;

import org.joml.Vector3f;
import org.joml.Matrix4f;

import org.checkerframework.checker.units.qual.t;
import org.checkerframework.checker.units.qual.s;
import org.checkerframework.checker.units.qual.m;

import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.client.renderer.CorridorShapeRenderer;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(modid = "minefinity_gauntlet", value = Dist.CLIENT)
public class CorridorShapeRenderer {
	private static final List<RoomShape> shapes = new ArrayList<>();
	private static long lastUpdateTime = 0;
	private static final Random random = new Random();
	private static int lastKnownLoopCount = 0;

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
			return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null)
			return;
		String dimensionKey = mc.level.dimension().location().toString();
		if (!dimensionKey.equals("minefinity_gauntlet:infinite_corridor"))
			return;
		long currentTime = mc.level.getGameTime();
		if (currentTime - lastUpdateTime > 100) {
			updateShapes(mc.player.position(), mc.level);
			lastUpdateTime = currentTime;
		}
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		Vec3 cameraPos = event.getCamera().getPosition();
		poseStack.pushPose();
		poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		for (RoomShape shape : shapes) {
			if (shape.position.distanceTo(cameraPos) > 64)
				continue;
			renderShape(shape, poseStack, bufferSource, currentTime);
		}
		poseStack.popPose();
		bufferSource.endBatch();
	}

	private static void updateShapes(Vec3 playerPos, net.minecraft.world.level.Level level) {
		// get current loop count from global variable
		int currentLoopCount = (int) MinefinityGauntletModVariables.WorldVariables.get(level).corridor_loop_count;
		// if new loops spawned, generate shapes for them
		if (currentLoopCount > lastKnownLoopCount) {
			for (int loopNum = lastKnownLoopCount + 1; loopNum <= currentLoopCount; loopNum++) {
				generateShapesForLoop(loopNum);
			}
			lastKnownLoopCount = currentLoopCount;
		}
		// remove shapes that are too far behind player
		shapes.removeIf(shape -> shape.position.x < playerPos.x - 100);
	}

	private static void generateShapesForLoop(int loopNumber) {
		// calculate loop's world position
		double loopX = 24 * loopNumber;
		double loopY = 60;
		double loopZ = 0;
		// room offsets relative to loop origin
		double[][] roomOffsets = {{6.5, 2.5, 2.5}, // back left
				{6.5, 2.5, 13.5}, // back right
				{18.5, 2.5, 2.5}, // front left
				{18.5, 2.5, 13.5} // front right
		};
		// create shapes for each room
		for (double[] offset : roomOffsets) {
			Vec3 roomCenter = new Vec3(loopX + offset[0], loopY + offset[1], loopZ + offset[2]);
			// 30% chance to spawn multiple shapes
			if (random.nextFloat() < 0.3f) {
				// spawn 2-3 nested shapes with different colors
				int count = 2 + random.nextInt(2);
				for (int i = 0; i < count; i++) {
					shapes.add(new RoomShape(roomCenter, true)); // grouped shapes
				}
			} else {
				// single shape
				shapes.add(new RoomShape(roomCenter, false));
			}
		}
	}

	private static void generateShapesForStart() {
		double loopX = -1; // start structure position
		double loopY = 60;
		double loopZ = 0;
		// same room offsets (start structure has same layout, just +1 block longer)
		double[][] roomOffsets = {{6.5, 2.5, 2.5}, // back left
				{6.5, 2.5, 13.5}, // back right
				{18.5, 2.5, 2.5}, // front left
				{18.5, 2.5, 13.5} // front right
		};
		for (double[] offset : roomOffsets) {
			Vec3 roomCenter = new Vec3(loopX + offset[0], loopY + offset[1], loopZ + offset[2]);
			if (random.nextFloat() < 0.3f) {
				int count = 2 + random.nextInt(2);
				for (int i = 0; i < count; i++) {
					shapes.add(new RoomShape(roomCenter, true));
				}
			} else {
				shapes.add(new RoomShape(roomCenter, false));
			}
		}
	}

	private static void renderShape(RoomShape shape, PoseStack poseStack, MultiBufferSource bufferSource, long time) {
		float t = (time + shape.timeOffset) * 0.05f;
		poseStack.pushPose();
		poseStack.translate(shape.position.x, shape.position.y, shape.position.z);
		Matrix4f matrix = poseStack.last().pose();
		VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugLineStrip(1.5));
		shape.render(buffer, matrix, t);
		poseStack.popPose();
	}

	private static class RoomShape {
		Vec3 position;
		int shapeType;
		float[] color;
		float size;
		int timeOffset;
		float rotationSpeed;
		float[] params;
		boolean isStatic;

		RoomShape(Vec3 pos, boolean isGrouped) {
			this.position = pos;
			this.shapeType = random.nextInt(15);
			// minimum size increased, max size slightly reduced for grouped shapes
			if (isGrouped) {
				this.size = 0.9f + random.nextFloat() * 0.5f; // 0.9 to 1.4 blocks for grouped
			} else {
				this.size = 1.0f + random.nextFloat() * 0.5f; // 1.0 to 1.5 blocks for single
			}
			this.timeOffset = random.nextInt(10000);
			this.isStatic = random.nextFloat() < 0.15f; // 15% static (reduced from 20%)
			this.rotationSpeed = isStatic ? 0 : (0.2f + random.nextFloat() * 0.6f);
			this.params = new float[8];
			for (int i = 0; i < params.length; i++) {
				params[i] = 1.0f + random.nextFloat() * 4.0f;
			}
			// infinity stone colors
			float[][] colors = {{0.3f, 0.6f, 1.0f}, // cyan - space
					{1.0f, 0.3f, 0.3f}, // red - power
					{0.9f, 0.3f, 1.0f}, // purple - reality
					{0.3f, 1.0f, 0.5f}, // green - time
					{1.0f, 0.7f, 0.2f}, // orange - soul
					{1.0f, 1.0f, 0.4f}, // yellow - mind
					{0.5f, 0.3f, 1.0f}, // deep purple
					{1.0f, 0.5f, 0.8f} // pink
			};
			this.color = colors[random.nextInt(colors.length)];
		}

		RoomShape(Vec3 pos) {
			this(pos, false);
		}

		void render(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (shapeType % 15) {
				case 0 -> renderTorusKnot(buffer, matrix, t);
				case 1 -> renderLissajous(buffer, matrix, t);
				case 2 -> renderPenroseTriangle(buffer, matrix, t);
				case 3 -> renderTesseract(buffer, matrix, t);
				case 4 -> renderSuperformula(buffer, matrix, t);
				case 5 -> renderButterfly(buffer, matrix, t);
				case 6 -> renderMobius(buffer, matrix, t);
				case 7 -> renderHypocycloid(buffer, matrix, t);
				case 8 -> renderSpiral(buffer, matrix, t);
				case 9 -> renderRoseCurve(buffer, matrix, t);
				case 10 -> renderNeckerCube(buffer, matrix, t);
				case 11 -> renderDNAHelix(buffer, matrix, t);
				case 12 -> renderTetrahedron(buffer, matrix, t);
				case 13 -> renderOctahedron(buffer, matrix, t);
				case 14 -> renderIcosahedron(buffer, matrix, t);
			}
		}

		// torus knot
		void renderTorusKnot(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 150;
			float p = 2 + (float) Math.sin(t * 0.08f) * 0.5f;
			float q = 3 + (float) Math.cos(t * 0.1f) * 0.8f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float r = 0.3f + 0.2f * (float) Math.cos(q * u + rot);
				float x = (1.2f + r * (float) Math.cos(q * u)) * (float) Math.cos(p * u);
				float y = (1.2f + r * (float) Math.cos(q * u)) * (float) Math.sin(p * u);
				float z = r * (float) Math.sin(q * u);
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// lissajous curves
		void renderLissajous(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 200;
			float a = params[0] + (float) Math.sin(t * 0.06f) * 0.8f;
			float b = params[1] + (float) Math.cos(t * 0.08f) * 0.8f;
			float c = params[2] + (float) Math.sin(t * 0.05f) * 0.8f;
			float delta = t * 0.1f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 4;
				float x = (float) Math.sin(a * u + delta);
				float y = (float) Math.sin(b * u);
				float z = (float) Math.sin(c * u + delta * 0.7f);
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// penrose triangle
		void renderPenroseTriangle(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			Vector3f[] vertices = new Vector3f[3];
			for (int i = 0; i < 3; i++) {
				float angle = (float) i / 3 * (float) Math.PI * 2 + rot;
				vertices[i] = new Vector3f((float) Math.cos(angle) * size, (float) Math.sin(angle) * size, 0);
			}
			float thickness = size * 0.2f;
			for (int i = 0; i < 3; i++) {
				Vector3f v1 = vertices[i];
				Vector3f v2 = vertices[(i + 1) % 3];
				for (int j = 0; j <= 20; j++) {
					float progress = (float) j / 20;
					Vector3f pos = new Vector3f(v1.x + (v2.x - v1.x) * progress, v1.y + (v2.y - v1.y) * progress, (float) Math.sin(progress * Math.PI) * thickness * 0.3f);
					buffer.addVertex(matrix, pos.x, pos.y, pos.z).setColor(color[0], color[1], color[2], 0.95f);
				}
			}
		}

		// tesseract (4d hypercube)
		void renderTesseract(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float rot4d = t * rotationSpeed * 0.3f;
			float s = size * 0.3f;
			float[][] vertices4d = new float[16][4];
			int idx = 0;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 2; k++) {
						for (int l = 0; l < 2; l++) {
							vertices4d[idx][0] = (i * 2 - 1) * s;
							vertices4d[idx][1] = (j * 2 - 1) * s;
							vertices4d[idx][2] = (k * 2 - 1) * s;
							vertices4d[idx][3] = (l * 2 - 1) * s;
							idx++;
						}
					}
				}
			}
			Vector3f[] projected = new Vector3f[16];
			for (int i = 0; i < 16; i++) {
				float x4 = vertices4d[i][0];
				float y4 = vertices4d[i][1];
				float z4 = vertices4d[i][2];
				float w4 = vertices4d[i][3];
				float x4r = x4 * (float) Math.cos(rot4d) - w4 * (float) Math.sin(rot4d);
				float w4r = x4 * (float) Math.sin(rot4d) + w4 * (float) Math.cos(rot4d);
				float distance = 3.0f;
				float w = 1.0f / Math.max(0.5f, distance - w4r * 0.3f);
				w = Math.min(w, 2.0f);
				projected[i] = new Vector3f(x4r * w, y4 * w, z4 * w);
				float x = projected[i].x * (float) Math.cos(rot) - projected[i].z * (float) Math.sin(rot);
				float z = projected[i].x * (float) Math.sin(rot) + projected[i].z * (float) Math.cos(rot);
				projected[i].x = x;
				projected[i].z = z;
			}
			int[][] edges = {{0, 1}, {2, 3}, {4, 5}, {6, 7}, {8, 9}, {10, 11}, {12, 13}, {14, 15}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {8, 10}, {9, 11}, {12, 14}, {13, 15}, {0, 4}, {1, 5}, {2, 6}, {3, 7}, {8, 12}, {9, 13}, {10, 14}, {11, 15}, {0, 8},
					{1, 9}, {2, 10}, {3, 11}, {4, 12}, {5, 13}, {6, 14}, {7, 15}};
			for (int[] edge : edges) {
				Vector3f v1 = projected[edge[0]];
				Vector3f v2 = projected[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		// superformula
		void renderSuperformula(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 120;
			float m = 6;
			float n1 = params[5] * (float) Math.sin(t * 0.04f);
			float n2 = params[6] * (float) Math.cos(t * 0.05f);
			float n3 = params[7];
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float phi = (float) i / segments * (float) Math.PI * 2;
				float r = (float) Math.pow(Math.pow(Math.abs(Math.cos(m * phi / 4)), n2) + Math.pow(Math.abs(Math.sin(m * phi / 4)), n3), -1.0 / Math.abs(n1 + 0.1));
				float x = r * (float) Math.cos(phi + rot);
				float y = r * (float) Math.sin(phi + rot);
				float z = (float) Math.sin(phi * 2 + t * 0.3f);
				buffer.addVertex(matrix, x * size * 0.6f, y * size * 0.6f, z * size * 0.6f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// butterfly curve
		void renderButterfly(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 180;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 12;
				float ex = (float) Math.exp(Math.sin(u + t * 0.08f)) - 2 * (float) Math.cos(4 * u) - (float) Math.pow(Math.sin(u / 12), 5);
				float x = (float) Math.sin(u) * ex;
				float y = (float) Math.cos(u) * ex;
				float z = (float) Math.sin(u * 2 + rot) * 0.4f;
				buffer.addVertex(matrix, x * size * 0.2f, y * size * 0.2f, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// mobius strip
		void renderMobius(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 120;
			float vWidth = 0.25f + (float) Math.sin(t * 0.08f) * 0.1f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.2f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float twist = (float) Math.sin(rot) * 0.3f;
				float x = (1 + vWidth * (float) Math.cos(u / 2 + twist)) * (float) Math.cos(u);
				float y = (1 + vWidth * (float) Math.cos(u / 2 + twist)) * (float) Math.sin(u);
				float z = vWidth * (float) Math.sin(u / 2 + twist);
				buffer.addVertex(matrix, x * size * 0.9f, y * size * 0.9f, z * size * 0.9f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// hypocycloid
		void renderHypocycloid(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 120;
			float a = 4 + (float) Math.sin(t * 0.06f);
			float b = params[3] + (float) Math.sin(t * 0.08f) * 1.0f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float ratio = (a - b) / b;
				float x = (a - b) * (float) Math.cos(u + rot) + b * (float) Math.cos(ratio * u);
				float y = (a - b) * (float) Math.sin(u + rot) - b * (float) Math.sin(ratio * u);
				float z = (float) Math.sin(u * 3 + t * 0.4f) * 0.5f;
				buffer.addVertex(matrix, x * size * 0.25f, y * size * 0.25f, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// spiral
		void renderSpiral(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 150;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.2f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 4;
				float radius = 0.3f + 0.4f * (float) Math.sin(u * 1.2f + t * 0.3f);
				float x = radius * (float) Math.cos(u + rot);
				float y = u * 0.2f - 2;
				float z = radius * (float) Math.sin(u + rot);
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// rose curve
		void renderRoseCurve(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 150;
			float k = params[4];
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float theta = (float) i / segments * (float) Math.PI * 6;
				float r = (float) Math.sin(k * theta + t * 0.08f);
				float x = r * (float) Math.cos(theta + rot);
				float y = r * (float) Math.sin(theta + rot);
				float z = (float) Math.sin(theta * 2 + t * 0.4f) * 0.4f;
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		// necker cube
		void renderNeckerCube(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float s = size * 0.5f;
			Vector3f[] vertices = {new Vector3f(-s, -s, -s), new Vector3f(s, -s, -s), new Vector3f(s, s, -s), new Vector3f(-s, s, -s), new Vector3f(-s, -s, s), new Vector3f(s, -s, s), new Vector3f(s, s, s), new Vector3f(-s, s, s)};
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}, {4, 5}, {5, 6}, {6, 7}, {7, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
			for (int[] edge : edges) {
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		// dna helix
		void renderDNAHelix(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 100;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 3;
				float x1 = (float) Math.cos(u + rot) * size * 0.4f;
				float y1 = u * 0.25f - 2;
				float z1 = (float) Math.sin(u + rot) * size * 0.4f;
				buffer.addVertex(matrix, x1, y1, z1).setColor(color[0], color[1], color[2], 0.95f);
				float x2 = (float) Math.cos(u + (float) Math.PI + rot) * size * 0.4f;
				float z2 = (float) Math.sin(u + (float) Math.PI + rot) * size * 0.4f;
				buffer.addVertex(matrix, x2, y1, z2).setColor(color[0] * 0.7f, color[1] * 0.7f, color[2] * 0.7f, 0.95f);
				if (i % 10 == 0) {
					buffer.addVertex(matrix, x1, y1, z1).setColor(color[0] * 0.5f, color[1] * 0.5f, color[2] * 0.5f, 0.8f);
					buffer.addVertex(matrix, x2, y1, z2).setColor(color[0] * 0.5f, color[1] * 0.5f, color[2] * 0.5f, 0.8f);
				}
			}
		}

		// tetrahedron
		void renderTetrahedron(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float s = size;
			Vector3f[] vertices = {new Vector3f(s, s, s), new Vector3f(s, -s, -s), new Vector3f(-s, s, -s), new Vector3f(-s, -s, s)};
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			int[][] edges = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}, {2, 3}};
			for (int[] edge : edges) {
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		// octahedron
		void renderOctahedron(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float s = size;
			Vector3f[] vertices = {new Vector3f(s, 0, 0), new Vector3f(-s, 0, 0), new Vector3f(0, s, 0), new Vector3f(0, -s, 0), new Vector3f(0, 0, s), new Vector3f(0, 0, -s)};
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			int[][] edges = {{0, 2}, {0, 3}, {0, 4}, {0, 5}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {2, 4}, {4, 3}, {3, 5}, {5, 2}};
			for (int[] edge : edges) {
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		// icosahedron
		void renderIcosahedron(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float phi = (1 + (float) Math.sqrt(5)) / 2;
			float s = size * 0.5f;
			Vector3f[] vertices = {new Vector3f(-s, phi * s, 0), new Vector3f(s, phi * s, 0), new Vector3f(-s, -phi * s, 0), new Vector3f(s, -phi * s, 0), new Vector3f(0, -s, phi * s), new Vector3f(0, s, phi * s), new Vector3f(0, -s, -phi * s),
					new Vector3f(0, s, -phi * s), new Vector3f(phi * s, 0, -s), new Vector3f(phi * s, 0, s), new Vector3f(-phi * s, 0, -s), new Vector3f(-phi * s, 0, s)};
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			int[][] edges = {{0, 11}, {0, 5}, {0, 1}, {0, 7}, {0, 10}, {1, 5}, {5, 9}, {11, 5}, {11, 4}, {11, 10}, {2, 3}, {3, 9}, {3, 4}, {4, 9}, {2, 6}, {6, 7}, {7, 8}, {8, 9}, {8, 1}, {1, 7}, {2, 10}, {6, 10}, {6, 8}, {3, 2}, {4, 2}};
			for (int[] edge : edges) {
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}
	}
}
