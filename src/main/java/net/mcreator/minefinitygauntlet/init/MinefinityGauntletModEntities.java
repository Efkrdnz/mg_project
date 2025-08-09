
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

import net.mcreator.minefinitygauntlet.entity.PortalEntity;
import net.mcreator.minefinitygauntlet.entity.InvEntity;
import net.mcreator.minefinitygauntlet.entity.HerePortalEntity;
import net.mcreator.minefinitygauntlet.entity.FakeBlockEntityEntity;
import net.mcreator.minefinitygauntlet.entity.ExpEntity;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MinefinityGauntletModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, MinefinityGauntletMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<InvEntity>> INV = register("inv",
			EntityType.Builder.<InvEntity>of(InvEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<ExpEntity>> EXP = register("exp",
			EntityType.Builder.<ExpEntity>of(ExpEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.1f, 0.1f));
	public static final DeferredHolder<EntityType<?>, EntityType<PortalEntity>> PORTAL = register("portal",
			EntityType.Builder.<PortalEntity>of(PortalEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<FakeBlockEntityEntity>> FAKE_BLOCK_ENTITY = register("fake_block_entity",
			EntityType.Builder.<FakeBlockEntityEntity>of(FakeBlockEntityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(1f, 1f));
	public static final DeferredHolder<EntityType<?>, EntityType<HerePortalEntity>> HERE_PORTAL = register("here_portal",
			EntityType.Builder.<HerePortalEntity>of(HerePortalEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));

	// Start of user code block custom entities
	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		ExpEntity.init(event);
		PortalEntity.init(event);
		FakeBlockEntityEntity.init(event);
		HerePortalEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(EXP.get(), ExpEntity.createAttributes().build());
		event.put(PORTAL.get(), PortalEntity.createAttributes().build());
		event.put(FAKE_BLOCK_ENTITY.get(), FakeBlockEntityEntity.createAttributes().build());
		event.put(HERE_PORTAL.get(), HerePortalEntity.createAttributes().build());
	}
}
