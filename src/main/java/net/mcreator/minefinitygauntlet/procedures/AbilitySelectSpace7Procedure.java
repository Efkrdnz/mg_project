package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class AbilitySelectSpace7Procedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 0) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.SelectedPower = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 1) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s1s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s1p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 2) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s2s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s2p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 3) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s3s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s3p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 4) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s4s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s4p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 5) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s5s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s5p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).apending == 6) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s6s = 0;
				_vars.syncPlayerVariables(entity);
			}
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.s6p = 7;
				_vars.syncPlayerVariables(entity);
			}
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
	}
}
