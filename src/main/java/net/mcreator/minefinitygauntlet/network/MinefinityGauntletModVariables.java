package net.mcreator.minefinitygauntlet.network;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;

import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MinefinityGauntletModVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MinefinityGauntletMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());
	public static CompoundTag temp_law_data = new CompoundTag();

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		MinefinityGauntletMod.addNetworkMessage(SavedDataSyncMessage.TYPE, SavedDataSyncMessage.STREAM_CODEC, SavedDataSyncMessage::handleData);
		MinefinityGauntletMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
	}

	@EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
			PlayerVariables clone = new PlayerVariables();
			clone.ancX = original.ancX;
			clone.ancY = original.ancY;
			clone.ancZ = original.ancZ;
			clone.SelectedStone = original.SelectedStone;
			clone.SelectedPower = original.SelectedPower;
			clone.s1s = original.s1s;
			clone.s1p = original.s1p;
			clone.s2s = original.s2s;
			clone.s2p = original.s2p;
			clone.s3s = original.s3s;
			clone.s3p = original.s3p;
			clone.s4s = original.s4s;
			clone.s4p = original.s4p;
			clone.s5s = original.s5s;
			clone.s5p = original.s5p;
			clone.s6s = original.s6s;
			clone.s6p = original.s6p;
			clone.block_to_telekinesis = original.block_to_telekinesis;
			clone.soul_barrier = original.soul_barrier;
			if (!event.isWasDeath()) {
				clone.beamPower = original.beamPower;
				clone.r1rc = original.r1rc;
				clone.teleblock = original.teleblock;
				clone.apending = original.apending;
				clone.beamSoul = original.beamSoul;
			}
			event.getEntity().setData(PLAYER_VARIABLES, clone);
		}

		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player) {
				SavedData mapdata = MapVariables.get(event.getEntity().level());
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (mapdata != null)
					PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(0, mapdata));
				if (worlddata != null)
					PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player) {
				SavedData worlddata = WorldVariables.get(event.getEntity().level());
				if (worlddata != null)
					PacketDistributor.sendToPlayer(player, new SavedDataSyncMessage(1, worlddata));
			}
		}
	}

	public static class WorldVariables extends SavedData {
		public static final String DATA_NAME = "minefinity_gauntlet_worldvars";
		public boolean TimeSlow = false;
		public boolean TimeStop = false;
		public CompoundTag universal_laws = new CompoundTag();

		public static WorldVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			WorldVariables data = new WorldVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			TimeSlow = nbt.getBoolean("TimeSlow");
			TimeStop = nbt.getBoolean("TimeStop");
			this.universal_laws = nbt.get("universal_laws") instanceof CompoundTag universal_laws ? universal_laws : new CompoundTag();
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("TimeSlow", TimeSlow);
			nbt.putBoolean("TimeStop", TimeStop);
			nbt.put("universal_laws", this.universal_laws);
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof ServerLevel level)
				PacketDistributor.sendToPlayersInDimension(level, new SavedDataSyncMessage(1, this));
		}

		static WorldVariables clientSide = new WorldVariables();

		public static WorldVariables get(LevelAccessor world) {
			if (world instanceof ServerLevel level) {
				return level.getDataStorage().computeIfAbsent(new SavedData.Factory<>(WorldVariables::new, WorldVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public static class MapVariables extends SavedData {
		public static final String DATA_NAME = "minefinity_gauntlet_mapvars";
		public boolean colorshift = false;
		public CompoundTag UniversalLaws = new CompoundTag();

		public static MapVariables load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
			MapVariables data = new MapVariables();
			data.read(tag, lookupProvider);
			return data;
		}

		public void read(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			colorshift = nbt.getBoolean("colorshift");
			this.UniversalLaws = nbt.get("UniversalLaws") instanceof CompoundTag UniversalLaws ? UniversalLaws : new CompoundTag();
		}

		@Override
		public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
			nbt.putBoolean("colorshift", colorshift);
			nbt.put("UniversalLaws", this.UniversalLaws);
			return nbt;
		}

		public void syncData(LevelAccessor world) {
			this.setDirty();
			if (world instanceof Level && !world.isClientSide())
				PacketDistributor.sendToAllPlayers(new SavedDataSyncMessage(0, this));
		}

		static MapVariables clientSide = new MapVariables();

		public static MapVariables get(LevelAccessor world) {
			if (world instanceof ServerLevelAccessor serverLevelAcc) {
				return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new SavedData.Factory<>(MapVariables::new, MapVariables::load), DATA_NAME);
			} else {
				return clientSide;
			}
		}
	}

	public record SavedDataSyncMessage(int dataType, SavedData data) implements CustomPacketPayload {
		public static final Type<SavedDataSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "saved_data_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SavedDataSyncMessage> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SavedDataSyncMessage message) -> {
			buffer.writeInt(message.dataType);
			if (message.data != null)
				buffer.writeNbt(message.data.save(new CompoundTag(), buffer.registryAccess()));
		}, (RegistryFriendlyByteBuf buffer) -> {
			int dataType = buffer.readInt();
			CompoundTag nbt = buffer.readNbt();
			SavedData data = null;
			if (nbt != null) {
				data = dataType == 0 ? new MapVariables() : new WorldVariables();
				if (data instanceof MapVariables mapVariables)
					mapVariables.read(nbt, buffer.registryAccess());
				else if (data instanceof WorldVariables worldVariables)
					worldVariables.read(nbt, buffer.registryAccess());
			}
			return new SavedDataSyncMessage(dataType, data);
		});

		@Override
		public Type<SavedDataSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final SavedDataSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> {
					if (message.dataType == 0)
						MapVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
					else
						WorldVariables.clientSide.read(message.data.save(new CompoundTag(), context.player().registryAccess()), context.player().registryAccess());
				}).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		public boolean beamPower = false;
		public boolean r1rc = false;
		public double ancX = 0;
		public double ancY = 0;
		public double ancZ = 0;
		public boolean teleblock = false;
		public double SelectedStone = 0;
		public double SelectedPower = 0;
		public double s1s = 0;
		public double s1p = 0;
		public double s2s = 0;
		public double s2p = 0;
		public double s3s = 0;
		public double s3p = 0;
		public double s4s = 0;
		public double s4p = 0;
		public double s5s = 0;
		public double s5p = 0;
		public double apending = 0;
		public double s6s = 0;
		public double s6p = 0;
		public BlockState block_to_telekinesis = Blocks.AIR.defaultBlockState();
		public boolean soul_barrier = false;
		public boolean beamSoul = false;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("beamPower", beamPower);
			nbt.putBoolean("r1rc", r1rc);
			nbt.putDouble("ancX", ancX);
			nbt.putDouble("ancY", ancY);
			nbt.putDouble("ancZ", ancZ);
			nbt.putBoolean("teleblock", teleblock);
			nbt.putDouble("SelectedStone", SelectedStone);
			nbt.putDouble("SelectedPower", SelectedPower);
			nbt.putDouble("s1s", s1s);
			nbt.putDouble("s1p", s1p);
			nbt.putDouble("s2s", s2s);
			nbt.putDouble("s2p", s2p);
			nbt.putDouble("s3s", s3s);
			nbt.putDouble("s3p", s3p);
			nbt.putDouble("s4s", s4s);
			nbt.putDouble("s4p", s4p);
			nbt.putDouble("s5s", s5s);
			nbt.putDouble("s5p", s5p);
			nbt.putDouble("apending", apending);
			nbt.putDouble("s6s", s6s);
			nbt.putDouble("s6p", s6p);
			nbt.put("block_to_telekinesis", NbtUtils.writeBlockState(block_to_telekinesis));
			nbt.putBoolean("soul_barrier", soul_barrier);
			nbt.putBoolean("beamSoul", beamSoul);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			beamPower = nbt.getBoolean("beamPower");
			r1rc = nbt.getBoolean("r1rc");
			ancX = nbt.getDouble("ancX");
			ancY = nbt.getDouble("ancY");
			ancZ = nbt.getDouble("ancZ");
			teleblock = nbt.getBoolean("teleblock");
			SelectedStone = nbt.getDouble("SelectedStone");
			SelectedPower = nbt.getDouble("SelectedPower");
			s1s = nbt.getDouble("s1s");
			s1p = nbt.getDouble("s1p");
			s2s = nbt.getDouble("s2s");
			s2p = nbt.getDouble("s2p");
			s3s = nbt.getDouble("s3s");
			s3p = nbt.getDouble("s3p");
			s4s = nbt.getDouble("s4s");
			s4p = nbt.getDouble("s4p");
			s5s = nbt.getDouble("s5s");
			s5p = nbt.getDouble("s5p");
			apending = nbt.getDouble("apending");
			s6s = nbt.getDouble("s6s");
			s6p = nbt.getDouble("s6p");
			block_to_telekinesis = NbtUtils.readBlockState(lookupProvider.lookupOrThrow(BuiltInRegistries.BLOCK.key()), nbt.getCompound("block_to_telekinesis"));
			soul_barrier = nbt.getBoolean("soul_barrier");
			beamSoul = nbt.getBoolean("beamSoul");
		}

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				PacketDistributor.sendToPlayer(serverPlayer, new PlayerVariablesSyncMessage(this));
		}
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MinefinityGauntletMod.MODID, "player_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec
				.of((RegistryFriendlyByteBuf buffer, PlayerVariablesSyncMessage message) -> buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
					PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables());
					message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
					return message;
				});

		@Override
		public Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				context.enqueueWork(() -> context.player().getData(PLAYER_VARIABLES).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}
}
