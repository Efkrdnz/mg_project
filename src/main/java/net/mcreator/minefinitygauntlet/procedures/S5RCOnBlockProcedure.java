package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

public class S5RCOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		if (world.getBlockState(BlockPos.containing(x, y, z)).getDestroySpeed(world, BlockPos.containing(x, y, z)) != -1) {
			{
				MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
				_vars.block_to_telekinesis = blockstate;
				_vars.syncPlayerVariables(entity);
			}
			world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
		}
	}
}
