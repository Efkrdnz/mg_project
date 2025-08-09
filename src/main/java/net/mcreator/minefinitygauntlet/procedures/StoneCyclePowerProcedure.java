package net.mcreator.minefinitygauntlet.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityPowerMenu;
import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;

import io.netty.buffer.Unpooled;

public class StoneCyclePowerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.SelectedStone = 1;
			_vars.syncPlayerVariables(entity);
		}
		{
			MinefinityGauntletModVariables.PlayerVariables _vars = entity.getData(MinefinityGauntletModVariables.PLAYER_VARIABLES);
			_vars.SelectedPower = 1;
			_vars.syncPlayerVariables(entity);
		}
		if (entity instanceof ServerPlayer _ent) {
			BlockPos _bpos = BlockPos.containing(x, y, z);
			_ent.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("SelectAbilityPower");
				}

				@Override
				public boolean shouldTriggerClientSideContainerClosingOnOpen() {
					return false;
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					return new SelectAbilityPowerMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
				}
			}, _bpos);
		}
	}
}
