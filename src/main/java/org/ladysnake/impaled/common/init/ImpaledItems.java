package org.ladysnake.impaled.common.init;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.item.*;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public interface ImpaledItems {
    Set<Item> ALL_TRIDENTS = new ReferenceOpenHashSet<>();

    Item ELDER_GUARDIAN_EYE = create("elder_guardian_eye", Item::new, new Item.Settings()
            .rarity(Rarity.UNCOMMON));

    Item ANCIENT_TRIDENT = create("ancient_trident", Item::new, new Item.Settings()
            .rarity(Rarity.UNCOMMON)
            .fireproof());

    // Tridents
    Item PITCHFORK = create("pitchfork", settings -> new PitchforkItem(settings, ImpaledEntityTypes.PITCHFORK), new Item.Settings()
            .enchantable(1)
            .maxDamage(150));

    Item HELLFORK = create("hellfork", settings -> new HellforkItem(settings, ImpaledEntityTypes.HELLFORK), new Item.Settings()
            .enchantable(1)
            .maxDamage(325)
            .fireproof());

    Item SOULFORK = create("soulfork", settings -> new HellforkItem(settings, ImpaledEntityTypes.SOULFORK), new Item.Settings()
            .enchantable(1)
            .maxDamage(325)
            .fireproof());

    Item ELDER_TRIDENT = create("elder_trident", settings -> new ElderTridentItem(settings, ImpaledEntityTypes.ELDER_TRIDENT), new Item.Settings()
            .enchantable(1)
            .maxDamage(250));

    Item ATLAN = create("atlan", settings -> new AtlanItem(settings, ImpaledEntityTypes.ATLAN), new Item.Settings()
            .enchantable(1)
            .maxDamage(250));

    // Other
    Item MAELSTROM = create("maelstrom", MaelstromItem::new, new Item.Settings()
            .maxDamage(80));

    Item TRIDENT_UPGRADE_SMITHING_TEMPLATE = create("trident_upgrade_smithing_template", settings -> new SmithingTemplateItem(
            translation("applies_to").copy().formatted(Formatting.BLUE), // Applies to
            translation("ingredients").copy().formatted(Formatting.BLUE), // Ingredients
            translation("base_slot_description"), // Base Description
            translation("additions_slot_description"), // Additions Description
            List.of(Impaled.id("container/slot/trident")),
            List.of(Impaled.id("container/slot/ancient_trident"), Impaled.id("container/slot/apple"), Impaled.id("container/slot/elder_guardian_eye")),
            settings
    ), new Item.Settings().rarity(Rarity.UNCOMMON));

    static Item create(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Items.register(RegistryKey.of(RegistryKeys.ITEM, Impaled.id(name)), factory, settings);
    }

    private static Text translation(String type) {
        return Text.translatable(Util.createTranslationKey("item", Impaled.id("smithing_template.trident_upgrade." + type)));
    }

    static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ImpaledItems::addCombatEntries);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ImpaledItems::addIngredientEntries);

        ALL_TRIDENTS.add(PITCHFORK);
        ALL_TRIDENTS.add(HELLFORK);
        ALL_TRIDENTS.add(SOULFORK);
        ALL_TRIDENTS.add(ELDER_TRIDENT);
        ALL_TRIDENTS.add(ATLAN);

        for (Item item : ALL_TRIDENTS) {
            DefaultItemComponentEvents.MODIFY.register(ctx -> ctx.modify(
                    Predicate.isEqual(item),
                    (builder, item1) -> {
                        builder.add(DataComponentTypes.ATTRIBUTE_MODIFIERS, TridentItem.createAttributeModifiers());
                        builder.add(DataComponentTypes.TOOL, TridentItem.createToolComponent());
                    }
            ));
        }
    }

    private static void addCombatEntries(FabricItemGroupEntries entries) {
        entries.add(ImpaledItems.PITCHFORK);
        entries.add(ImpaledItems.HELLFORK);
        entries.add(ImpaledItems.SOULFORK);
        entries.add(ImpaledItems.ELDER_TRIDENT);
        entries.add(ImpaledItems.ATLAN);
        entries.add(ImpaledItems.MAELSTROM);
    }

    private static void addIngredientEntries(FabricItemGroupEntries entries) {
        entries.add(ELDER_GUARDIAN_EYE);
        entries.add(ANCIENT_TRIDENT);
        entries.addAfter(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, TRIDENT_UPGRADE_SMITHING_TEMPLATE);
    }
}
