package org.ladysnake.impaled.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.TridentItem;
import net.minecraft.item.consume.UseAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.util.EnchantmentListener;

import java.util.function.Predicate;

public class MaelstromItem extends RangedWeaponItem {
    public MaelstromItem(Settings settings) {
        super(settings);
    }

    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(this.getDefaultStack(), 20 - (3 * EnchantmentListener.getLevel(world, stack, "minecraft:efficiency")));
        }

        return super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack maelstromStack, int remainingUseTicks) {
        super.usageTick(world, user, maelstromStack, remainingUseTicks);
        if (remainingUseTicks % (20 - (3 * EnchantmentListener.getLevel(world, maelstromStack, "minecraft:efficiency"))) == 0 && world instanceof ServerWorld) {
            if (user instanceof PlayerEntity player) {
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.size(); i++) {
                    ItemStack stackToThrow = player.getInventory().getStack(i);
                    if (!stackToThrow.isEmpty() && !EnchantmentListener.hasEnchantment(stackToThrow, "minecraft:loyalty") && stackToThrow.isIn(Impaled.TRIDENTS)) {
                        TridentEntity trident = null;
                        stackToThrow.damage(1, player);
                        maelstromStack.damage(1, player);

                        if (stackToThrow.getItem() instanceof ImpaledTridentItem item) {
                            trident = item.createTrident(world, user, stackToThrow);
                        } else if (stackToThrow.getItem() instanceof TridentItem) {
                            trident = new TridentEntity(world, user, stackToThrow);
                            trident.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 2.5F, 1.0F);
                        }

                        if (trident != null) {
                            if (player.getAbilities().creativeMode) {
                                trident.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                            }

                            world.spawnEntity(trident);
                            world.playSoundFromEntity(null, player, SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().creativeMode) {
                                player.getInventory().removeOne(stackToThrow);
                            }

                            player.incrementStat(Stats.USED.getOrCreateStat(this));
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public Predicate<ItemStack> getProjectiles() {
        return itemStack -> itemStack.isIn(Impaled.TRIDENTS);
    }

    public int getRange() {
        return 15;
    }
    
    public void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), yaw, 0.0F, speed, divergence);
    }
}
