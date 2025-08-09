
package net.mcreator.minefinitygauntlet.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class MinefinityTabItem extends Item {
	public MinefinityTabItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}
}
