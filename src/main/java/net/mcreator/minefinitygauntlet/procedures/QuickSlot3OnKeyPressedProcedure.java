package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

public class QuickSlot3OnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _entity) ? _entity.isHolding(MinefinityGauntletModItems.INFINITY_GAUNTLET.get()) : false) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.SelectedStone = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).s3s;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.SelectedPower = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).s3p;
				_vars.syncPlayerVariables(entity);
			}
		}
	}
}
