
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MinefinityGauntletModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MinefinityGauntletMod.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEFINITY_CREATIVE_TAB_2 = REGISTRY.register("minefinity_creative_tab_2",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minefinity_gauntlet.minefinity_creative_tab_2")).icon(() -> new ItemStack(MinefinityGauntletModItems.POWER_STONE.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinefinityGauntletModItems.POWER_STONE.get());
				tabData.accept(MinefinityGauntletModItems.SPACE_STONE.get());
				tabData.accept(MinefinityGauntletModItems.TIME_STONE.get());
				tabData.accept(MinefinityGauntletModItems.REALITY_STONE.get());
				tabData.accept(MinefinityGauntletModItems.SOUL_STONE.get());
				tabData.accept(MinefinityGauntletModItems.MIND_STONE.get());
			})

					.build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEFINITY_CREATIVE_TAB_3 = REGISTRY.register("minefinity_creative_tab_3",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minefinity_gauntlet.minefinity_creative_tab_3")).icon(() -> new ItemStack(MinefinityGauntletModItems.TESERRACT.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinefinityGauntletModItems.TESERRACT.get());
			})

					.build());
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MINEFINITY_CREATIVE_TAB = REGISTRY.register("minefinity_creative_tab",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.minefinity_gauntlet.minefinity_creative_tab")).icon(() -> new ItemStack(MinefinityGauntletModItems.INFINITY_GAUNTLET.get())).displayItems((parameters, tabData) -> {
				tabData.accept(MinefinityGauntletModItems.INFINITY_GAUNTLET.get());
				tabData.accept(MinefinityGauntletModItems.COSMI_ROD.get());
				tabData.accept(MinefinityGauntletModItems.COSMI_ROD_POWER.get());
				tabData.accept(MinefinityGauntletModItems.UNIVERSAL_LAW_TESTITEM.get());
			})

					.build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {

			tabData.accept(MinefinityGauntletModItems.FAKE_BLOCK_ENTITY_SPAWN_EGG.get());

		}
	}
}
