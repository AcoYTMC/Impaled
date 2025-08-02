package org.ladysnake.impaled.common.util;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static net.minecraft.component.DataComponentTypes.ENCHANTMENTS;

public class EnchantmentListener {
    public static boolean hasEnchantment(ItemStack stack, String enchantKey) {
        final var enchantments = stack.getOrDefault(ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getEnchantmentEntries();

        for (final var entry : enchantments) {
            String enchant = entry.getKey().getIdAsString();

            if (enchant.contains(enchantKey)) {
                return true;
            }
        }

        return false;
    }

    public static int getLevel(World world, ItemStack stack, String enchantKey) {
        return hasEnchantment(stack, enchantKey) ?
                EnchantmentHelper.getEnchantments(stack)
                        .getLevel(
                                world.getRegistryManager()
                                        .getEntryOrThrow(
                                                RegistryKey.of(
                                                        RegistryKeys.ENCHANTMENT,
                                                        Identifier.of(enchantKey)
                                                )
                                        )
                        ) : 0;
    }
}
