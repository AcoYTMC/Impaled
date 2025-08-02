package org.ladysnake.impaled.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ImpaledTridentEntityRenderState extends EntityRenderState {
    public float pitch;
    public float yaw;
    public boolean enchanted;
}
