package net.kofllee.hoverhints.archaeology;

import net.kofllee.hoverhints.mixin.archaeology.BrushableBlockEntityAccessor;
import net.kofllee.hoverhints.mixin.loot.LootItemAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolSingletonContainerAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolAccessor;
import net.kofllee.hoverhints.mixin.loot.LootTableAccessor;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ArchaeologyLootCalculator {

    private ArchaeologyLootCalculator() {}

    public static List<ArchaeologyLootEntry> calculate(ServerLevel world, BrushableBlockEntity blockEntity) {
        var lootTableKey = ((BrushableBlockEntityAccessor) blockEntity).hoverHints$getLootTable();

        if (lootTableKey == null) {
            return List.of();
        }

        LootTable table = world.getServer()
                .reloadableRegistries()
                .getLootTable(lootTableKey);

        List<ArchaeologyLootEntry> entries = new ArrayList<>();

        for (LootPool pool : ((LootTableAccessor) table).hoverHints$getPools()) {
            readPool(pool, entries);
        }

        return entries.stream()
                .sorted(Comparator.comparingDouble(ArchaeologyLootEntry::chancePercent).reversed())
                .toList();
    }

    private static void readPool(LootPool pool, List<ArchaeologyLootEntry> out) {
        List<LootPoolEntryContainer> poolEntries =
                ((LootPoolAccessor) pool).hoverHints$getEntries();

        int totalWeight = getTotalWeight(poolEntries);

        if (totalWeight <= 0) {
            return;
        }

        for (LootPoolEntryContainer poolEntry : poolEntries) {
            if (!(poolEntry instanceof LootItem itemEntry)) {
                continue;
            }

            int weight = ((LootPoolSingletonContainerAccessor) itemEntry).hoverHints$getWeight();

            if (weight <= 0) {
                continue;
            }

            Item item = ((LootItemAccessor) itemEntry).hoverHints$getItem().value();

            float chancePercent = ((float) weight / (float) totalWeight) * 100.0f;
            chancePercent = Math.round(chancePercent * 10.0f) / 10.0f;

            out.add(new ArchaeologyLootEntry(item, chancePercent));
        }
    }

    private static int getTotalWeight(List<LootPoolEntryContainer> entries) {
        int total = 0;

        for (LootPoolEntryContainer entry : entries) {
            if (entry instanceof LootPoolSingletonContainer) {
                total += ((LootPoolSingletonContainerAccessor) entry).hoverHints$getWeight();
            }
        }

        return total;
    }
}