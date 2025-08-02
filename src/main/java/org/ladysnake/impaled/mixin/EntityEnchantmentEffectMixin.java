package org.ladysnake.impaled.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.entity.DamageEntityEnchantmentEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.ladysnake.impaled.mixin.impaling.MobEntityMixin;
import org.ladysnake.impaled.mixin.impaling.PlayerEntityMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/// This cancels the damage done. We're overriding the damage in {@link PlayerEntityMixin} and {@link MobEntityMixin}
@Mixin(DamageEntityEnchantmentEffect.class)
public abstract class EntityEnchantmentEffectMixin implements EnchantmentEntityEffect {
    @WrapOperation(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean yuh(Entity instance, ServerWorld serverWorld, DamageSource source, float amount, Operation<Boolean> original) {
        return original.call(instance, serverWorld, source, 0.0F);
    }
}
