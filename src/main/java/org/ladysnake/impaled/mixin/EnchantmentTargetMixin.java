package org.ladysnake.impaled.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.ladysnake.impaled.common.item.AtlanItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/// This allows the Atlan to have any melee enchancement, alongside the default trident ones
@Mixin(Enchantment.class)
public abstract class EnchantmentTargetMixin {
    @Shadow public abstract boolean slotMatches(EquipmentSlot slot);

    @Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && stack.getItem() instanceof AtlanItem && this.slotMatches(EquipmentSlot.MAINHAND)) {
            cir.setReturnValue(true);
        }
    }
}
