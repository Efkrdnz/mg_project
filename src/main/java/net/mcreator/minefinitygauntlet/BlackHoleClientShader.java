package net.mcreator.minefinitygauntlet.client;

import org.checkerframework.checker.units.qual.h;

import net.mcreator.minefinitygauntlet.client.BlackHoleClientShader;

@net.neoforged.fml.common.EventBusSubscriber(modid = "minefinity_gauntlet", value = net.neoforged.api.distmarker.Dist.CLIENT, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
public class BlackHoleClientShader {
	public static net.minecraft.client.renderer.ShaderInstance BLACKHOLE_SHADER;
	public static net.minecraft.client.renderer.RenderType BLACKHOLE_RENDER_TYPE;
	private static com.mojang.blaze3d.pipeline.TextureTarget SCENE_COPY;
	private static int lastW = -1;
	private static int lastH = -1;
	private static boolean printedLoadOk = false;
	private static boolean printedLoadFail = false;

	@net.neoforged.bus.api.SubscribeEvent
	public static void registerShaders(net.neoforged.neoforge.client.event.RegisterShadersEvent event) {
		try {
			event.registerShader(
					new net.minecraft.client.renderer.ShaderInstance(event.getResourceProvider(), net.minecraft.resources.ResourceLocation.parse("minefinity_gauntlet:blackhole_billboard"), com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX),
					shader -> {
						BLACKHOLE_SHADER = shader;
						BLACKHOLE_RENDER_TYPE = net.minecraft.client.renderer.RenderType.create("blackhole_billboard", com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_TEX, com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS, 256, false, true,
								net.minecraft.client.renderer.RenderType.CompositeState.builder().setShaderState(new net.minecraft.client.renderer.RenderStateShard.ShaderStateShard(() -> BLACKHOLE_SHADER))
										.setDepthTestState(net.minecraft.client.renderer.RenderStateShard.LEQUAL_DEPTH_TEST).setCullState(net.minecraft.client.renderer.RenderStateShard.NO_CULL)
										.setWriteMaskState(net.minecraft.client.renderer.RenderStateShard.COLOR_WRITE).setTransparencyState(net.minecraft.client.renderer.RenderStateShard.TRANSLUCENT_TRANSPARENCY)
										.setOutputState(net.minecraft.client.renderer.RenderStateShard.MAIN_TARGET).createCompositeState(true));
						if (!printedLoadOk) {
							printedLoadOk = true;
						}
					});
		} catch (java.io.IOException e) {
			BLACKHOLE_SHADER = null;
			BLACKHOLE_RENDER_TYPE = null;
			if (!printedLoadFail) {
				printedLoadFail = true;
				e.printStackTrace();
			}
		} catch (Exception e) {
			BLACKHOLE_SHADER = null;
			BLACKHOLE_RENDER_TYPE = null;
			if (!printedLoadFail) {
				printedLoadFail = true;
				e.printStackTrace();
			}
		}
	}

	// captures the current frame and sets up shader uniforms for distortion rendering
	public static boolean beginFrameCapture(float timeSeconds) {
		if (BLACKHOLE_SHADER == null || BLACKHOLE_RENDER_TYPE == null)
			return false;
		net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
		if (mc.level == null)
			return false;
		com.mojang.blaze3d.pipeline.RenderTarget main = mc.getMainRenderTarget();
		int w = main.width;
		int h = main.height;
		if (w <= 0 || h <= 0)
			return false;
		ensureSceneCopy(w, h);
		copyMainToSceneCopy(main, SCENE_COPY, w, h);
		try {
			BLACKHOLE_SHADER.setSampler("SceneSampler", SCENE_COPY.getColorTextureId());
		} catch (Exception ignored) {
		}
		var outSizeU = BLACKHOLE_SHADER.getUniform("OutSize");
		if (outSizeU != null)
			outSizeU.set((float) w, (float) h);
		var timeU = BLACKHOLE_SHADER.getUniform("Time");
		if (timeU != null)
			timeU.set(timeSeconds);
		var strU = BLACKHOLE_SHADER.getUniform("Strength");
		if (strU != null)
			strU.set(1.0f);
		var flipU = BLACKHOLE_SHADER.getUniform("FlipY");
		if (flipU != null)
			flipU.set(1.0f);
		var sU = BLACKHOLE_SHADER.getUniform("EffectScale");
		if (sU != null)
			sU.set(1.6f);
		return true;
	}

	private static void ensureSceneCopy(int w, int h) {
		if (SCENE_COPY == null || w != lastW || h != lastH) {
			lastW = w;
			lastH = h;
			if (SCENE_COPY != null) {
				try {
					SCENE_COPY.destroyBuffers();
				} catch (Exception ignored) {
				}
			}
			SCENE_COPY = new com.mojang.blaze3d.pipeline.TextureTarget(w, h, false, false);
			try {
				SCENE_COPY.setFilterMode(org.lwjgl.opengl.GL11.GL_LINEAR);
			} catch (Exception ignored) {
			}
		}
	}

	private static void copyMainToSceneCopy(com.mojang.blaze3d.pipeline.RenderTarget from, com.mojang.blaze3d.pipeline.RenderTarget to, int w, int h) {
		org.lwjgl.opengl.GL30.glBindFramebuffer(org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER, from.frameBufferId);
		org.lwjgl.opengl.GL30.glBindFramebuffer(org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER, to.frameBufferId);
		org.lwjgl.opengl.GL30.glBlitFramebuffer(0, 0, w, h, 0, 0, w, h, org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT, org.lwjgl.opengl.GL11.GL_NEAREST);
		org.lwjgl.opengl.GL30.glBindFramebuffer(org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER, 0);
		org.lwjgl.opengl.GL30.glBindFramebuffer(org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER, 0);
		from.bindWrite(true);
	}
}
