package net.kofllee.hoverhints.loot;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;

import java.util.List;

public final class LootConditionReader {

    private LootConditionReader(){}

    public static LootChance readChance(List<LootItemCondition> conditions, int lootingLevel){
        float chance = 1.0f;

        for (LootItemCondition condition : conditions) {
            LootChance conditionChance = readSingleChance(condition, lootingLevel);

            if(!conditionChance.known()){
                continue;
            }

            chance *= conditionChance.value();
        }

        return LootChance.known(chance);
    }

    private static LootChance readSingleChance(LootItemCondition condition, int lootingLevel){
        if(condition instanceof LootItemRandomChanceCondition randomChance){
            Float value = LootNumberProviderReader.readConstantFLoat(randomChance.chance());

            if(value == null){
                return LootChance.unknown();
            }

            return LootChance.known(value);
        }

        if(condition instanceof LootItemRandomChanceWithEnchantedBonusCondition enchantedChance){
            float chance = lootingLevel > 0 ? enchantedChance.enchantedChance().calculate(lootingLevel) : enchantedChance.unenchantedChance();

            return LootChance.known(chance);
        }

        return LootChance.known(1.0f);
    }
}
