
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.mcreator.minefinitygauntlet.client.gui.UniversalLawGuiScreen;
import net.mcreator.minefinitygauntlet.client.gui.StoneSelectGuiScreen;
import net.mcreator.minefinitygauntlet.client.gui.SoulStorageScreen;
import net.mcreator.minefinitygauntlet.client.gui.SoulStoneGUIScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilityTimeScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilitySpaceScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilitySoulScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilityRealityScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilityPowerScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilityMindScreen;
import net.mcreator.minefinitygauntlet.client.gui.SelectAbilityInfinityScreen;
import net.mcreator.minefinitygauntlet.client.gui.S4GUIScreen;
import net.mcreator.minefinitygauntlet.client.gui.LawCreationGuiScreen;
import net.mcreator.minefinitygauntlet.client.gui.FindTpGUIScreen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MinefinityGauntletModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(MinefinityGauntletModMenus.S_4_GUI.get(), S4GUIScreen::new);
		event.register(MinefinityGauntletModMenus.STONE_SELECT_GUI.get(), StoneSelectGuiScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_POWER.get(), SelectAbilityPowerScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_SPACE.get(), SelectAbilitySpaceScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_REALITY.get(), SelectAbilityRealityScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_TIME.get(), SelectAbilityTimeScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_SOUL.get(), SelectAbilitySoulScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_MIND.get(), SelectAbilityMindScreen::new);
		event.register(MinefinityGauntletModMenus.SELECT_ABILITY_INFINITY.get(), SelectAbilityInfinityScreen::new);
		event.register(MinefinityGauntletModMenus.UNIVERSAL_LAW_GUI.get(), UniversalLawGuiScreen::new);
		event.register(MinefinityGauntletModMenus.LAW_CREATION_GUI.get(), LawCreationGuiScreen::new);
		event.register(MinefinityGauntletModMenus.FIND_TP_GUI.get(), FindTpGUIScreen::new);
		event.register(MinefinityGauntletModMenus.SOUL_STONE_GUI.get(), SoulStoneGUIScreen::new);
		event.register(MinefinityGauntletModMenus.SOUL_STORAGE.get(), SoulStorageScreen::new);
	}
}
