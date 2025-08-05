package org.ladysnake.impaled.common.util;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface TargetingUtil {
    record TargetPayload(int entityId) implements CustomPayload {
        public static final PacketCodec<PacketByteBuf, TargetPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, TargetPayload::entityId, TargetPayload::new);
        public static final Id<TargetPayload> ID = new Id<>(Identifier.of("mialee_misc", "target"));

        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    static void init() {
        PayloadTypeRegistry.playC2S().register(TargetPayload.ID, TargetPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TargetPayload.ID, (payload, context) -> {
            ServerPlayerEntity serverPlayer = context.player();
            context.server().execute(() -> {
                if (serverPlayer instanceof IPlayerTargeting targeting) {
                    if (serverPlayer.getWorld().getEntityById(payload.entityId()) instanceof LivingEntity living) {
                        targeting.mialeeMisc$setLastTarget(living);
                    }
                }
            });
        });
    }
}
