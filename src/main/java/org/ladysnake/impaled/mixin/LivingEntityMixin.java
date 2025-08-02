package org.ladysnake.impaled.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.entity.ElderTridentEntity;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.impaled.common.util.EnchantmentListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Unique private @Nullable Consumer<ItemStack> impaled$dropSink;

    @Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldDropLoot()Z"))
    private void drop(ServerWorld world, DamageSource source, CallbackInfo ci) {
        Entity directSource = source.getSource();
        LivingEntity living = (LivingEntity)(Object)this;

        if (directSource instanceof ElderTridentEntity entity) {
            this.impaled$dropSink = entity.getStackFetcher();
        }

        if (living instanceof ElderGuardianEntity && (directSource instanceof PlayerEntity player && player.getMainHandStack().isIn(Impaled.TRIDENTS) || (directSource instanceof TridentEntity trident && EnchantmentListener.hasEnchantment(trident.getItemStack(), "minecraft:loyalty")))) {
            this.dropStack(world, new ItemStack(ImpaledItems.ELDER_GUARDIAN_EYE));
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        }
    }

    @Inject(method = "drop", at = @At("RETURN"))
    private void endDrop(ServerWorld world, DamageSource damageSource, CallbackInfo ci) {
        this.impaled$dropSink = null;
    }

    @Override
    protected void impaled$dropStack(ServerWorld world, ItemStack stack, Vec3d offset, CallbackInfoReturnable<ItemEntity> cir) {
        if (this.impaled$dropSink != null) {
            this.impaled$dropSink.accept(stack);
            cir.setReturnValue(null);
        }
    }
}