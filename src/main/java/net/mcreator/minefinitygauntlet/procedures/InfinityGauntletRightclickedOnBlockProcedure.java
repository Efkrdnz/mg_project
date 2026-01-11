package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class InfinityGauntletRightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 0) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
				S2RightclickedProcedure.execute(entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
				S3RightclickedOnBlockProcedure.execute(world, x, y, z, entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
				S4RightclickedProcedure.execute(world, x, y, z, entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 5) {
				S5RCOnBlockProcedure.execute(world, x, y, z, blockstate, entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 7) {
				EnableSingularityProcedure.execute(entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 8) {
				EnableSingularityShrinkProcedure.execute(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 1) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				TestItemRightclickedProcedure.execute(entity);
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
				PowerLaunchProcedure.execute(world, entity);
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
				WorldSunderUseProcedure.execute(world, x, y, z, entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 2) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				R1RightclickedProcedure.execute(entity);
			}
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
				R4BlockShiftUseProcedure.execute(world, x, y, z);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 3) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				T1UseSlowProcedure.execute(world);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 2) {
				T2UseStopProcedure.execute(world, x, y, z);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 3) {
				TimeAnchorSetProcedure.execute(entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 4) {
				TimeAnchorReturnProcedure.execute(entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 4) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				So1UseRCProcedure.execute(entity);
			} else if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 5) {
				SoulStorageOpenProcedure.execute(world, x, y, z, entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 5) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				PlayerTrackerOpenProcedure.execute(world, entity);
			}
		}
		if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedStone == 6) {
			if (entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).SelectedPower == 1) {
				ResetUniverseProcedure.execute(world, entity);
			}
		}
	}
}
