package org.ladysnake.impaled.mixin.access;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.impaled.common.util.PlayerEntityRenderStateAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/// Adds the player as a field in {@link PlayerEntityRenderState}
@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin implements PlayerEntityRenderStateAccess {
    @Unique private PlayerEntity player;

    @Override
    public void impaled$setPlayerEntity(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public PlayerEntity impaled$getPlayerEntity() {
        return this.player;
    }
}
