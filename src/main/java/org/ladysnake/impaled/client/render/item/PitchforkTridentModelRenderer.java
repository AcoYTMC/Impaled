package org.ladysnake.impaled.client.render.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.item.model.special.SimpleSpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import org.joml.Vector3f;
import org.ladysnake.impaled.client.render.entity.model.ImpaledTridentEntityModel;
import org.ladysnake.impaled.common.Impaled;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class PitchforkTridentModelRenderer implements SimpleSpecialModelRenderer {
    private final ImpaledTridentEntityModel model;

    public PitchforkTridentModelRenderer(ImpaledTridentEntityModel model) {
        this.model = model;
    }

    @Override
    public void render(ItemDisplayContext displayContext, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean glint) {
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, this.model.getLayer(Impaled.id("textures/entity/pitchfork.png")), false, glint);
        this.model.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

    @Override
    public void collectVertices(Set<Vector3f> vertices) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1.0F, -1.0F, 1.0F);
        this.model.getRootPart().collectVertices(matrixStack, vertices);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<PitchforkTridentModelRenderer.Unbaked> CODEC = MapCodec.unit(new PitchforkTridentModelRenderer.Unbaked());

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new PitchforkTridentModelRenderer(new ImpaledTridentEntityModel(entityModels.getModelPart(EntityModelLayers.TRIDENT)));
        }
    }
}
