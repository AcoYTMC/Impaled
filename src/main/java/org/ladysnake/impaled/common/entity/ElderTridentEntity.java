package org.ladysnake.impaled.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ladysnake.impaled.common.util.EnchantmentListener;
import org.ladysnake.mialeemisc.entities.IPlayerTargeting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElderTridentEntity extends ImpaledTridentEntity {
    private final List<ItemStack> fetchedStacks = new ArrayList<>();
    public Entity tridentTarget;
    public boolean hasSearchedTarget;

    public ElderTridentEntity(EntityType<? extends ElderTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public Consumer<ItemStack> getStackFetcher() {
        return this.fetchedStacks::add;
    }

    @Override
    public void tick() {
        if (this.isInGround()) {
            this.setDealtDamage();
        }
        if (!this.hasSearchedTarget) {
            if (this.getOwner() != null) {
                if (this.getOwner() instanceof IPlayerTargeting targeting) {
                    this.tridentTarget = targeting.mialeeMisc$getLastTarget();
                } else if (this.getOwner() instanceof MobEntity mob) {
                    this.tridentTarget = mob.getTarget();
                }
                this.hasSearchedTarget = true;
            }
        } else {
            if (!this.hasDealtDamage()) {
                if (this.tridentTarget != null && this.tridentTarget.isAlive()) {
                    Vec3d vec3d = new Vec3d(this.tridentTarget.getX() - this.getX(), this.tridentTarget.getEyeY() - this.getY(), this.tridentTarget.getZ() - this.getZ());
                    this.setVelocity(this.getVelocity().multiply(0.9D).add(vec3d.normalize().multiply(0.25D)));
                }
                this.setNoGravity(this.tridentTarget != null && this.tridentTarget.isAlive());
            } else {
                this.setNoGravity(false);
            }
        }
        super.tick();
        Box box = this.getBoundingBox();
        List<Entity> list = this.getWorld().getOtherEntities(this, box);
        for (Entity entity : list) {
            if (entity instanceof ItemEntity itemEntity) {
                this.fetchedStacks.add(itemEntity.getStack());
                itemEntity.discard();
            }
        }
    }

    @Override
    public void setDealtDamage() {
        this.setNoGravity(false);
        this.tridentTarget = null;
        super.setDealtDamage();
    }

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld && EnchantmentListener.hasEnchantment(this.asItemStack(), "minecraft:channeling") && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 2))) {
                if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.ELDER_GUARDIAN_EFFECT, this.isSilent() ? 0.0F : 1.0F));
                }
            }
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        Entity entity = this.getOwner();
        if ((entity == null || entity.getUuid() == player.getUuid()) && this.getWorld() instanceof ServerWorld serverWorld) {
            for (ItemStack stack : this.fetchedStacks) {
                if (!player.getInventory().insertStack(stack)) {
                    this.dropStack(serverWorld, stack);
                }
            }
            this.fetchedStacks.clear();
        }
    }

    @Override
    public float getDragInWater() {
        return 1.0F;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (reason.shouldDestroy() && this.getWorld() instanceof ServerWorld serverWorld) {
            for (ItemStack fetchedStack : this.fetchedStacks) {
                this.dropStack(serverWorld, fetchedStack);
            }
        }
        super.remove(reason);
    }
}
