package org.ladysnake.impaled.common.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.entity.ImpaledTridentEntity;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.impaled.common.util.EnchantmentListener;

import java.util.Map;

public class HellforkItem extends ImpaledTridentItem {
    private static final Map<Block, BlockState> BLOCK_STATE_MAP = Maps.newHashMap(ImmutableMap.of(
            Blocks.SOUL_CAMPFIRE, Blocks.CAMPFIRE.getDefaultState(),
            Blocks.SOUL_LANTERN, Blocks.LANTERN.getDefaultState(),
            Blocks.SOUL_TORCH, Blocks.TORCH.getDefaultState(),
            Blocks.SOUL_WALL_TORCH, Blocks.WALL_TORCH.getDefaultState()
    ));

    private static final Map<Block, BlockState> REVERSE_BLOCK_STATE_MAP = Maps.newHashMap(ImmutableMap.of(
            Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE.getDefaultState(),
            Blocks.LANTERN, Blocks.SOUL_LANTERN.getDefaultState(),
            Blocks.TORCH, Blocks.SOUL_TORCH.getDefaultState(),
            Blocks.WALL_TORCH, Blocks.SOUL_WALL_TORCH.getDefaultState()
    ));

    public HellforkItem(Item.Settings settings, EntityType<? extends ImpaledTridentEntity> entityType) {
        super(settings, entityType);
    }

    @Override
    protected boolean canRiptide(PlayerEntity player) {
        return Impaled.isHoldingSoulfork(player) || player.isInLava() || player.isOnFire();
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity player && player.isUsingRiptide() && stack.getItem() == ImpaledItems.SOULFORK) {
            if (player.experienceLevel <= 0) {
                if (world instanceof ServerWorld serverWorld) {
                    user.damage(serverWorld, world.getDamageSources().impaledSources().hellforkHeat(), 2f);
                    user.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
                }
            } else {
                player.addExperienceLevels(-1);
            }
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.SOUL, user.getX(), user.getY(), user.getZ(), 20, user.getRandom().nextFloat(), user.getRandom().nextGaussian(), user.getRandom().nextFloat(), user.getRandom().nextFloat() / 10f);
            }
            user.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), 1.0f, 1.0f);
            return true;
        }

        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.getDamage() > stack.getMaxDamage() - 1) {
            return ActionResult.FAIL;
        } else if (EnchantmentListener.hasEnchantment(stack, "minecraft:riptide") && !user.isInLava() && !user.isOnFire() && !(stack.getItem() == ImpaledItems.SOULFORK)) {
            return ActionResult.FAIL;
        } else {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();
        if (BLOCK_STATE_MAP.containsKey(state.getBlock()) && context.getStack().getItem() == ImpaledItems.HELLFORK && player != null) {
            ItemStack soulfork = context.getStack().copyComponentsToNewStack(ImpaledItems.SOULFORK, 1);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

            player.setStackInHand(context.getHand(), soulfork);

            BlockState replacedState = BLOCK_STATE_MAP.get(state.getBlock());
            for (Property property : state.getProperties()) {
                if (replacedState.getProperties().contains(property)) {
                    replacedState = replacedState.with(property, state.get(property));
                }
            }

            world.setBlockState(pos, replacedState);

            for (int i = 0; i < 20; i++) {
                world.addParticleClient(ParticleTypes.SOUL, pos.getX() + .5 + world.random.nextGaussian() / 10, pos.getY() + .5 + world.random.nextGaussian() / 10, pos.getZ() + .5 + world.random.nextGaussian() / 10, 0, world.random.nextFloat() / 10, 0);
            }
            return ActionResult.SUCCESS;
        } else if (REVERSE_BLOCK_STATE_MAP.containsKey(state.getBlock()) && context.getStack().getItem() == ImpaledItems.SOULFORK && player != null) {
            ItemStack hellfork = context.getStack().copyComponentsToNewStack(ImpaledItems.HELLFORK, 1);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

            player.setStackInHand(context.getHand(), hellfork);

            BlockState replacedState = REVERSE_BLOCK_STATE_MAP.get(state.getBlock());
            for (Property property : state.getProperties()) {
                if (replacedState.getProperties().contains(property)) {
                    replacedState = replacedState.with(property, state.get(property));
                }
            }

            world.setBlockState(pos, replacedState);

            for (int i = 0; i < 20; i++) {
                world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + .5 + world.random.nextGaussian() / 10, pos.getY() + .5 + world.random.nextGaussian() / 10, pos.getZ() + .5 + world.random.nextGaussian() / 10, 0, world.random.nextFloat() / 10, 0);
            }
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }
}
