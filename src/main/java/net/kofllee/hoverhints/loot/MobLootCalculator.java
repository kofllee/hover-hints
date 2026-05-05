package net.kofllee.hoverhints.loot;

import net.kofllee.hoverhints.mixin.loot.*;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MobLootCalculator {

    private MobLootCalculator() {}

    public static List<MobLootEntry> calculate(ServerWorld world, MobLootKey key) {
        LootTable table = world.getServer().getReloadableRegistries().getLootTable(key.entityType().getLootTableId());

        List<MobLootEntry> entries = new ArrayList<>();

        for(LootPool pool : ((LootTableAccessor) table).hoverHints$getPools()){
            readPool(pool, key.lootingLevel(), entries);
        }

        return entries.stream().sorted(Comparator.comparingDouble(MobLootEntry::chancePercent).reversed()).toList();
    }

    private static void readPool(LootPool pool, int lootingLevel, List<MobLootEntry> out) {
        LootPoolAccessor accessor = (LootPoolAccessor) pool;

        List<LootPoolEntry> poolEntries = accessor.hoverHints$getEntries();
        List<LootCondition> poolConditions = accessor.hoverHints$getConditions();

        int totalWeight = getTotalWeight(poolEntries);

        if(totalWeight <= 0){
            return;
        }

        LootChance poolChance = LootConditionReader.readChance(poolConditions, lootingLevel);
        
        for(LootPoolEntry poolEntry : poolEntries){
            if(!(poolEntry instanceof ItemEntry itemEntry)){
                continue;
            }

            readItemEntry(itemEntry, poolChance, totalWeight, lootingLevel, out);
        }
    }

    private static void readItemEntry(ItemEntry itemEntry, LootChance poolChance, int totalWeight, int lootingLevel, List<MobLootEntry> out) {
        int weight = ((LeafEntryAccessor) itemEntry).hoverHints$getWeight();
        if(weight <= 0){
            return;
        }

        float entryChance = (float) weight / (float) totalWeight;
        float finalChance = poolChance.value() * entryChance;

        LootNumberRange countRange = readCountRange(itemEntry);

        List<LootFunction> functions =
                ((LeafEntryAccessor) itemEntry).hoverHints$getFunctions();

        countRange = applyLooting(countRange, functions, lootingLevel);

        ItemStack stack = new ItemStack(((ItemEntryAccessor) itemEntry).hoverHints$getItem().value());

        float chancePercent = Math.round(finalChance * 1000.0f) / 10.0f;

        out.add(new MobLootEntry(stack.getItem(), countRange.min(), countRange.max(), chancePercent));
    }

    private static LootNumberRange applyLooting(
            LootNumberRange baseRange,
            List<LootFunction> functions,
            int lootingLevel
    ) {
        if (lootingLevel <= 0) {
            return baseRange;
        }

        int min = baseRange.min();
        int max = baseRange.max();

        for (LootFunction function : functions) {

            if (function instanceof EnchantedCountIncreaseLootFunction enchantedCount) {
                LootNumberProvider countProvider =
                        ((EnchantedCountIncreaseLootFunctionAccessor) enchantedCount)
                                .hoverHints$getCount();

                LootNumberRange bonusRange = LootNumberProviderReader.readIntRange(countProvider);

                int bonusMin = Math.round(bonusRange.min() * lootingLevel);
                int bonusMax = Math.round(bonusRange.max() * lootingLevel);

                min += bonusMin;
                max += bonusMax;

                int limit =
                        ((EnchantedCountIncreaseLootFunctionAccessor) enchantedCount)
                                .hoverHints$getLimit();

                if (limit > 0) {
                    max = Math.min(max, limit);
                }
            }

            if (function instanceof ApplyBonusLootFunction) {
                continue;
            }
        }

        return new LootNumberRange(min, max);
    }

    private static LootNumberRange readCountRange(ItemEntry itemEntry) {
        for(LootFunction function : ((LeafEntryAccessor) itemEntry).hoverHints$getFunctions()){
            if(function instanceof SetCountLootFunction setCount){
                return LootNumberProviderReader.readIntRange(((SetCountLootFunctionAccessor)setCount).hoverHints$getCountRange());
            }
        }

        return LootNumberRange.one();
    }

    private static int getTotalWeight(List<LootPoolEntry> entries) {
        int total = 0;

        for(LootPoolEntry poolEntry : entries){
            if(poolEntry instanceof LeafEntry) {
                total += ((LeafEntryAccessor) poolEntry).hoverHints$getWeight();
            }
        }

        return total;
    }
}
