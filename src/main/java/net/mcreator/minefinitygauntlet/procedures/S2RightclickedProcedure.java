package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class S2RightclickedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.ancX = entity.getX();
			_vars.syncPlayerVariables(entity);
		}
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.ancY = entity.getY();
			_vars.syncPlayerVariables(entity);
		}
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.ancZ = entity.getZ();
			_vars.syncPlayerVariables(entity);
		}
	}
}
