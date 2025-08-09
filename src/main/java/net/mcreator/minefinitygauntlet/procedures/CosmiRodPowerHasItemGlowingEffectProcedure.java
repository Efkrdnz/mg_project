package net.mcreator.minefinitygauntlet.procedures;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;

public class CosmiRodPowerHasItemGlowingEffectProcedure {
	public static boolean execute() {
		if (GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS) {
			return true;
		}
		return false;
	}
}
