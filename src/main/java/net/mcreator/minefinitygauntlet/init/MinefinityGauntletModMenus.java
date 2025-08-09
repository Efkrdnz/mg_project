
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import net.mcreator.minefinitygauntlet.world.inventory.UniversalLawGuiMenu;
import net.mcreator.minefinitygauntlet.world.inventory.StoneSelectGuiMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityTimeMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilitySpaceMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilitySoulMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityRealityMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityPowerMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityMindMenu;
import net.mcreator.minefinitygauntlet.world.inventory.SelectAbilityInfinityMenu;
import net.mcreator.minefinitygauntlet.world.inventory.S4GUIMenu;
import net.mcreator.minefinitygauntlet.world.inventory.LawCreationGuiMenu;
import net.mcreator.minefinitygauntlet.world.inventory.FindTpGUIMenu;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

public class MinefinityGauntletModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, MinefinityGauntletMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<S4GUIMenu>> S_4_GUI = REGISTRY.register("s_4_gui", () -> IMenuTypeExtension.create(S4GUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<StoneSelectGuiMenu>> STONE_SELECT_GUI = REGISTRY.register("stone_select_gui", () -> IMenuTypeExtension.create(StoneSelectGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilityPowerMenu>> SELECT_ABILITY_POWER = REGISTRY.register("select_ability_power", () -> IMenuTypeExtension.create(SelectAbilityPowerMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilitySpaceMenu>> SELECT_ABILITY_SPACE = REGISTRY.register("select_ability_space", () -> IMenuTypeExtension.create(SelectAbilitySpaceMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilityRealityMenu>> SELECT_ABILITY_REALITY = REGISTRY.register("select_ability_reality", () -> IMenuTypeExtension.create(SelectAbilityRealityMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilityTimeMenu>> SELECT_ABILITY_TIME = REGISTRY.register("select_ability_time", () -> IMenuTypeExtension.create(SelectAbilityTimeMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilitySoulMenu>> SELECT_ABILITY_SOUL = REGISTRY.register("select_ability_soul", () -> IMenuTypeExtension.create(SelectAbilitySoulMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilityMindMenu>> SELECT_ABILITY_MIND = REGISTRY.register("select_ability_mind", () -> IMenuTypeExtension.create(SelectAbilityMindMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<SelectAbilityInfinityMenu>> SELECT_ABILITY_INFINITY = REGISTRY.register("select_ability_infinity", () -> IMenuTypeExtension.create(SelectAbilityInfinityMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<UniversalLawGuiMenu>> UNIVERSAL_LAW_GUI = REGISTRY.register("universal_law_gui", () -> IMenuTypeExtension.create(UniversalLawGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<LawCreationGuiMenu>> LAW_CREATION_GUI = REGISTRY.register("law_creation_gui", () -> IMenuTypeExtension.create(LawCreationGuiMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<FindTpGUIMenu>> FIND_TP_GUI = REGISTRY.register("find_tp_gui", () -> IMenuTypeExtension.create(FindTpGUIMenu::new));
}
