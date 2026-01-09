package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

public class ReturnPowerNameProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		if ((entity instanceof LivingEntity _entity) ? _entity.isHolding(MinefinityGauntletModItems.INFINITY_GAUNTLET.get()) : false) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 0) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Warp";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Set Point";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
					return "Tp Point";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
					return "Portal";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 5) {
					return "Block Telekinesis";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 6) {
					return "Pull Portal";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 7) {
					return "Singularity";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 8) {
					return "Normalize Space/Time";
				}
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 1) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Power Punch";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Power Launch";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
					return "Power Beam";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
					return "World Sunder";
				}
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 2) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Projectile Negation";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Athmosphere Switch";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
					if (entity.isShiftKeyDown()) {
						return "Heal (Self)";
					} else {
						return "Heal";
					}
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
					return "Block Shift";
				}
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 3) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Time Slow";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Time Stop";
				}
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 4) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Soul Barrier";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Vampiric Beam";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
					return "Soul Swap";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
					return "Soul Capture";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 5) {
					return "Soul Storage";
				}
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 5) {
				if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
					return "Know All";
				} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
					return "Mind Annihilation";
				}
			}
		}
		return "";
	}
}
