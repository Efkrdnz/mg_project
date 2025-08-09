
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.mcreator.minefinitygauntlet.procedures.QuickSlot1OnKeyPressedProcedure;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record QuickSlot1Message(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<QuickSlot1Message> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "key_quick_slot_1"));
	public static final StreamCodec<RegistryFriendlyByteBuf, QuickSlot1Message> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, QuickSlot1Message message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new QuickSlot1Message(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<QuickSlot1Message> type() {
		return TYPE;
	}

	public static void handleData(final QuickSlot1Message message, final IPayloadContext context) {
		if (context.flow() == PacketFlow.SERVERBOUND) {
			context.enqueueWork(() -> {
				pressAction(context.player(), message.eventType, message.pressedms);
			}).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
	}

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			QuickSlot1OnKeyPressedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinefinityGauntletMod.addNetworkMessage(QuickSlot1Message.TYPE, QuickSlot1Message.STREAM_CODEC, QuickSlot1Message::handleData);
	}
}
