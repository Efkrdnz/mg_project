
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.minefinitygauntlet.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import net.minecraft.world.item.Item;

import net.mcreator.minefinitygauntlet.item.UniversalLawTestitemItem;
import net.mcreator.minefinitygauntlet.item.TimeStoneItem;
import net.mcreator.minefinitygauntlet.item.TestItemItem;
import net.mcreator.minefinitygauntlet.item.TeserractItem;
import net.mcreator.minefinitygauntlet.item.SpaceStoneItem;
import net.mcreator.minefinitygauntlet.item.SoulStoneItem;
import net.mcreator.minefinitygauntlet.item.S5Item;
import net.mcreator.minefinitygauntlet.item.S4Item;
import net.mcreator.minefinitygauntlet.item.S3Item;
import net.mcreator.minefinitygauntlet.item.S2Item;
import net.mcreator.minefinitygauntlet.item.S1Item;
import net.mcreator.minefinitygauntlet.item.RealityStoneItem;
import net.mcreator.minefinitygauntlet.item.R2Item;
import net.mcreator.minefinitygauntlet.item.R1Item;
import net.mcreator.minefinitygauntlet.item.PowerStoneItem;
import net.mcreator.minefinitygauntlet.item.P3Item;
import net.mcreator.minefinitygauntlet.item.P2Item;
import net.mcreator.minefinitygauntlet.item.MinefinityTabItem;
import net.mcreator.minefinitygauntlet.item.MindStoneItem;
import net.mcreator.minefinitygauntlet.item.InfinityGauntletItem;
import net.mcreator.minefinitygauntlet.item.CosmiRodPowerItem;
import net.mcreator.minefinitygauntlet.item.CosmiRodItem;
import net.mcreator.minefinitygauntlet.MinefinityGauntletMod;

public class MinefinityGauntletModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MinefinityGauntletMod.MODID);
	public static final DeferredItem<Item> TEST_ITEM = REGISTRY.register("test_item", TestItemItem::new);
	public static final DeferredItem<Item> EXP_SPAWN_EGG = REGISTRY.register("exp_spawn_egg", () -> new DeferredSpawnEggItem(MinefinityGauntletModEntities.EXP, -1, -1, new Item.Properties()));
	public static final DeferredItem<Item> P_2 = REGISTRY.register("p_2", P2Item::new);
	public static final DeferredItem<Item> P_3 = REGISTRY.register("p_3", P3Item::new);
	public static final DeferredItem<Item> R_1 = REGISTRY.register("r_1", R1Item::new);
	public static final DeferredItem<Item> R_2 = REGISTRY.register("r_2", R2Item::new);
	public static final DeferredItem<Item> S_1 = REGISTRY.register("s_1", S1Item::new);
	public static final DeferredItem<Item> POWER_STONE = REGISTRY.register("power_stone", PowerStoneItem::new);
	public static final DeferredItem<Item> MINEFINITY_TAB = REGISTRY.register("minefinity_tab", MinefinityTabItem::new);
	public static final DeferredItem<Item> SPACE_STONE = REGISTRY.register("space_stone", SpaceStoneItem::new);
	public static final DeferredItem<Item> TIME_STONE = REGISTRY.register("time_stone", TimeStoneItem::new);
	public static final DeferredItem<Item> REALITY_STONE = REGISTRY.register("reality_stone", RealityStoneItem::new);
	public static final DeferredItem<Item> SOUL_STONE = REGISTRY.register("soul_stone", SoulStoneItem::new);
	public static final DeferredItem<Item> MIND_STONE = REGISTRY.register("mind_stone", MindStoneItem::new);
	public static final DeferredItem<Item> S_2 = REGISTRY.register("s_2", S2Item::new);
	public static final DeferredItem<Item> S_3 = REGISTRY.register("s_3", S3Item::new);
	public static final DeferredItem<Item> S_4 = REGISTRY.register("s_4", S4Item::new);
	public static final DeferredItem<Item> S_5 = REGISTRY.register("s_5", S5Item::new);
	public static final DeferredItem<Item> FAKE_BLOCK_ENTITY_SPAWN_EGG = REGISTRY.register("fake_block_entity_spawn_egg", () -> new DeferredSpawnEggItem(MinefinityGauntletModEntities.FAKE_BLOCK_ENTITY, -1, -1, new Item.Properties()));
	public static final DeferredItem<Item> INFINITY_GAUNTLET = REGISTRY.register("infinity_gauntlet", InfinityGauntletItem::new);
	public static final DeferredItem<Item> TESERRACT = REGISTRY.register("teserract", TeserractItem::new);
	public static final DeferredItem<Item> COSMI_ROD = REGISTRY.register("cosmi_rod", CosmiRodItem::new);
	public static final DeferredItem<Item> COSMI_ROD_POWER = REGISTRY.register("cosmi_rod_power", CosmiRodPowerItem::new);
	public static final DeferredItem<Item> UNIVERSAL_LAW_TESTITEM = REGISTRY.register("universal_law_testitem", UniversalLawTestitemItem::new);
	// Start of user code block custom items
	// End of user code block custom items
}
