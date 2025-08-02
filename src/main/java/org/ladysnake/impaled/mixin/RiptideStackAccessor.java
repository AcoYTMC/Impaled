package org.ladysnake.impaled.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface RiptideStackAccessor {
    @Accessor("riptideStack")
    ItemStack riptideStack();
}
