
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

import net.mcreator.minefinitygauntlet.procedures.QuickSlot3OnKeyPressedProcedure;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record QuickSlot3Message(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<QuickSlot3Message> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "key_quick_slot_3"));
	public static final StreamCodec<RegistryFriendlyByteBuf, QuickSlot3Message> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, QuickSlot3Message message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new QuickSlot3Message(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<QuickSlot3Message> type() {
		return TYPE;
	}

	public static void handleData(final QuickSlot3Message message, final IPayloadContext context) {
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

			QuickSlot3OnKeyPressedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinefinityGauntletMod.addNetworkMessage(QuickSlot3Message.TYPE, QuickSlot3Message.STREAM_CODEC, QuickSlot3Message::handleData);
	}
}
