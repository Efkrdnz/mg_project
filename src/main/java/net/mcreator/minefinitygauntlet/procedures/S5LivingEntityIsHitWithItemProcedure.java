package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class S5LivingEntityIsHitWithItemProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		entity.setDeltaMovement(new Vec3((5 * sourceentity.getLookAngle().x), 2, (5 * sourceentity.getLookAngle().z)));
	}
}
