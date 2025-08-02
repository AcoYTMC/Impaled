package org.ladysnake.impaled.common.item;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.impaled.common.entity.ImpaledTridentEntity;
import org.ladysnake.impaled.compat.EnchancementCompat;
import org.ladysnake.impaled.compat.ImpaledConfig;

import java.util.Objects;

public class ImpaledTridentItem extends TridentItem {
    EntityType<? extends ImpaledTridentEntity> type;

    public ImpaledTridentItem(Item.Settings settings, EntityType<? extends ImpaledTridentEntity> entityType) {
        super(settings);
        this.type = entityType;
    }

    public EntityType<? extends ImpaledTridentEntity> getEntityType() {
        return type;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (ImpaledConfig.vanillaTridentLogic) {
            if (user instanceof PlayerEntity player) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                if (i < 10) {
                    return false;
                } else {
                    float f = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                    if (ModConfig.rebalanceEnchantments) f *= 2 / 3F;

                    if (f > 0.0F && !canRiptide(player)) {
                        return false;
                    } else if (stack.willBreakNextUse()) {
                        return false;
                    } else {
                        RegistryEntry<SoundEvent> registryEntry = EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.TRIDENT_SOUND)
                                .orElse(SoundEvents.ITEM_TRIDENT_THROW);
                        player.incrementStat(Stats.USED.getOrCreateStat(this));
                        if (world instanceof ServerWorld) {
                            stack.damage(1, player);
                            if (f == 0.0F) {
                                ItemStack itemStack = stack.splitUnlessCreative(1, player);
                                ImpaledTridentEntity trident = createTrident(world, user, itemStack);
                                if (player.isInCreativeMode()) {
                                    trident.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                }

                                world.playSoundFromEntity(null, trident, registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                                return true;
                            }
                        }

                        if (f > 0.0F) {
                            float g = player.getYaw();
                            float h = player.getPitch();
                            float j = -MathHelper.sin(g * (float) (Math.PI / 180.0)) * MathHelper.cos(h * (float) (Math.PI / 180.0));
                            float k = -MathHelper.sin(h * (float) (Math.PI / 180.0));
                            float l = MathHelper.cos(g * (float) (Math.PI / 180.0)) * MathHelper.cos(h * (float) (Math.PI / 180.0));
                            float m = MathHelper.sqrt(j * j + k * k + l * l);
                            j *= f / m;
                            k *= f / m;
                            l *= f / m;
                            player.addVelocity(j, k, l);
                            player.useRiptide(20, 8.0F, stack);
                            if (player.isOnGround()) {
                                float n = 1.1999999F;
                                player.move(MovementType.SELF, new Vec3d(0.0, 1.1999999F, 0.0));
                            }

                            world.playSoundFromEntity(null, player, registryEntry.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            if (user instanceof PlayerEntity player) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                if (i >= 10) {
                    float j = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
                    if (ModConfig.rebalanceEnchantments) j *= 2 / 3F;

                    if (j <= 0 || canRiptide(player)) {
                        if (!world.isClient) {
                            stack.damage(1, player);
                            if (j == 0) {
                                ImpaledTridentEntity trident = createTrident(world, player, stack);

                                if (player.getAbilities().creativeMode) {
                                    trident.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                                }

                                world.spawnEntity(trident);
                                world.playSoundFromEntity(null, trident, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                                if (!player.getAbilities().creativeMode) {
                                    player.getInventory().removeOne(stack);
                                }
                                return true;
                            }
                        }

                        player.incrementStat(Stats.USED.getOrCreateStat(this));
                        if (j > 0) {
                            float f = player.getYaw();
                            float g = player.getPitch();
                            float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                            float k = -MathHelper.sin(g * 0.017453292F);
                            float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                            float m = MathHelper.sqrt(h * h + k * k + l * l);
                            float n = 3.0F * ((1.0F + j) / 4.0F);
                            h *= n / m;
                            k *= n / m;
                            l *= n / m;
                            player.addVelocity(h, k, l);
                            player.useRiptide(20, 8.0F, stack);
                            if (player.isOnGround()) {
                                player.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                            }

                            SoundEvent soundEvent3;
                            if (j >= 3) {
                                soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_3.value();
                            } else if (j == 2) {
                                soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_2.value();
                            } else {
                                soundEvent3 = SoundEvents.ITEM_TRIDENT_RIPTIDE_1.value();
                            }

                            world.playSoundFromEntity(null, player, soundEvent3, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
    }

    protected boolean canRiptide(PlayerEntity playerEntity) {
        return playerEntity.isTouchingWaterOrRain();
    }

    public @NotNull ImpaledTridentEntity createTrident(World world, LivingEntity user, ItemStack stack) {
        ImpaledTridentEntity trident = Objects.requireNonNull(this.type.create(world, SpawnReason.SPAWN_ITEM_USE));
        trident.setTridentAttributes(stack);
        trident.setOwner(user);
        trident.setTridentStack(stack);
        trident.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 2.5F, 1.0F);
        trident.updatePosition(user.getX(), user.getEyeY() - 0.1, user.getZ());
        EnchancementCompat.tryEnableEnchantments(trident, user, stack);
        return trident;
    }
}
