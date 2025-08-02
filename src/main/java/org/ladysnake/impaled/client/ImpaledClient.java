package org.ladysnake.impaled.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.ladysnake.impaled.client.render.entity.ImpaledTridentEntityRenderer;
import org.ladysnake.impaled.client.render.entity.model.ImpaledTridentEntityModel;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.init.ImpaledEntityTypes;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.impaled.common.item.ImpaledTridentItem;

public class ImpaledClient implements ClientModInitializer {
    public static final Identifier HELLFORK_RIPTIDE_TEXTURE = Impaled.id("textures/entity/hellfork_riptide.png");
    public static final Identifier SOULFORK_RIPTIDE_TEXTURE = Impaled.id("textures/entity/soulfork_riptide.png");
    public static final EntityModelLayer ATLAN = new EntityModelLayer(Impaled.id("atlan"), "main");

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(ATLAN, ImpaledTridentEntityModel::getAtlanTexturedModelData);

        for (Item item : ImpaledItems.ALL_TRIDENTS) {
            if (item instanceof ImpaledTridentItem tridentItem) {
                Identifier itemId = Registries.ITEM.getId(item);
                Identifier texture = Identifier.of(itemId.getNamespace(), "textures/entity/" + itemId.getPath() + ".png");
                EntityModelLayer modelLayer = item == ImpaledItems.ATLAN ? ATLAN : EntityModelLayers.TRIDENT;
                EntityRendererRegistry.register(tridentItem.getEntityType(), ctx -> new ImpaledTridentEntityRenderer(ctx, texture, modelLayer));
            }
        }

        EntityRendererRegistry.register(ImpaledEntityTypes.GUARDIAN_TRIDENT, ctx -> new ImpaledTridentEntityRenderer(ctx, Impaled.id("textures/entity/guardian_trident.png"), EntityModelLayers.TRIDENT));
    }
}
