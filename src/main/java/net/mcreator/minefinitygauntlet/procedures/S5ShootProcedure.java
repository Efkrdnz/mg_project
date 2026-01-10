package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

public class S5ShootProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double X = 0;
		double angle = 0;
		double Y = 0;
		double Z = 0;
		double Numerical_value = 0;
		double yaw = 0;
		if (entity instanceof Player) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinefinityGauntletModItems.S_5.get()
					|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == MinefinityGauntletModItems.INFINITY_GAUNTLET.get()) {
				if (!(entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).block_to_telekinesis.getBlock() == Blocks.AIR)) {
					Numerical_value = entity.getBbWidth() + 2.5;
					yaw = entity.getYRot();
					angle = entity.getXRot();
					for (int index0 = 0; index0 < 1; index0++) {
						X = x - Numerical_value * Math.cos(Math.toRadians(angle)) * Math.sin(Math.toRadians(yaw));
						Y = (y + 1.5) - Numerical_value * Math.sin(Math.toRadians(angle));
						Z = z + Numerical_value * Math.cos(Math.toRadians(angle)) * Math.cos(Math.toRadians(yaw));
						if (world instanceof ServerLevel _serverLevel) {
							Entity entityinstance = EntityType.FALLING_BLOCK.create(_serverLevel);
							if (entityinstance != null) {
								CompoundTag _compoundTag = entityinstance.saveWithoutId(new CompoundTag());
								_compoundTag.put("BlockState", NbtUtils.writeBlockState(entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).block_to_telekinesis));
								entityinstance.load(_compoundTag);
								entityinstance.setPos(X, Y, Z);
								entityinstance.getPersistentData().putString("id_block", (BuiltInRegistries.BLOCK.getKey(entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).block_to_telekinesis.getBlock()).toString()));
								entityinstance.getPersistentData().putString("ownerentity", (entity.getStringUUID()));
								for (int index1 = 0; index1 < 20; index1++) {
									entityinstance.setDeltaMovement(new Vec3((3 * entity.getLookAngle().x), (3 * entity.getLookAngle().y), (3 * entity.getLookAngle().z)));
								}
								_serverLevel.addFreshEntity(entityinstance);
							}
						}
						Numerical_value = Numerical_value + 0.2;
					}
					{
						MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
						_vars.block_to_telekinesis = Blocks.AIR.defaultBlockState();
						_vars.syncPlayerVariables(entity);
					}
				}
			}
		}
	}
}
