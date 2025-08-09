package net.mcreator.minefinitygauntlet.procedures;

import org.checkerframework.checker.units.qual.s;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModEntities;
import net.mcreator.minefinitygauntlet.entity.HerePortalEntity;

import java.util.HashMap;

public class TpHerePortalPlaceProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return;
		if (world instanceof ServerLevel _serverLevel) {
			Entity entityinstance = MinefinityGauntletModEntities.HERE_PORTAL.get().create(_serverLevel, null, BlockPos.containing(x + 3 * entity.getLookAngle().x, y + 1.5 + 3 * entity.getLookAngle().y, z + 3 * entity.getLookAngle().z),
					MobSpawnType.MOB_SUMMONED, false, false);
			if (entityinstance != null) {
				entityinstance.setYRot(world.getRandom().nextFloat() * 360.0F);
				if (entityinstance instanceof HerePortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(HerePortalEntity.DATA_radius, (int) new Object() {
						double convert(String s) {
							try {
								return Double.parseDouble(s.trim());
							} catch (Exception e) {
							}
							return 0;
						}
					}.convert(guistate.containsKey("textin:rad") ? (String) guistate.get("textin:rad") : ""));
				if (entityinstance instanceof HerePortalEntity _datEntSetI)
					_datEntSetI.getEntityData().set(HerePortalEntity.DATA_maxlife, (int) (new Object() {
						double convert(String s) {
							try {
								return Double.parseDouble(s.trim());
							} catch (Exception e) {
							}
							return 0;
						}
					}.convert(guistate.containsKey("textin:dur") ? (String) guistate.get("textin:dur") : "") * 20));
				if (entityinstance instanceof HerePortalEntity _datEntSetS)
					_datEntSetS.getEntityData().set(HerePortalEntity.DATA_master, (entity.getStringUUID()));
				if (entityinstance instanceof HerePortalEntity _datEntSetS)
					_datEntSetS.getEntityData().set(HerePortalEntity.DATA_type, (guistate.containsKey("textin:type") ? (String) guistate.get("textin:type") : ""));
				_serverLevel.addFreshEntity(entityinstance);
			}
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
	}
}
