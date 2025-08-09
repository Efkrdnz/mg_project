package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class ScreenShakeOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double rand = 0;
		rand = Mth.nextInt(RandomSource.create(), 1, 8);
		if (rand == 1) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() + 0.5));
				_ent.setXRot((float) (entity.getXRot() + 0));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 2) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() - 0.5));
				_ent.setXRot((float) (entity.getXRot() + 0));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 3) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() + 0));
				_ent.setXRot((float) (entity.getXRot() + 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 4) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() + 0));
				_ent.setXRot((float) (entity.getXRot() - 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 5) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() + 0.5));
				_ent.setXRot((float) (entity.getXRot() + 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 6) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() + 0.5));
				_ent.setXRot((float) (entity.getXRot() - 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 7) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() - 0.5));
				_ent.setXRot((float) (entity.getXRot() + 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
		if (rand == 8) {
			{
				Entity _ent = entity;
				_ent.setYRot((float) (entity.getYRot() - 0.5));
				_ent.setXRot((float) (entity.getXRot() - 0.5));
				_ent.setYBodyRot(_ent.getYRot());
				_ent.setYHeadRot(_ent.getYRot());
				_ent.yRotO = _ent.getYRot();
				_ent.xRotO = _ent.getXRot();
				if (_ent instanceof LivingEntity _entity) {
					_entity.yBodyRotO = _entity.getYRot();
					_entity.yHeadRotO = _entity.getYRot();
				}
			}
		}
	}
}
