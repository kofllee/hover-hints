package net.kofllee.hoverhints.loot;

import net.kofllee.hoverhints.mixin.loot.*;
import net.kofllee.hoverhints.mixin.loot.EnchantedCountIncreaseFunctionAccessor;
import net.kofllee.hoverhints.mixin.loot.LootItemAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolSingletonContainerAccessor;
import net.kofllee.hoverhints.mixin.loot.LootTableAccessor;
import net.kofllee.hoverhints.mixin.loot.SetItemCountFunctionAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MobLootCalculator {

    private MobLootCalculator() {}

    public static List<MobLootEntry> calculate(ServerLevel world, MobLootKey key) {
        LootTable table = world.getServer().reloadableRegistries().getLootTable(key.entityType().getDefaultLootTable().get());

        List<MobLootEntry> entries = new ArrayList<>();

        for(LootPool pool : ((LootTableAccessor) table).hoverHints$getPools()){
            readPool(pool, key.lootingLevel(), entries);
        }

        return entries.stream().sorted(Comparator.comparingDouble(MobLootEntry::chancePercent).reversed()).toList();
    }

    private static void readPool(LootPool pool, int lootingLevel, List<MobLootEntry> out) {
        LootPoolAccessor accessor = (LootPoolAccessor) pool;

        List<LootPoolEntryContainer> poolEntries = accessor.hoverHints$getEntries();
        List<LootItemCondition> poolConditions = accessor.hoverHints$getConditions();

        int totalWeight = getTotalWeight(poolEntries);

        if(totalWeight <= 0){
            return;
        }

        LootChance poolChance = LootConditionReader.readChance(poolConditions, lootingLevel);
        
        for(LootPoolEntryContainer poolEntry : poolEntries){
            if(!(poolEntry instanceof LootItem itemEntry)){
                continue;
            }

            readItemEntry(itemEntry, poolChance, totalWeight, lootingLevel, out);
        }
    }

    private static void readItemEntry(LootItem itemEntry, LootChance poolChance, int totalWeight, int lootingLevel, List<MobLootEntry> out) {
        int weight = ((LootPoolSingletonContainerAccessor) itemEntry).hoverHints$getWeight();
        if(weight <= 0){
            return;
        }

        float entryChance = (float) weight / (float) totalWeight;
        float finalChance = poolChance.value() * entryChance;

        LootNumberRange countRange = readCountRange(itemEntry);

        List<LootItemFunction> functions =
                ((LootPoolSingletonContainerAccessor) itemEntry).hoverHints$getFunctions();

        countRange = applyLooting(countRange, functions, lootingLevel);

        ItemStack stack = new ItemStack(((LootItemAccessor) itemEntry).hoverHints$getItem().value());

        float chancePercent = Math.round(finalChance * 1000.0f) / 10.0f;

        out.add(new MobLootEntry(stack.getItem(), countRange.min(), countRange.max(), chancePercent));
    }

    private static LootNumberRange applyLooting(
            LootNumberRange baseRange,
            List<LootItemFunction> functions,
            int lootingLevel
    ) {
        if (lootingLevel <= 0) {
            return baseRange;
        }

        int min = baseRange.min();
        int max = baseRange.max();

        for (LootItemFunction function : functions) {

            if (function instanceof EnchantedCountIncreaseFunction enchantedCount) {
                NumberProvider countProvider =
                        ((EnchantedCountIncreaseFunctionAccessor) enchantedCount)
                                .hoverHints$getValue();

                LootNumberRange bonusRange = LootNumberProviderReader.readIntRange(countProvider);

                int bonusMin = bonusRange.min() * lootingLevel;
                int bonusMax = bonusRange.max() * lootingLevel;

                min += bonusMin;
                max += bonusMax;

                int limit =
                        ((EnchantedCountIncreaseFunctionAccessor) enchantedCount)
                                .hoverHints$getLimit();

                if (limit > 0) {
                    max = Math.min(max, limit);
                }
            }
        }

        return new LootNumberRange(min, max);
    }

    private static LootNumberRange readCountRange(LootItem itemEntry) {
        for(LootItemFunction function : ((LootPoolSingletonContainerAccessor) itemEntry).hoverHints$getFunctions()){
            if(function instanceof SetItemCountFunction setCount){
                return LootNumberProviderReader.readIntRange(((SetItemCountFunctionAccessor)setCount).hoverHints$getValue());
            }
        }

        return LootNumberRange.one();
    }

    private static int getTotalWeight(List<LootPoolEntryContainer> entries) {
        int total = 0;

        for(LootPoolEntryContainer poolEntry : entries){
            if(poolEntry instanceof LootPoolSingletonContainer) {
                total += ((LootPoolSingletonContainerAccessor) poolEntry).hoverHints$getWeight();
            }
        }

        return total;
    }
}
