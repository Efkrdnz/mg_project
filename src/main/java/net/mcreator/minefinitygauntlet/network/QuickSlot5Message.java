
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

import net.mcreator.minefinitygauntlet.procedures.QuickSlot5OnKeyPressedProcedure;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record QuickSlot5Message(int eventType, int pressedms) implements CustomPacketPayload {
	public static final Type<QuickSlot5Message> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "key_quick_slot_5"));
	public static final StreamCodec<RegistryFriendlyByteBuf, QuickSlot5Message> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, QuickSlot5Message message) -> {
		buffer.writeInt(message.eventType);
		buffer.writeInt(message.pressedms);
	}, (RegistryFriendlyByteBuf buffer) -> new QuickSlot5Message(buffer.readInt(), buffer.readInt()));

	@Override
	public Type<QuickSlot5Message> type() {
		return TYPE;
	}

	public static void handleData(final QuickSlot5Message message, final IPayloadContext context) {
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

			QuickSlot5OnKeyPressedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MinefinityGauntletMod.addNetworkMessage(QuickSlot5Message.TYPE, QuickSlot5Message.STREAM_CODEC, QuickSlot5Message::handleData);
	}
}
