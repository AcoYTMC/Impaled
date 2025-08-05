package org.ladysnake.impaled.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;
import org.ladysnake.impaled.common.init.ImpaledEntityTypes;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.impaled.common.item.HellforkItem;
import org.ladysnake.impaled.common.util.TargetingUtil;

public class Impaled implements ModInitializer {
	public static final String MOD_ID = "impaled";

	public static boolean isHoldingSoulfork(LivingEntity living) {
		return living.getMainHandStack().getItem() == ImpaledItems.SOULFORK || living.getOffHandStack().getItem() == ImpaledItems.SOULFORK;
	}

	public static boolean isHoldingFlamefork(LivingEntity living) {
		return living.getMainHandStack().getItem() instanceof HellforkItem || living.getOffHandStack().getItem() instanceof HellforkItem;
	}

	@Override
	public void onInitialize() {
		ImpaledEntityTypes.init();
		ImpaledItems.init();
        TargetingUtil.init();

		UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(1, 1);
		LootCondition.Builder chanceLootCondition = RandomChanceLootCondition.builder(60);
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registry) -> {
			if (LootTables.BASTION_TREASURE_CHEST.equals(key)) {
				LootPool.Builder lootPool = LootPool.builder()
						.rolls(lootTableRange)
						.conditionally(chanceLootCondition)
						.with(ItemEntry.builder(ImpaledItems.ANCIENT_TRIDENT));

				tableBuilder.pool(lootPool);
			}
		});
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
