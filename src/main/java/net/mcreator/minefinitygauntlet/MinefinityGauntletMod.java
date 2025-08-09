package net.mcreator.minefinitygauntlet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.util.Tuple;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.Minecraft;

import net.mcreator.minefinitygauntlet.network.MinefinityGauntletModVariables;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModTabs;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModParticleTypes;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModMobEffects;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModMenus;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModEntities;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import java.lang.reflect.Field;

import java.io.StringWriter;
import java.io.PrintWriter;

@Mod("minefinity_gauntlet")
public class MinefinityGauntletMod {
	public static final Logger LOGGER = LogManager.getLogger(MinefinityGauntletMod.class);
	public static final String MODID = "minefinity_gauntlet";

	public MinefinityGauntletMod(IEventBus modEventBus) {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::registerNetworking);

		MinefinityGauntletModItems.REGISTRY.register(modEventBus);
		MinefinityGauntletModEntities.REGISTRY.register(modEventBus);
		MinefinityGauntletModTabs.REGISTRY.register(modEventBus);
		MinefinityGauntletModVariables.ATTACHMENT_TYPES.register(modEventBus);

		MinefinityGauntletModMobEffects.REGISTRY.register(modEventBus);
		MinefinityGauntletModMenus.REGISTRY.register(modEventBus);
		MinefinityGauntletModParticleTypes.REGISTRY.register(modEventBus);

		// Start of user code block mod init
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods
	private static boolean networkingRegistered = false;
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

	private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
	}

	public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerNetworking(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MODID);
		MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		networkingRegistered = true;
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}

	public static record GuiSyncMessage(String editbox, String value) implements CustomPacketPayload {
		public static final Type<GuiSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "gui_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, GuiSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, GuiSyncMessage message) -> {
			writeComponent(buffer, Component.literal(message.editbox));
			writeComponent(buffer, Component.literal(message.value));
		}, (RegistryFriendlyByteBuf buffer) -> {
			String editbox = readComponent(buffer).getString();
			String value = readComponent(buffer).getString();
			return new GuiSyncMessage(editbox, value);
		});

		@Override
		public Type<GuiSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final GuiSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND) {
				context.enqueueWork(() -> {
					Screen currentScreen = Minecraft.getInstance().screen;
					Map<String, EditBox> textFieldsMap = new HashMap<>();
					if (currentScreen != null) {
						Field[] fields = currentScreen.getClass().getDeclaredFields();
						for (Field field : fields) {
							if (EditBox.class.isAssignableFrom(field.getType())) {
								try {
									field.setAccessible(true);
									EditBox textField = (EditBox) field.get(currentScreen);
									if (textField != null) {
										textFieldsMap.put(field.getName(), textField);
									}
								} catch (IllegalAccessException ex) {
									StringWriter sw = new StringWriter();
									PrintWriter pw = new PrintWriter(sw);
									ex.printStackTrace(pw);
									String exceptionAsString = sw.toString();
									MinefinityGauntletMod.LOGGER.error(exceptionAsString);
								}
							}
						}
					}
					if (textFieldsMap.get(message.editbox) != null) {
						textFieldsMap.get(message.editbox).setValue(message.value);
					}
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}

		private static Component readComponent(RegistryFriendlyByteBuf buffer) {
			return ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buffer);
		}

		private static void writeComponent(RegistryFriendlyByteBuf buffer, Component component) {
			ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buffer, component);
		}
	}

	@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
	public static class GuiSyncInit {
		@SubscribeEvent
		public static void init(FMLCommonSetupEvent event) {
			MinefinityGauntletMod.addNetworkMessage(GuiSyncMessage.TYPE, GuiSyncMessage.STREAM_CODEC, GuiSyncMessage::handleData);
		}
	}
}
