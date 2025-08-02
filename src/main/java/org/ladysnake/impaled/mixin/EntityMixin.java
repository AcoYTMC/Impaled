package org.ladysnake.impaled.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.impaled.common.Impaled;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract @Nullable ItemEntity dropStack(ServerWorld world, ItemStack stack);
    @Shadow public abstract World getWorld();
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Inject(method = "doesRenderOnFire", at = @At("RETURN"), cancellable = true)
    public void removePlayerFireRenderDuringHellforkRiptide(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof PlayerEntity player && player.isUsingRiptide() && Impaled.isHoldingFlamefork(player)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/entity/ItemEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"), cancellable = true)
    protected void impaled$dropStack(ServerWorld world, ItemStack stack, Vec3d offset, CallbackInfoReturnable<ItemEntity> cir) {
        // overridden in LivingEntityMixin
    }

    @Inject(method = "isOnFire", at = @At("RETURN"), cancellable = true)
    public void isOnFire(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof PlayerEntity player && player.isUsingRiptide() && Impaled.isHoldingFlamefork(player)) {
            cir.setReturnValue(true);
        }
    }
}
