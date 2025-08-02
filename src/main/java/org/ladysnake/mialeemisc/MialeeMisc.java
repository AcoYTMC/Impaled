package org.ladysnake.mialeemisc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.mialeemisc.entities.IPlayerTargeting;
import org.ladysnake.mialeemisc.networking.TargetPayload;

public class MialeeMisc implements ModInitializer {
    public static final String MOD_ID = "mialeemisc";

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(TargetPayload.ID, TargetPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(TargetPayload.ID, (payload, context) -> {
            int id = payload.entityId();
            ServerPlayerEntity serverPlayer = context.player();
            context.server().execute(() -> {
                if (serverPlayer instanceof IPlayerTargeting targeting) {
                    if (serverPlayer.getWorld().getEntityById(id) instanceof LivingEntity living) {
                        targeting.mialeeMisc$setLastTarget(living);
                    }
                }
            });
        });
    }

    public static Identifier id(String ... path) {
        return namedId(MOD_ID, path);
    }

    public static Identifier namedId(String namespace, String ... path) {
        return Identifier.of(namespace, String.join(".", path));
    }
}
