package net.mcreator.minefinitygauntlet.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.minefinitygauntlet.mixins.DisableHotbarKeymappingMixin;
import net.mcreator.minefinitygauntlet.init.MinefinityGauntletModItems;

@Mixin(KeyMapping.class)
public abstract class DisableHotbarKeymappingMixin {
	@Inject(method = "consumeClick", at = @At("HEAD"), cancellable = true)
	public void inject1(CallbackInfoReturnable<Boolean> cir) {
		KeyMapping keyMapping = (KeyMapping) (Object) this;
		String keyName = keyMapping.getName();
		// ✅ Check if the key is one of the hotbar keys (1-5)
		if (keyName.equals("key.hotbar.1") || keyName.equals("key.hotbar.2") || keyName.equals("key.hotbar.3") || keyName.equals("key.hotbar.4") || keyName.equals("key.hotbar.5") || keyName.equals("key.hotbar.6")) {
			Player player = Minecraft.getInstance().player;
			if (player != null) {
				ItemStack mainHandItem = player.getMainHandItem();
				// ✅ Check if the player is holding the Infinity Gauntlet
				if (mainHandItem.is(MinefinityGauntletModItems.INFINITY_GAUNTLET.get())) {
					cir.setReturnValue(false); // ✅ Prevents switching to hotbar slots 1-5
				}
			}
		}
	}
}
