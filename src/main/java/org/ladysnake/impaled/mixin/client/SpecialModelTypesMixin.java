package org.ladysnake.impaled.mixin.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.ladysnake.impaled.client.render.item.*;
import org.ladysnake.impaled.common.Impaled;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(SpecialModelTypes.class)
public class SpecialModelTypesMixin {
    @Shadow
    @Final
    public static Codecs.IdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void addCustomModels(CallbackInfo ci) {
        ID_MAPPER.put(Impaled.id("atlan"), AtlanModelRenderer.Unbaked.CODEC);
        ID_MAPPER.put(Impaled.id("elder_trident"), ElderTridentModelRenderer.Unbaked.CODEC);
        ID_MAPPER.put(Impaled.id("hellfork"), HellforkTridentModelRenderer.Unbaked.CODEC);
        ID_MAPPER.put(Impaled.id("pitchfork"), PitchforkTridentModelRenderer.Unbaked.CODEC);
        ID_MAPPER.put(Impaled.id("soulfork"), SoulforkTridentModelRenderer.Unbaked.CODEC);
    }
}
