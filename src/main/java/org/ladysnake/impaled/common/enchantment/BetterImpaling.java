package org.ladysnake.impaled.common.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.ladysnake.impaled.common.item.HellforkItem;
import org.ladysnake.impaled.common.util.EnchantmentListener;

public final class BetterImpaling {
    public static float getAttackDamage(ItemStack stack, Entity target) {
        int impalingLevel = EnchantmentListener.getLevel(target.getWorld(), stack, "minecraft:impaling");

        if (impalingLevel > 0) {
            if (stack.getItem() instanceof HellforkItem) {
                if (isFireImmune(target)) {
                    return impalingLevel * 2F;
                }
            } else if (target.isTouchingWaterOrRain()) {
                return impalingLevel * 1.5F;
            }
        }

        return 0;
    }

    private static boolean isFireImmune(Entity target) {
        if (target.isFireImmune()) return true;
        if (!(target instanceof LivingEntity)) return false;
        return ((LivingEntity)target).hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }
}
