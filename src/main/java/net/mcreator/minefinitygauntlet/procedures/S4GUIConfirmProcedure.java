package net.mcreator.minefinitygauntlet.procedures;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModEntities;
import net.mcreator.minefinitygauntlet.entity.PortalEntity;

import java.util.HashMap;

public class S4GUIConfirmProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return;
		if (world instanceof ServerLevel _serverLevel) {
			Entity entityinstance = MinefinityGauntletModEntities.PORTAL.get().create(_serverLevel, null, BlockPos.containing(x + 3 * entity.getLookAngle().x, y + 1.5, z + 3 * entity.getLookAngle().z), MobSpawnType.MOB_SUMMONED, false, false);
			if (entityinstance != null) {
				entityinstance.setYRot(world.getRandom().nextFloat() * 360.0F);
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetX, (int) (new Object() {
						double convert(String s) {
							try {
								return Double.parseDouble(s.trim());
							} catch (Exception e) {
							}
							return 0;
						}
					}.convert(guistate.containsKey("textin:tppx") ? (String) guistate.get("textin:tppx") : "") * 10));
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetY, (int) (new Object() {
						double convert(String s) {
							try {
								return Double.parseDouble(s.trim());
							} catch (Exception e) {
							}
							return 0;
						}
					}.convert(guistate.containsKey("textin:tppy") ? (String) guistate.get("textin:tppy") : "") * 10));
				if (entityinstance instanceof PortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(PortalEntity.DATA_targetZ, (int) (new Object() {
						double convert(String s) {
							try {
								return Double.parseDouble(s.trim());
							} catch (Exception e) {
							}
							return 0;
						}
					}.convert(guistate.containsKey("textin:tppz") ? (String) guistate.get("textin:tppz") : "") * 10));
				if (entityinstance instanceof PortalEntity _datEntSetS)
					_datEntSetS.getEntityData().set(PortalEntity.DATA_master, (entity.getStringUUID()));
				_serverLevel.addFreshEntity(entityinstance);
			}
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
	}
}
