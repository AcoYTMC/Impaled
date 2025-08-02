package org.ladysnake.mialeemisc.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.ladysnake.mialeemisc.MialeeMisc;

public record TargetPayload(int entityId) implements CustomPayload {
    public static final Id<TargetPayload> ID = new CustomPayload.Id<>(MialeeMisc.id("target"));
    public static final PacketCodec<RegistryByteBuf, TargetPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, TargetPayload::entityId, TargetPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
