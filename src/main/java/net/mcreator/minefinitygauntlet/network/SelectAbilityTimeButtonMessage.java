
package net.mcreator.minefinitygauntlet.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityTimeMenu;
import net.mcreator.minefinitygauntlet.procedures.StoneCycleSpaceProcedure;
import net.mcreator.minefinitygauntlet.procedures.StoneCycleSoulProcedure;
import net.mcreator.minefinitygauntlet.procedures.StoneCycleRealityProcedure;
import net.mcreator.minefinitygauntlet.procedures.StoneCyclePowerProcedure;
import net.mcreator.minefinitygauntlet.procedures.StoneCycleMindProcedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPowerProcedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPower6Procedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPower5Procedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPower4Procedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPower3Procedure;
import net.mcreator.minefinitygauntlet.procedures.SlotSelectPower2Procedure;
import net.mcreator.minefinitygauntlet.procedures.SelectAbilityThisGUIIsClosedProcedure;
import net.mcreator.minefinitygauntlet.procedures.AbilitySelectTime2Procedure;
import net.mcreator.minefinitygauntlet.procedures.AbilitySelectTime1Procedure;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

import java.util.Map;
import java.util.HashMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record SelectAbilityTimeButtonMessage(int buttonID, int x, int y, int z, HashMap<String, String> textstate) implements CustomPacketPayload {

	public static final Type<SelectAbilityTimeButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "select_ability_time_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SelectAbilityTimeButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SelectAbilityTimeButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
		writeTextState(message.textstate, buffer);
	}, (RegistryFriendlyByteBuf buffer) -> new SelectAbilityTimeButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), readTextState(buffer)));
	@Override
	public Type<SelectAbilityTimeButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final SelectAbilityTimeButtonMessage message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				Player entity = context.player();
				int buttonID = message.buttonID;
				int x = message.x;
				int y = message.y;
				int z = message.z;
				HashMap<String, String> textstate = message.textstate;
				handleButtonAction(entity, buttonID, x, y, z, textstate);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z, HashMap<String, String> textstate) {
		Level world = entity.level();
		HashMap guistate = SelectAbilityTimeMenu.guistate;
		// connect EditBox and CheckBox to guistate
		for (Map.Entry<String, String> entry : textstate.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			guistate.put(key, value);
		}
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == -2) {

			SelectAbilityThisGUIIsClosedProcedure.execute(entity);
		}
		if (buttonID == 0) {

			SlotSelectPowerProcedure.execute(entity);
		}
		if (buttonID == 1) {

			SlotSelectPower2Procedure.execute(entity);
		}
		if (buttonID == 2) {

			SlotSelectPower3Procedure.execute(entity);
		}
		if (buttonID == 3) {

			SlotSelectPower4Procedure.execute(entity);
		}
		if (buttonID == 4) {

			SlotSelectPower5Procedure.execute(entity);
		}
		if (buttonID == 5) {

			SlotSelectPower6Procedure.execute(entity);
		}
		if (buttonID == 6) {

			AbilitySelectTime2Procedure.execute(entity);
		}
		if (buttonID == 7) {

			AbilitySelectTime1Procedure.execute(entity);
		}
		if (buttonID == 8) {

			StoneCyclePowerProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 9) {

			StoneCycleSpaceProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 10) {

			StoneCycleRealityProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 11) {

			StoneCycleMindProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 12) {

			StoneCycleSoulProcedure.execute(world, x, y, z, entity);
		}
	}

	private static void writeTextState(HashMap<String, String> map, RegistryFriendlyByteBuf buffer) {
		buffer.writeInt(map.size());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			writeComponent(buffer, Component.literal(entry.getKey()));
			writeComponent(buffer, Component.literal(entry.getValue()));
		}
	}

	private static HashMap<String, String> readTextState(RegistryFriendlyByteBuf buffer) {
		int size = buffer.readInt();
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < size; i++) {
			String key = readComponent(buffer).getString();
			String value = readComponent(buffer).getString();
			map.put(key, value);
		}
		return map;
	}

	private static Component readComponent(RegistryFriendlyByteBuf buffer) {
		return ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buffer);
	}

	private static void writeComponent(RegistryFriendlyByteBuf buffer, Component component) {
		ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buffer, component);
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinefinityGauntletMod.addNetworkMessage(SelectAbilityTimeButtonMessage.TYPE, SelectAbilityTimeButtonMessage.STREAM_CODEC, SelectAbilityTimeButtonMessage::handleData);
	}
}
