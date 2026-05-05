package net.kofllee.hoverhints.archaeology;

import net.kofllee.hoverhints.mixin.archaeology.BrushableBlockEntityAccessor;
import net.kofllee.hoverhints.mixin.loot.ItemEntryAccessor;
import net.kofllee.hoverhints.mixin.loot.LeafEntryAccessor;
import net.kofllee.hoverhints.mixin.loot.LootPoolAccessor;
import net.kofllee.hoverhints.mixin.loot.LootTableAccessor;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ArchaeologyLootCalculator {

    private ArchaeologyLootCalculator() {}

    public static List<ArchaeologyLootEntry> calculate(ServerWorld world, BrushableBlockEntity blockEntity) {
        var lootTableKey = ((BrushableBlockEntityAccessor) blockEntity).hoverHints$getLootTable();

        if (lootTableKey == null) {
            return List.of();
        }

        LootTable table = world.getServer()
                .getReloadableRegistries()
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
        List<LootPoolEntry> poolEntries =
                ((LootPoolAccessor) pool).hoverHints$getEntries();

        int totalWeight = getTotalWeight(poolEntries);

        if (totalWeight <= 0) {
            return;
        }

        for (LootPoolEntry poolEntry : poolEntries) {
            if (!(poolEntry instanceof ItemEntry itemEntry)) {
                continue;
            }

            int weight = ((LeafEntryAccessor) itemEntry).hoverHints$getWeight();

            if (weight <= 0) {
                continue;
            }

            Item item = ((ItemEntryAccessor) itemEntry).hoverHints$getItem().value();

            float chancePercent = ((float) weight / (float) totalWeight) * 100.0f;
            chancePercent = Math.round(chancePercent * 10.0f) / 10.0f;

            out.add(new ArchaeologyLootEntry(item, chancePercent));
        }
    }

    private static int getTotalWeight(List<LootPoolEntry> entries) {
        int total = 0;

        for (LootPoolEntry entry : entries) {
            if (entry instanceof LeafEntry) {
                total += ((LeafEntryAccessor) entry).hoverHints$getWeight();
            }
        }

        return total;
    }
}