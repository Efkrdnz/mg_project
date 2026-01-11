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
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.client.renderer.UnrealVoidRenderer;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;

@EventBusSubscriber(modid = "minefinity_gauntlet", value = Dist.CLIENT)
public class UnrealVoidRenderer {
	private static final List<CosmicStructure> structures = new ArrayList<>();
	private static long lastUpdateTime = 0;
	private static final int MAX_STRUCTURES = 60;
	private static final double RENDER_DISTANCE = 200.0;
	private static final Random random = new Random();

	@SubscribeEvent
	public static void onRenderWorld(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)
			return;
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null)
			return;
		String dimensionKey = mc.level.dimension().location().toString();
		if (!dimensionKey.equals("minefinity_gauntlet:unreal_void"))
			return;
		long currentTime = mc.level.getGameTime();
		if (currentTime - lastUpdateTime > 60) {
			updateStructures(mc.player.position());
			lastUpdateTime = currentTime;
		}
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
		Vec3 cameraPos = event.getCamera().getPosition();
		poseStack.pushPose();
		poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		for (CosmicStructure structure : structures) {
			if (structure.position.distanceTo(cameraPos) > RENDER_DISTANCE)
				continue;
			renderStructure(structure, poseStack, bufferSource, currentTime, mc.level, cameraPos);
		}
		poseStack.popPose();
		bufferSource.endBatch();
	}

	private static void updateStructures(Vec3 playerPos) {
		structures.removeIf(s -> s.position.distanceTo(playerPos) > RENDER_DISTANCE + 80);
		while (structures.size() < MAX_STRUCTURES) {
			double angle = random.nextDouble() * Math.PI * 2;
			double distance = 20 + random.nextDouble() * 140;
			double x = playerPos.x + Math.cos(angle) * distance;
			double y = playerPos.y + (random.nextDouble() - 0.5) * 100;
			double z = playerPos.z + Math.sin(angle) * distance;
			Vec3 pos = new Vec3(x, y, z);
			boolean tooClose = false;
			for (CosmicStructure existing : structures) {
				if (existing.position.distanceTo(pos) < 8) {
					tooClose = true;
					break;
				}
			}
			if (!tooClose) {
				structures.add(new CosmicStructure(pos));
			}
		}
	}

	private static void renderStructure(CosmicStructure structure, PoseStack poseStack, MultiBufferSource bufferSource, long time, net.minecraft.world.level.Level level, Vec3 cameraPos) {
		float t = (time + structure.timeOffset) * 0.05f;
		poseStack.pushPose();
		poseStack.translate(structure.position.x, structure.position.y, structure.position.z);
		Matrix4f matrix = poseStack.last().pose();
		if (structure.type == StructureType.GALAXY) {
			VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugLineStrip(1.0));
			structure.render(buffer, matrix, t);
			// subtle particle stars for galaxies only
			if (time % 3 == 0 && structure.position.distanceTo(cameraPos) < 80) {
				spawnGalaxyParticles(structure, level, t);
			}
		} else {
			VertexConsumer buffer = bufferSource.getBuffer(RenderType.debugLineStrip(1.5));
			structure.render(buffer, matrix, t);
		}
		poseStack.popPose();
	}

	private static void spawnGalaxyParticles(CosmicStructure structure, net.minecraft.world.level.Level level, float t) {
		if (structure.type != StructureType.GALAXY)
			return;
		for (int i = 0; i < 2; i++) {
			float angle = random.nextFloat() * (float) Math.PI * 2;
			float radius = random.nextFloat() * structure.size * 0.7f;
			float spiralAngle = angle + radius * 0.4f + t * 0.05f;
			double px = structure.position.x + radius * Math.cos(spiralAngle);
			double py = structure.position.y + (random.nextDouble() - 0.5) * structure.size * 0.05;
			double pz = structure.position.z + radius * Math.sin(spiralAngle);
			level.addParticle(ParticleTypes.END_ROD, px, py, pz, 0, 0, 0);
		}
	}

	enum StructureType {
		GALAXY, PARAMETRIC, FRACTAL, VORTEX, CONSTELLATION, IMPOSSIBLE, GEOMETRIC, PLANE, GLITCH
	}

	private static class CosmicStructure {
		Vec3 position;
		StructureType type;
		int shapeType;
		float[] color;
		float size;
		int timeOffset;
		float rotationSpeed;
		Vector3f rotationAxis;
		float[] params;
		boolean isStatic;
		float glitchPhase;

		CosmicStructure(Vec3 pos) {
			this.position = pos;
			float typeRoll = random.nextFloat();
			if (typeRoll < 0.20f) {
				this.type = StructureType.GALAXY;
			} else if (typeRoll < 0.35f) {
				this.type = StructureType.IMPOSSIBLE;
			} else if (typeRoll < 0.50f) {
				this.type = StructureType.GEOMETRIC;
			} else if (typeRoll < 0.60f) {
				this.type = StructureType.PLANE;
			} else if (typeRoll < 0.70f) {
				this.type = StructureType.CONSTELLATION;
			} else if (typeRoll < 0.80f) {
				this.type = StructureType.VORTEX;
			} else if (typeRoll < 0.90f) {
				this.type = StructureType.GLITCH;
			} else if (typeRoll < 0.95f) {
				this.type = StructureType.FRACTAL;
			} else {
				this.type = StructureType.PARAMETRIC;
			}
			this.shapeType = random.nextInt(25);
			this.isStatic = random.nextFloat() < 0.3f;
			this.glitchPhase = random.nextFloat() * 100;
			if (type == StructureType.GALAXY) {
				this.size = 10.0f + random.nextFloat() * 18.0f;
			} else if (type == StructureType.PLANE) {
				this.size = 8.0f + random.nextFloat() * 15.0f;
			} else if (type == StructureType.CONSTELLATION) {
				this.size = 6.0f + random.nextFloat() * 12.0f;
			} else {
				this.size = 3.0f + random.nextFloat() * 8.0f;
			}
			this.timeOffset = random.nextInt(10000);
			this.rotationSpeed = isStatic ? 0 : (0.1f + random.nextFloat() * 0.4f);
			this.rotationAxis = new Vector3f(random.nextFloat() - 0.5f, random.nextFloat() - 0.5f, random.nextFloat() - 0.5f).normalize();
			this.params = new float[10];
			for (int i = 0; i < params.length; i++) {
				params[i] = 1.0f + random.nextFloat() * 5.0f;
			}
			// cosmic colors
			float[][] colors = {{0.3f, 0.6f, 1.0f}, // cyan
					{1.0f, 0.3f, 0.3f}, // red
					{0.9f, 0.3f, 1.0f}, // magenta
					{0.3f, 1.0f, 0.5f}, // green
					{1.0f, 0.7f, 0.2f}, // orange
					{1.0f, 1.0f, 0.4f}, // yellow
					{0.5f, 0.3f, 1.0f}, // purple
					{1.0f, 0.5f, 0.8f} // pink
			};
			this.color = colors[random.nextInt(colors.length)];
		}

		void render(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (type) {
				case GALAXY -> renderGalaxy(buffer, matrix, t);
				case IMPOSSIBLE -> renderImpossible(buffer, matrix, t);
				case GEOMETRIC -> renderGeometric(buffer, matrix, t);
				case PLANE -> renderPlane(buffer, matrix, t);
				case VORTEX -> renderVortex(buffer, matrix, t);
				case CONSTELLATION -> renderConstellation(buffer, matrix, t);
				case FRACTAL -> renderFractal(buffer, matrix, t);
				case GLITCH -> renderGlitch(buffer, matrix, t);
				case PARAMETRIC -> renderParametric(buffer, matrix, t);
			}
		}

		// improved horizontal spinning galaxy
		void renderGalaxy(VertexConsumer buffer, Matrix4f matrix, float t) {
			int arms = 2 + random.nextInt(3);
			int pointsPerArm = 120;
			for (int arm = 0; arm < arms; arm++) {
				float armOffset = (float) arm / arms * (float) Math.PI * 2;
				for (int i = 0; i <= pointsPerArm; i++) {
					float radius = (float) i / pointsPerArm * size;
					float angle = armOffset + radius * 0.3f + t * rotationSpeed * 0.2f;
					float wobble = (float) Math.sin(radius * 1.5f + t * 0.2f) * 0.15f;
					float x = radius * (float) Math.cos(angle + wobble);
					float y = (float) Math.sin(radius * 2 + t * 0.3f) * size * 0.03f;
					float z = radius * (float) Math.sin(angle + wobble);
					float brightness = 1.0f - (radius / size) * 0.4f;
					buffer.addVertex(matrix, x, y, z).setColor(color[0] * brightness, color[1] * brightness, color[2] * brightness, 0.95f);
				}
			}
			// bright galactic core
			int corePoints = 40;
			for (int i = 0; i <= corePoints; i++) {
				float angle = (float) i / corePoints * (float) Math.PI * 2;
				float radius = 0.3f + (float) Math.sin(angle * 6 + t * 0.5f) * 0.1f;
				float x = radius * (float) Math.cos(angle + t);
				float y = radius * (float) Math.sin(angle + t) * 0.2f;
				float z = radius * (float) Math.sin(angle + t);
				buffer.addVertex(matrix, x, y, z).setColor(1.0f, 1.0f, 1.0f, 1.0f);
			}
		}

		// impossible geometry shapes
		void renderImpossible(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (shapeType % 5) {
				case 0 -> renderPenroseTriangle(buffer, matrix, t);
				case 1 -> renderNeckerCube(buffer, matrix, t);
				case 2 -> renderImpossibleStaircase(buffer, matrix, t);
				case 3 -> renderInterlockingRings(buffer, matrix, t);
				case 4 -> renderTesseract(buffer, matrix, t);
			}
		}

		void renderPenroseTriangle(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			Vector3f[] vertices = new Vector3f[3];
			for (int i = 0; i < 3; i++) {
				float angle = (float) i / 3 * (float) Math.PI * 2 + rot;
				vertices[i] = new Vector3f((float) Math.cos(angle) * size, (float) Math.sin(angle) * size, 0);
			}
			float thickness = size * 0.2f;
			// draw three bars of the triangle
			for (int i = 0; i < 3; i++) {
				Vector3f v1 = vertices[i];
				Vector3f v2 = vertices[(i + 1) % 3];
				// outer edge
				for (int j = 0; j <= 30; j++) {
					float progress = (float) j / 30;
					Vector3f pos = new Vector3f(v1.x + (v2.x - v1.x) * progress, v1.y + (v2.y - v1.y) * progress, (float) Math.sin(progress * Math.PI) * thickness * 0.3f);
					buffer.addVertex(matrix, pos.x, pos.y, pos.z).setColor(color[0], color[1], color[2], 0.95f);
				}
				// inner edge with impossible overlap
				for (int j = 0; j <= 30; j++) {
					float progress = (float) j / 30;
					Vector3f innerDir = new Vector3f(v2.x - v1.x, v2.y - v1.y, 0).normalize().mul(thickness);
					Vector3f perpendicular = new Vector3f(-innerDir.y, innerDir.x, 0);
					Vector3f pos = new Vector3f(v1.x + (v2.x - v1.x) * progress + perpendicular.x, v1.y + (v2.y - v1.y) * progress + perpendicular.y, -(float) Math.sin(progress * Math.PI) * thickness * 0.3f);
					buffer.addVertex(matrix, pos.x, pos.y, pos.z).setColor(color[0] * 0.7f, color[1] * 0.7f, color[2] * 0.7f, 0.95f);
				}
			}
		}

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
			// draw cube edges with ambiguous depth
			int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}, {4, 5}, {5, 6}, {6, 7}, {7, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
			for (int[] edge : edges) {
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		void renderImpossibleStaircase(VertexConsumer buffer, Matrix4f matrix, float t) {
			int steps = 8;
			float stepSize = size * 0.3f;
			float rot = isStatic ? 0 : t * rotationSpeed;
			for (int i = 0; i < steps; i++) {
				float angle = (float) i / steps * (float) Math.PI * 2 + rot;
				float nextAngle = (float) (i + 1) / steps * (float) Math.PI * 2 + rot;
				float x1 = (float) Math.cos(angle) * size;
				float z1 = (float) Math.sin(angle) * size;
				float y1 = i * stepSize;
				float x2 = (float) Math.cos(nextAngle) * size;
				float z2 = (float) Math.sin(nextAngle) * size;
				float y2 = (i + 1) * stepSize;
				// horizontal step
				buffer.addVertex(matrix, x1, y1, z1).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, x2, y1, z2).setColor(color[0], color[1], color[2], 0.9f);
				// vertical riser
				buffer.addVertex(matrix, x2, y1, z2).setColor(color[0] * 0.7f, color[1] * 0.7f, color[2] * 0.7f, 0.9f);
				buffer.addVertex(matrix, x2, y2, z2).setColor(color[0] * 0.7f, color[1] * 0.7f, color[2] * 0.7f, 0.9f);
			}
		}

		void renderInterlockingRings(VertexConsumer buffer, Matrix4f matrix, float t) {
			int rings = 3;
			int segments = 80;
			float rot = isStatic ? 0 : t * rotationSpeed;
			for (int ring = 0; ring < rings; ring++) {
				float ringAngle = (float) ring / rings * (float) Math.PI * 2;
				for (int i = 0; i <= segments; i++) {
					float u = (float) i / segments * (float) Math.PI * 2;
					float x = (size + size * 0.3f * (float) Math.cos(u)) * (float) Math.cos(ringAngle + rot);
					float y = size * 0.3f * (float) Math.sin(u);
					float z = (size + size * 0.3f * (float) Math.cos(u)) * (float) Math.sin(ringAngle + rot);
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], 0.9f);
				}
			}
		}

		void renderTesseract(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float rot4d = t * rotationSpeed * 0.3f;
			float s = size * 0.3f; // Reduced base size
			// 16 vertices of a 4d hypercube projected to 3d
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
			// rotate in 4d space then project to 3d
			Vector3f[] projected = new Vector3f[16];
			for (int i = 0; i < 16; i++) {
				float x4 = vertices4d[i][0];
				float y4 = vertices4d[i][1];
				float z4 = vertices4d[i][2];
				float w4 = vertices4d[i][3];
				// 4d rotation (XW plane)
				float x4r = x4 * (float) Math.cos(rot4d) - w4 * (float) Math.sin(rot4d);
				float w4r = x4 * (float) Math.sin(rot4d) + w4 * (float) Math.cos(rot4d);
				// project to 3d with clamped perspective
				float distance = 3.0f;
				float w = 1.0f / Math.max(0.5f, distance - w4r * 0.3f); // Clamp to prevent extreme scaling
				w = Math.min(w, 2.0f); // Limit maximum scale
				projected[i] = new Vector3f(x4r * w, y4 * w, z4 * w);
				// 3d rotation
				float x = projected[i].x * (float) Math.cos(rot) - projected[i].z * (float) Math.sin(rot);
				float z = projected[i].x * (float) Math.sin(rot) + projected[i].z * (float) Math.cos(rot);
				projected[i].x = x;
				projected[i].z = z;
			}
			// draw edges
			int[][] edges = {{0, 1}, {2, 3}, {4, 5}, {6, 7}, {8, 9}, {10, 11}, {12, 13}, {14, 15}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {8, 10}, {9, 11}, {12, 14}, {13, 15}, {0, 4}, {1, 5}, {2, 6}, {3, 7}, {8, 12}, {9, 13}, {10, 14}, {11, 15}, {0, 8},
					{1, 9}, {2, 10}, {3, 11}, {4, 12}, {5, 13}, {6, 14}, {7, 15}};
			for (int[] edge : edges) {
				Vector3f v1 = projected[edge[0]];
				Vector3f v2 = projected[edge[1]];
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
			}
		}

		// platonic solids and geometric shapes
		void renderGeometric(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (shapeType % 6) {
				case 0 -> renderTetrahedron(buffer, matrix, t);
				case 1 -> renderOctahedron(buffer, matrix, t);
				case 2 -> renderIcosahedron(buffer, matrix, t);
				case 3 -> renderDodecahedron(buffer, matrix, t);
				case 4 -> renderGeodesicSphere(buffer, matrix, t);
				case 5 -> renderDNAHelix(buffer, matrix, t);
			}
		}

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

		void renderDodecahedron(VertexConsumer buffer, Matrix4f matrix, float t) {
			float rot = isStatic ? 0 : t * rotationSpeed;
			float phi = (1 + (float) Math.sqrt(5)) / 2;
			float s = size * 0.4f;
			Vector3f[] vertices = new Vector3f[20];
			int idx = 0;
			// generate dodecahedron vertices
			for (int i = -1; i <= 1; i += 2) {
				for (int j = -1; j <= 1; j += 2) {
					for (int k = -1; k <= 1; k += 2) {
						vertices[idx++] = new Vector3f(i * s, j * s, k * s);
					}
				}
			}
			vertices[8] = new Vector3f(0, phi * s, 1 / phi * s);
			vertices[9] = new Vector3f(0, phi * s, -1 / phi * s);
			vertices[10] = new Vector3f(0, -phi * s, 1 / phi * s);
			vertices[11] = new Vector3f(0, -phi * s, -1 / phi * s);
			vertices[12] = new Vector3f(1 / phi * s, 0, phi * s);
			vertices[13] = new Vector3f(-1 / phi * s, 0, phi * s);
			vertices[14] = new Vector3f(1 / phi * s, 0, -phi * s);
			vertices[15] = new Vector3f(-1 / phi * s, 0, -phi * s);
			vertices[16] = new Vector3f(phi * s, 1 / phi * s, 0);
			vertices[17] = new Vector3f(phi * s, -1 / phi * s, 0);
			vertices[18] = new Vector3f(-phi * s, 1 / phi * s, 0);
			vertices[19] = new Vector3f(-phi * s, -1 / phi * s, 0);
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			// simplified edge rendering
			for (int i = 0; i < vertices.length; i++) {
				for (int j = i + 1; j < vertices.length; j++) {
					float dist = vertices[i].distance(vertices[j]);
					if (dist < size * 1.2f) {
						buffer.addVertex(matrix, vertices[i].x, vertices[i].y, vertices[i].z).setColor(color[0], color[1], color[2], 0.9f);
						buffer.addVertex(matrix, vertices[j].x, vertices[j].y, vertices[j].z).setColor(color[0], color[1], color[2], 0.9f);
					}
				}
			}
		}

		void renderGeodesicSphere(VertexConsumer buffer, Matrix4f matrix, float t) {
			int subdivisions = 2;
			float rot = isStatic ? 0 : t * rotationSpeed;
			List<Vector3f> vertices = new ArrayList<>();
			// start with icosahedron
			float phi = (1 + (float) Math.sqrt(5)) / 2;
			vertices.add(new Vector3f(-1, phi, 0).normalize().mul(size));
			vertices.add(new Vector3f(1, phi, 0).normalize().mul(size));
			vertices.add(new Vector3f(-1, -phi, 0).normalize().mul(size));
			vertices.add(new Vector3f(1, -phi, 0).normalize().mul(size));
			vertices.add(new Vector3f(0, -1, phi).normalize().mul(size));
			vertices.add(new Vector3f(0, 1, phi).normalize().mul(size));
			vertices.add(new Vector3f(0, -1, -phi).normalize().mul(size));
			vertices.add(new Vector3f(0, 1, -phi).normalize().mul(size));
			vertices.add(new Vector3f(phi, 0, -1).normalize().mul(size));
			vertices.add(new Vector3f(phi, 0, 1).normalize().mul(size));
			vertices.add(new Vector3f(-phi, 0, -1).normalize().mul(size));
			vertices.add(new Vector3f(-phi, 0, 1).normalize().mul(size));
			for (Vector3f v : vertices) {
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			// draw connections
			for (int i = 0; i < vertices.size(); i++) {
				for (int j = i + 1; j < vertices.size(); j++) {
					float dist = vertices.get(i).distance(vertices.get(j));
					if (dist < size * 1.1f) {
						Vector3f v1 = vertices.get(i);
						Vector3f v2 = vertices.get(j);
						buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], 0.9f);
						buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], 0.9f);
					}
				}
			}
		}

		void renderDNAHelix(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 200;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 4;
				// first strand
				float x1 = (float) Math.cos(u + rot) * size * 0.4f;
				float y1 = u * 0.4f - 5;
				float z1 = (float) Math.sin(u + rot) * size * 0.4f;
				buffer.addVertex(matrix, x1, y1, z1).setColor(color[0], color[1], color[2], 0.95f);
				// second strand
				float x2 = (float) Math.cos(u + (float) Math.PI + rot) * size * 0.4f;
				float z2 = (float) Math.sin(u + (float) Math.PI + rot) * size * 0.4f;
				buffer.addVertex(matrix, x2, y1, z2).setColor(color[0] * 0.7f, color[1] * 0.7f, color[2] * 0.7f, 0.95f);
				// connectors every quarter turn
				if (i % 15 == 0) {
					buffer.addVertex(matrix, x1, y1, z1).setColor(color[0] * 0.5f, color[1] * 0.5f, color[2] * 0.5f, 0.8f);
					buffer.addVertex(matrix, x2, y1, z2).setColor(color[0] * 0.5f, color[1] * 0.5f, color[2] * 0.5f, 0.8f);
				}
			}
		}

		// horizontal planes with patterns
		void renderPlane(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (shapeType % 4) {
				case 0 -> renderGridPlane(buffer, matrix, t);
				case 1 -> renderWavePlane(buffer, matrix, t);
				case 2 -> renderSpiralPlane(buffer, matrix, t);
				case 3 -> renderHexagonalPlane(buffer, matrix, t);
			}
		}

		void renderGridPlane(VertexConsumer buffer, Matrix4f matrix, float t) {
			int lines = 16;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.5f;
			for (int i = -lines / 2; i <= lines / 2; i++) {
				float offset = (float) i / lines * size * 2;
				// parallel lines in x direction
				float x1 = -size;
				float x2 = size;
				float z = offset;
				float y = (float) Math.sin(offset * 0.5f + t * 0.3f) * size * 0.1f;
				buffer.addVertex(matrix, x1, y, z).setColor(color[0], color[1], color[2], 0.8f);
				buffer.addVertex(matrix, x2, y, z).setColor(color[0], color[1], color[2], 0.8f);
				// parallel lines in z direction
				float x = offset; // <-- This line was missing!
				float z1 = -size;
				float z2 = size;
				y = (float) Math.sin(offset * 0.5f + t * 0.3f) * size * 0.1f;
				buffer.addVertex(matrix, x, y, z1).setColor(color[0], color[1], color[2], 0.8f);
				buffer.addVertex(matrix, x, y, z2).setColor(color[0], color[1], color[2], 0.8f);
			}
		}

		void renderWavePlane(VertexConsumer buffer, Matrix4f matrix, float t) {
			int resolution = 20;
			for (int i = 0; i < resolution; i++) {
				for (int j = 0; j <= resolution; j++) {
					float x = ((float) i / resolution - 0.5f) * size * 2;
					float z = ((float) j / resolution - 0.5f) * size * 2;
					float y = (float) (Math.sin(x * 0.5f + t * 0.3f) * Math.cos(z * 0.5f + t * 0.3f)) * size * 0.2f;
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], 0.85f);
				}
			}
		}

		void renderSpiralPlane(VertexConsumer buffer, Matrix4f matrix, float t) {
			int spirals = 8;
			int segments = 100;
			for (int spiral = 0; spiral < spirals; spiral++) {
				float spiralOffset = (float) spiral / spirals * (float) Math.PI * 2;
				for (int i = 0; i <= segments; i++) {
					float progress = (float) i / segments;
					float radius = progress * size;
					float angle = spiralOffset + progress * (float) Math.PI * 4 + t * rotationSpeed * 0.3f;
					float x = radius * (float) Math.cos(angle);
					float y = (float) Math.sin(progress * Math.PI * 2 + t * 0.5f) * size * 0.1f;
					float z = radius * (float) Math.sin(angle);
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], 0.85f);
				}
			}
		}

		void renderHexagonalPlane(VertexConsumer buffer, Matrix4f matrix, float t) {
			int rings = 6;
			for (int ring = 1; ring <= rings; ring++) {
				int hexagons = ring * 6;
				float radius = ring * size * 0.3f;
				for (int i = 0; i <= hexagons; i++) {
					float angle = (float) i / hexagons * (float) Math.PI * 2;
					float x = radius * (float) Math.cos(angle);
					float z = radius * (float) Math.sin(angle);
					float y = (float) Math.sin(angle * 6 + t * 0.5f) * size * 0.05f;
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], 0.85f);
				}
			}
		}

		// smoother vortex
		void renderVortex(VertexConsumer buffer, Matrix4f matrix, float t) {
			int layers = 6;
			int pointsPerLayer = 60;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int layer = 0; layer < layers; layer++) {
				float layerHeight = ((float) layer / layers - 0.5f) * size * 1.5f;
				float layerRadius = size * (1.0f - (float) layer / layers * 0.6f);
				for (int i = 0; i <= pointsPerLayer; i++) {
					float angle = (float) i / pointsPerLayer * (float) Math.PI * 2;
					float spiral = angle + layerHeight * 0.3f + rot;
					float r = layerRadius * (0.5f + (float) Math.sin(angle * 3 + t * 0.3f) * 0.1f);
					float x = r * (float) Math.cos(spiral);
					float y = layerHeight;
					float z = r * (float) Math.sin(spiral);
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], 0.9f);
				}
			}
		}

		// constellation
		void renderConstellation(VertexConsumer buffer, Matrix4f matrix, float t) {
			int stars = 6 + random.nextInt(6);
			Vector3f[] starPositions = new Vector3f[stars];
			for (int i = 0; i < stars; i++) {
				float theta = random.nextFloat() * (float) Math.PI * 2;
				float phi = random.nextFloat() * (float) Math.PI;
				float r = size * (0.5f + random.nextFloat() * 0.5f);
				starPositions[i] = new Vector3f(r * (float) Math.sin(phi) * (float) Math.cos(theta), r * (float) Math.sin(phi) * (float) Math.sin(theta), r * (float) Math.cos(phi));
			}
			// connect stars
			for (int i = 0; i < stars; i++) {
				Vector3f star1 = starPositions[i];
				// pulsing star
				float pulse = (float) Math.sin(t + i) * 0.15f + 0.25f;
				for (int j = 0; j < 15; j++) {
					Vector3f offset = new Vector3f((random.nextFloat() - 0.5f) * pulse, (random.nextFloat() - 0.5f) * pulse, (random.nextFloat() - 0.5f) * pulse);
					buffer.addVertex(matrix, star1.x + offset.x, star1.y + offset.y, star1.z + offset.z).setColor(1.0f, 1.0f, 1.0f, 0.95f);
				}
				// connect to nearest neighbors
				for (int j = i + 1; j < stars && j < i + 3; j++) {
					Vector3f star2 = starPositions[j];
					buffer.addVertex(matrix, star1.x, star1.y, star1.z).setColor(color[0], color[1], color[2], 0.7f);
					buffer.addVertex(matrix, star2.x, star2.y, star2.z).setColor(color[0], color[1], color[2], 0.7f);
				}
			}
		}

		// fractal
		void renderFractal(VertexConsumer buffer, Matrix4f matrix, float t) {
			renderFractalBranch(buffer, matrix, 0, 0, 0, size, t, 0, 3);
		}

		void renderFractalBranch(VertexConsumer buffer, Matrix4f matrix, float x, float y, float z, float length, float t, float angle, int depth) {
			if (depth <= 0)
				return;
			int segments = 20;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.2f;
			for (int i = 0; i <= segments; i++) {
				float progress = (float) i / segments;
				float currentAngle = angle + progress * (float) Math.PI * params[0] + rot;
				float nx = x + length * progress * (float) Math.cos(currentAngle);
				float ny = y + length * progress * (float) Math.sin(progress * (float) Math.PI * 0.5f);
				float nz = z + length * progress * (float) Math.sin(currentAngle);
				buffer.addVertex(matrix, nx, ny, nz).setColor(color[0], color[1], color[2], 0.85f);
			}
			if (depth > 1) {
				float newLength = length * 0.6f;
				float endX = x + length * (float) Math.cos(angle);
				float endZ = z + length * (float) Math.sin(angle);
				renderFractalBranch(buffer, matrix, endX, y, endZ, newLength, t, angle + 0.6f, depth - 1);
				renderFractalBranch(buffer, matrix, endX, y, endZ, newLength, t, angle - 0.6f, depth - 1);
			}
		}

		// glitch effects - shapes that phase in/out or have missing segments
		void renderGlitch(VertexConsumer buffer, Matrix4f matrix, float t) {
			float glitchIntensity = (float) Math.sin((t + glitchPhase) * 0.5f);
			boolean shouldRender = glitchIntensity > -0.5f;
			if (!shouldRender && random.nextFloat() > 0.8f)
				return;
			switch (shapeType % 3) {
				case 0 -> renderGlitchTorus(buffer, matrix, t, glitchIntensity);
				case 1 -> renderGlitchCube(buffer, matrix, t, glitchIntensity);
				case 2 -> renderGlitchSphere(buffer, matrix, t, glitchIntensity);
			}
		}

		void renderGlitchTorus(VertexConsumer buffer, Matrix4f matrix, float t, float glitchIntensity) {
			int segments = 150;
			float rot = t * rotationSpeed * 0.4f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				// random segments missing
				if (glitchIntensity > 0 && random.nextFloat() < 0.3f)
					continue;
				float R = size * 0.8f;
				float r = size * 0.3f;
				float x = (R + r * (float) Math.cos(u)) * (float) Math.cos(u * 3 + rot);
				float y = r * (float) Math.sin(u);
				float z = (R + r * (float) Math.cos(u)) * (float) Math.sin(u * 3 + rot);
				// add glitch offset
				if (glitchIntensity > 0.5f) {
					x += (random.nextFloat() - 0.5f) * size * 0.2f;
					y += (random.nextFloat() - 0.5f) * size * 0.2f;
					z += (random.nextFloat() - 0.5f) * size * 0.2f;
				}
				float alpha = 0.9f - Math.abs(glitchIntensity) * 0.3f;
				buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], alpha);
			}
		}

		void renderGlitchCube(VertexConsumer buffer, Matrix4f matrix, float t, float glitchIntensity) {
			float s = size * 0.6f;
			float rot = t * rotationSpeed * 0.4f;
			Vector3f[] vertices = {new Vector3f(-s, -s, -s), new Vector3f(s, -s, -s), new Vector3f(s, s, -s), new Vector3f(-s, s, -s), new Vector3f(-s, -s, s), new Vector3f(s, -s, s), new Vector3f(s, s, s), new Vector3f(-s, s, s)};
			for (Vector3f v : vertices) {
				// add glitch jitter
				if (glitchIntensity > 0.3f) {
					v.x += (random.nextFloat() - 0.5f) * s * 0.3f;
					v.y += (random.nextFloat() - 0.5f) * s * 0.3f;
					v.z += (random.nextFloat() - 0.5f) * s * 0.3f;
				}
				float x = v.x * (float) Math.cos(rot) - v.z * (float) Math.sin(rot);
				float z = v.x * (float) Math.sin(rot) + v.z * (float) Math.cos(rot);
				v.x = x;
				v.z = z;
			}
			int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}, {4, 5}, {5, 6}, {6, 7}, {7, 4}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
			for (int[] edge : edges) {
				// randomly skip edges for glitch effect
				if (glitchIntensity > 0 && random.nextFloat() < 0.4f)
					continue;
				Vector3f v1 = vertices[edge[0]];
				Vector3f v2 = vertices[edge[1]];
				float alpha = 0.9f - Math.abs(glitchIntensity) * 0.4f;
				buffer.addVertex(matrix, v1.x, v1.y, v1.z).setColor(color[0], color[1], color[2], alpha);
				buffer.addVertex(matrix, v2.x, v2.y, v2.z).setColor(color[0], color[1], color[2], alpha);
			}
		}

		void renderGlitchSphere(VertexConsumer buffer, Matrix4f matrix, float t, float glitchIntensity) {
			int rings = 12;
			int segments = 16;
			float rot = t * rotationSpeed * 0.4f;
			for (int i = 0; i < rings; i++) {
				float phi1 = (float) i / rings * (float) Math.PI;
				float phi2 = (float) (i + 1) / rings * (float) Math.PI;
				for (int j = 0; j <= segments; j++) {
					// skip random segments
					if (glitchIntensity > 0 && random.nextFloat() < 0.25f)
						continue;
					float theta = (float) j / segments * (float) Math.PI * 2 + rot;
					float x = size * (float) Math.sin(phi1) * (float) Math.cos(theta);
					float y = size * (float) Math.cos(phi1);
					float z = size * (float) Math.sin(phi1) * (float) Math.sin(theta);
					// glitch displacement
					if (glitchIntensity > 0.5f) {
						x += (random.nextFloat() - 0.5f) * size * 0.2f;
						y += (random.nextFloat() - 0.5f) * size * 0.2f;
						z += (random.nextFloat() - 0.5f) * size * 0.2f;
					}
					float alpha = 0.9f - Math.abs(glitchIntensity) * 0.3f;
					buffer.addVertex(matrix, x, y, z).setColor(color[0], color[1], color[2], alpha);
				}
			}
		}

		// parametric chaos shapes
		void renderParametric(VertexConsumer buffer, Matrix4f matrix, float t) {
			switch (shapeType % 8) {
				case 0 -> renderTorusKnot(buffer, matrix, t);
				case 1 -> renderLissajous(buffer, matrix, t);
				case 2 -> renderSuperformula(buffer, matrix, t);
				case 3 -> renderButterfly(buffer, matrix, t);
				case 4 -> renderRoseCurve(buffer, matrix, t);
				case 5 -> renderSpiral(buffer, matrix, t);
				case 6 -> renderHypocycloid(buffer, matrix, t);
				case 7 -> renderMobius(buffer, matrix, t);
			}
		}

		void renderTorusKnot(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 200;
			float p = 2 + (float) Math.sin(t * 0.08f) * 0.5f;
			float q = 3 + (float) Math.cos(t * 0.1f) * 0.8f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float r = 0.5f + 0.3f * (float) Math.cos(q * u + rot);
				float x = (2 + r * (float) Math.cos(q * u)) * (float) Math.cos(p * u);
				float y = (2 + r * (float) Math.cos(q * u)) * (float) Math.sin(p * u);
				float z = r * (float) Math.sin(q * u);
				buffer.addVertex(matrix, x * size * 0.4f, y * size * 0.4f, z * size * 0.4f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderLissajous(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 300;
			float a = params[0] + (float) Math.sin(t * 0.06f) * 1.0f;
			float b = params[1] + (float) Math.cos(t * 0.08f) * 1.0f;
			float c = params[2] + (float) Math.sin(t * 0.05f) * 1.0f;
			float delta = t * 0.1f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 4;
				float x = (float) Math.sin(a * u + delta);
				float y = (float) Math.sin(b * u);
				float z = (float) Math.sin(c * u + delta * 0.7f);
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderSuperformula(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 150;
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
				buffer.addVertex(matrix, x * size * 0.8f, y * size * 0.8f, z * size * 0.8f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderButterfly(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 250;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 12;
				float ex = (float) Math.exp(Math.sin(u + t * 0.08f)) - 2 * (float) Math.cos(4 * u) - (float) Math.pow(Math.sin(u / 12), 5);
				float x = (float) Math.sin(u) * ex;
				float y = (float) Math.cos(u) * ex;
				float z = (float) Math.sin(u * 2 + rot) * 0.5f;
				buffer.addVertex(matrix, x * size * 0.3f, y * size * 0.3f, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderRoseCurve(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 200;
			float k = params[4];
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float theta = (float) i / segments * (float) Math.PI * 6;
				float r = (float) Math.sin(k * theta + t * 0.08f);
				float x = r * (float) Math.cos(theta + rot);
				float y = r * (float) Math.sin(theta + rot);
				float z = (float) Math.sin(theta * 2 + t * 0.4f) * 0.5f;
				buffer.addVertex(matrix, x * size * 1.5f, y * size * 1.5f, z * size * 1.5f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderSpiral(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 250;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.2f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 6;
				float radius = 0.5f + 0.6f * (float) Math.sin(u * 1.2f + t * 0.3f);
				float x = radius * (float) Math.cos(u + rot);
				float y = u * 0.3f - 4;
				float z = radius * (float) Math.sin(u + rot);
				buffer.addVertex(matrix, x * size, y * size, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderHypocycloid(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 180;
			float a = 5 + (float) Math.sin(t * 0.06f);
			float b = params[3] + (float) Math.sin(t * 0.08f) * 1.5f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.3f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float ratio = (a - b) / b;
				float x = (a - b) * (float) Math.cos(u + rot) + b * (float) Math.cos(ratio * u);
				float y = (a - b) * (float) Math.sin(u + rot) - b * (float) Math.sin(ratio * u);
				float z = (float) Math.sin(u * 3 + t * 0.4f) * 0.6f;
				buffer.addVertex(matrix, x * size * 0.35f, y * size * 0.35f, z * size).setColor(color[0], color[1], color[2], 0.95f);
			}
		}

		void renderMobius(VertexConsumer buffer, Matrix4f matrix, float t) {
			int segments = 180;
			float vWidth = 0.3f + (float) Math.sin(t * 0.08f) * 0.15f;
			float rot = isStatic ? 0 : t * rotationSpeed * 0.2f;
			for (int i = 0; i <= segments; i++) {
				float u = (float) i / segments * (float) Math.PI * 2;
				float twist = (float) Math.sin(rot) * 0.3f;
				float x = (1 + vWidth * (float) Math.cos(u / 2 + twist)) * (float) Math.cos(u);
				float y = (1 + vWidth * (float) Math.cos(u / 2 + twist)) * (float) Math.sin(u);
				float z = vWidth * (float) Math.sin(u / 2 + twist);
				buffer.addVertex(matrix, x * size * 1.2f, y * size * 1.2f, z * size * 1.2f).setColor(color[0], color[1], color[2], 0.95f);
			}
		}
	}
}
