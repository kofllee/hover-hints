package net.kofllee.hoverhints.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;

import java.util.Optional;

public final class LootConditionReader {

    private LootConditionReader() {}

    public static LootChance readChance(
            Optional<Holder<LootItemCondition>> condition,
            int lootingLevel
    ) {
        if (condition.isEmpty()) {
            return LootChance.known(1.0F);
        }

        return readSingleChance(
                condition.get().value(),
                lootingLevel
        );
    }

    private static LootChance readSingleChance(
            LootItemCondition condition,
            int lootingLevel
    ) {
        if (condition instanceof LootItemRandomChanceCondition randomChance) {
            LootFloatRange range =
                    LootNumberProviderReader.readFloatRange(
                            randomChance.chance()
                    );

            if (range.min() != range.max()) {
                return LootChance.unknown();
            }

            return LootChance.known(range.min());
        }

        if (condition instanceof LootItemRandomChanceWithEnchantedBonusCondition enchantedChance) {
            float chance =
                    lootingLevel > 0
                            ? enchantedChance.enchantedChance().calculate(lootingLevel)
                            : enchantedChance.unenchantedChance();

            return LootChance.known(chance);
        }

        return LootChance.known(1.0F);
    }
}