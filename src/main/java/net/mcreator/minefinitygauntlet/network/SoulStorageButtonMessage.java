
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

import net.mcreator.minefinitygauntlet.world.inventory.SoulStorageMenu;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease9Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease8Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease7Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease6Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease5Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease4Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease3Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease2Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease1Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoulStorageRelease0Procedure;
import net.mcreator.minefinitygauntlet.procedures.SoStorageButtonScrollUpProcedure;
import net.mcreator.minefinitygauntlet.procedures.SoStorageButtonScrollDownProcedure;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

import java.util.Map;
import java.util.HashMap;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record SoulStorageButtonMessage(int buttonID, int x, int y, int z, HashMap<String, String> textstate) implements CustomPacketPayload {

	public static final Type<SoulStorageButtonMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "soul_storage_buttons"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SoulStorageButtonMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SoulStorageButtonMessage message) -> {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
		writeTextState(message.textstate, buffer);
	}, (RegistryFriendlyByteBuf buffer) -> new SoulStorageButtonMessage(buffer.readInt(), buffer.readInt(), buffer.readInt(), buffer.readInt(), readTextState(buffer)));
	@Override
	public Type<SoulStorageButtonMessage> type() {
		return TYPE;
	}

	public static void handleData(final SoulStorageButtonMessage message, final IPayloadContext context) {
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
		HashMap guistate = SoulStorageMenu.guistate;
		// connect EditBox and CheckBox to guistate
		for (Map.Entry<String, String> entry : textstate.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			guistate.put(key, value);
		}
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			SoulStorageRelease0Procedure.execute(entity);
		}
		if (buttonID == 1) {

			SoulStorageRelease1Procedure.execute(entity);
		}
		if (buttonID == 2) {

			SoulStorageRelease2Procedure.execute(entity);
		}
		if (buttonID == 3) {

			SoulStorageRelease3Procedure.execute(entity);
		}
		if (buttonID == 4) {

			SoulStorageRelease4Procedure.execute(entity);
		}
		if (buttonID == 5) {

			SoulStorageRelease5Procedure.execute(entity);
		}
		if (buttonID == 6) {

			SoulStorageRelease6Procedure.execute(entity);
		}
		if (buttonID == 7) {

			SoulStorageRelease7Procedure.execute(entity);
		}
		if (buttonID == 8) {

			SoulStorageRelease8Procedure.execute(entity);
		}
		if (buttonID == 9) {

			SoulStorageRelease9Procedure.execute(entity);
		}
		if (buttonID == 10) {

			SoStorageButtonScrollUpProcedure.execute(entity);
		}
		if (buttonID == 11) {

			SoStorageButtonScrollDownProcedure.execute(entity);
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
		MinefinityGauntletMod.addNetworkMessage(SoulStorageButtonMessage.TYPE, SoulStorageButtonMessage.STREAM_CODEC, SoulStorageButtonMessage::handleData);
	}
}
