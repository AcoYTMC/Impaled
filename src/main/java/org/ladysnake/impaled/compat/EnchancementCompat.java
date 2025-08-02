package org.ladysnake.impaled.compat;

import moriyashiine.enchancement.common.component.entity.LeechingTridentComponent;
import moriyashiine.enchancement.common.component.entity.TeleportOnHitComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.ladysnake.impaled.common.entity.ImpaledTridentEntity;

public class EnchancementCompat {
    public static final boolean enabled = FabricLoader.getInstance().isModLoaded("enchancement");

    public static void tryEnableEnchantments(ImpaledTridentEntity trident, LivingEntity user, ItemStack stack) {
        if (enabled) {
            LeechingTridentComponent.maybeSet(user, stack, trident);
            TeleportOnHitComponent.maybeSet(user, stack, trident);
        }
    }
}
