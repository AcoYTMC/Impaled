package org.ladysnake.impaled.common.util;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerEntityRenderStateAccess {
    void impaled$setPlayerEntity(PlayerEntity player);
    PlayerEntity impaled$getPlayerEntity();
}
