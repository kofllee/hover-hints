package net.kofllee.hoverhints.loot;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;

import java.util.List;

public final class LootConditionReader {

    private LootConditionReader(){}

    public static LootChance readChance(List<LootCondition> conditions, int lootingLevel){
        float chance = 1.0f;

        for (LootCondition condition : conditions) {
            LootChance conditionChance = readSingleChance(condition, lootingLevel);

            if(!conditionChance.known()){
                continue;
            }

            chance *= conditionChance.value();
        }

        return LootChance.known(chance);
    }

    private static LootChance readSingleChance(LootCondition condition, int lootingLevel){
        if(condition instanceof RandomChanceLootCondition randomChance){
            Float value = LootNumberProviderReader.readConstantFLoat(randomChance.chance());

            if(value == null){
                return LootChance.unknown();
            }

            return LootChance.known(value);
        }

        if(condition instanceof RandomChanceWithEnchantedBonusLootCondition enchantedChance){
            float chance = lootingLevel > 0 ? enchantedChance.enchantedChance().getValue(lootingLevel) : enchantedChance.unenchantedChance();

            return LootChance.known(chance);
        }

        return LootChance.known(1.0f);
    }
}
