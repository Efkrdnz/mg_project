package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModEntities;
import net.mcreator.minefinitygauntlet.entity.PortalEntity;

public class S3RightclickedOnBlockProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof ServerLevel _serverLevel) {
			Entity entityinstance = MinefinityGauntletModEntities.PORTAL.get().create(_serverLevel, null, BlockPos.containing(x + 0.5, y + 1.2, z + 0.5), MobSpawnType.MOB_SUMMONED, false, false);
			if (entityinstance != null) {
				entityinstance.setYRot(world.getRandom().nextFloat() * 360.0F);
				if (entityinstance instanceof PortalEntity _datEntSetS)
					_datEntSetS.getEntityData().set(PortalEntity.DATA_master, (entity.getStringUUID()));
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetX, (int) (10 * entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).ancX));
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetY, (int) (10 * entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).ancY));
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetZ, (int) (10 * entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES).ancZ));
				_serverLevel.addFreshEntity(entityinstance);
			}
		}
	}
}
