package org.ladysnake.impaled.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.entity.*;

@SuppressWarnings("deprecation")
public interface ImpaledEntityTypes {
    EntityType<PitchforkEntity> PITCHFORK = registerEntityType(
            "pitchfork",
            EntityType.Builder.create(PitchforkEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    EntityType<HellforkEntity> HELLFORK = registerEntityType(
            "hellfork",
            EntityType.Builder.create(HellforkEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    EntityType<SoulforkEntity> SOULFORK = registerEntityType(
            "soulfork",
            EntityType.Builder.create(SoulforkEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    EntityType<ElderTridentEntity> ELDER_TRIDENT = registerEntityType(
            "elder_trident",
            EntityType.Builder.create(ElderTridentEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
    );

    EntityType<GuardianTridentEntity> GUARDIAN_TRIDENT = registerEntityType(
            "guardian_trident",
            EntityType.Builder.create(GuardianTridentEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
    );

    EntityType<ImpaledTridentEntity> ATLAN = registerEntityType(
            "atlan",
            EntityType.Builder.create(ImpaledTridentEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
    );

    private static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.Builder<T> builder) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Impaled.id(name));
        return Registry.register(Registries.ENTITY_TYPE, key.getValue(), builder.build(key));
    }

    static void init() {
        //
    }
}
