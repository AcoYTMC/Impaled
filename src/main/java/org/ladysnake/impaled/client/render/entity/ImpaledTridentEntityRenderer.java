package org.ladysnake.impaled.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.ladysnake.impaled.client.render.entity.model.ImpaledTridentEntityModel;
import org.ladysnake.impaled.common.entity.ImpaledTridentEntity;

@Environment(EnvType.CLIENT)
public class ImpaledTridentEntityRenderer extends EntityRenderer<ImpaledTridentEntity, ImpaledTridentEntityRenderState> {
    private final ImpaledTridentEntityModel model;
    private final Identifier texture;

    public ImpaledTridentEntityRenderer(EntityRendererFactory.Context context, Identifier texture, EntityModelLayer modelLayer) {
        super(context);
        this.model = new ImpaledTridentEntityModel(context.getPart(modelLayer));
        this.texture = texture;
    }

    public void render(ImpaledTridentEntityRenderState renderState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(renderState.yaw - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(renderState.pitch + 90.0F));
        VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(
                vertexConsumers, this.model.getLayer(this.getTexture()), false, renderState.enchanted
        );
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
        super.render(renderState, matrices, vertexConsumers, light);
    }

    public ImpaledTridentEntityRenderState createRenderState() {
        return new ImpaledTridentEntityRenderState();
    }

    public Identifier getTexture() {
        return this.texture;
    }

    @Override
    public void updateRenderState(ImpaledTridentEntity trident, ImpaledTridentEntityRenderState state, float tickProgress) {
        super.updateRenderState(trident, state, tickProgress);
        state.yaw = trident.getLerpedYaw(tickProgress);
        state.pitch = trident.getLerpedPitch(tickProgress);
        state.enchanted = trident.isEnchanted();
    }
}
