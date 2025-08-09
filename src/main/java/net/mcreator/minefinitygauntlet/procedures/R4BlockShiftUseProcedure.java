package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;

public class R4BlockShiftUseProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		world.setBlock(BlockPos.containing(x, y, z),
				(BuiltInRegistries.BLOCK.getOrCreateTag(BlockTags.create(ResourceLocation.parse("randomblocks"))).getRandomElement(RandomSource.create()).orElseGet(() -> BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.AIR)).value()).defaultBlockState(),
				3);
	}
}
