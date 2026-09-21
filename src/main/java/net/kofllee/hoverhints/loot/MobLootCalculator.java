package net.kofllee.hoverhints.loot;

import net.kofllee.hoverhints.mixin.loot.EnchantedCountIncreaseFunctionAccessor;
import net.kofllee.hoverhints.mixin.loot.LootItemAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolEntryContainerAccessor;
import net.kofllee.hoverhints.mixin.loot.LootTableAccessor;
import net.kofllee.hoverhints.mixin.loot.SequenceFunctionAccessor;
import net.kofllee.hoverhints.mixin.loot.SetItemCountFunctionAccessor;
import net.kofllee.hoverhints.mixin.loot.UniformContainerBaseAccessor;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SequenceFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class MobLootCalculator {

    private MobLootCalculator() {}

    public static List<MobLootEntry> calculate(
            ServerLevel world,
            MobLootKey key
    ) {
        LootTable table = world.getServer()
                .reloadableRegistries()
                .getLootTable(
                        key.entityType()
                                .getDefaultLootTable()
                                .get()
                );

        List<MobLootEntry> entries = new ArrayList<>();

        for (LootPool pool :
                ((LootTableAccessor) table).hoverHints$getPools()) {

            readPool(
                    pool,
                    key.lootingLevel(),
                    entries
            );
        }

        return entries.stream()
                .sorted(
                        Comparator.comparingDouble(
                                MobLootEntry::chancePercent
                        ).reversed()
                )
                .toList();
    }

    private static void readPool(
            LootPool pool,
            int lootingLevel,
            List<MobLootEntry> out
    ) {
        LootPoolAccessor accessor =
                (LootPoolAccessor) pool;

        List<LootPoolEntryContainer> poolEntries =
                accessor.hoverHints$getEntries();

        int totalWeight =
                getTotalWeight(poolEntries);

        if (totalWeight <= 0) {
            return;
        }

        LootChance poolChance =
                LootConditionReader.readChance(
                        accessor.hoverHints$getCondition(),
                        lootingLevel
                );

        for (LootPoolEntryContainer poolEntry : poolEntries) {
            if (!(poolEntry instanceof LootItem itemEntry)) {
                continue;
            }

            readItemEntry(
                    itemEntry,
                    poolChance,
                    totalWeight,
                    lootingLevel,
                    out
            );
        }
    }

    private static void readItemEntry(
            LootItem itemEntry,
            LootChance poolChance,
            int totalWeight,
            int lootingLevel,
            List<MobLootEntry> out
    ) {
        int weight =
                ((UniformContainerBaseAccessor) itemEntry)
                        .hoverHints$getWeight();

        if (weight <= 0) {
            return;
        }

        float entryChance =
                (float) weight / (float) totalWeight;

        float finalChance =
                poolChance.value() * entryChance;

        LootNumberRange countRange =
                readCountRange(itemEntry);

        List<LootItemFunction> functions =
                getFunctions(itemEntry);

        countRange =
                applyLooting(
                        countRange,
                        functions,
                        lootingLevel
                );

        ItemStack stack =
                new ItemStack(
                        ((LootItemAccessor) itemEntry)
                                .hoverHints$getItem()
                                .value()
                );

        float chancePercent =
                Math.round(finalChance * 1000.0F) / 10.0F;

        out.add(
                new MobLootEntry(
                        stack.getItem(),
                        countRange.min(),
                        countRange.max(),
                        chancePercent
                )
        );
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
            if (!(function instanceof EnchantedCountIncreaseFunction enchantedCount)) {
                continue;
            }

            Holder<ContextFloatProvider> countProvider =
                    ((EnchantedCountIncreaseFunctionAccessor) enchantedCount)
                            .hoverHints$getCount();

            LootFloatRange bonusRange =
                    LootNumberProviderReader.readFloatRange(
                            countProvider
                    );

            int bonusMin =
                    Math.round(
                            bonusRange.min() * lootingLevel
                    );

            int bonusMax =
                    Math.round(
                            bonusRange.max() * lootingLevel
                    );

            min += bonusMin;
            max += bonusMax;

            int limit =
                    ((EnchantedCountIncreaseFunctionAccessor) enchantedCount)
                            .hoverHints$getLimit();

            if (limit > 0) {
                min = Math.min(min, limit);
                max = Math.min(max, limit);
            }
        }

        return new LootNumberRange(min, max);
    }

    private static LootNumberRange readCountRange(
            LootItem itemEntry
    ) {
        for (LootItemFunction function : getFunctions(itemEntry)) {
            if (!(function instanceof SetItemCountFunction setCount)) {
                continue;
            }

            return LootNumberProviderReader.readIntRange(
                    ((SetItemCountFunctionAccessor) setCount)
                            .hoverHints$getCount()
            );
        }

        return LootNumberRange.one();
    }

    private static List<LootItemFunction> getFunctions(
            LootPoolEntryContainer entry
    ) {
        Optional<Holder<LootItemFunction>> modifier =
                ((LootPoolEntryContainerAccessor) entry)
                        .hoverHints$getModifier();

        if (modifier.isEmpty()) {
            return List.of();
        }

        return flattenFunction(
                modifier.get().value()
        );
    }

    private static List<LootItemFunction> flattenFunction(
            LootItemFunction function
    ) {
        if (!(function instanceof SequenceFunction sequence)) {
            return List.of(function);
        }

        List<LootItemFunction> result =
                new ArrayList<>();

        for (Holder<LootItemFunction> holder :
                ((SequenceFunctionAccessor) sequence)
                        .hoverHints$getFunctions()) {

            result.add(holder.value());
        }

        return result;
    }

    private static int getTotalWeight(
            List<LootPoolEntryContainer> entries
    ) {
        int total = 0;

        for (LootPoolEntryContainer entry : entries) {
            if (!(entry instanceof UniformContainerBase uniformEntry)) {
                continue;
            }

            total +=
                    ((UniformContainerBaseAccessor) uniformEntry)
                            .hoverHints$getWeight();
        }

        return total;
    }
}