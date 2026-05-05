package net.kofllee.hoverhints.client.archaeology;

import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientArchaeologyLootCache {

    private static final Map<BlockPos, List<ArchaeologyLootEntry>> CACHE = new HashMap<>();

    private ClientArchaeologyLootCache() {}

    public static void put(BlockPos pos, List<ArchaeologyLootEntry> entries) {
        CACHE.put(pos.toImmutable(), entries);
    }

    public static List<ArchaeologyLootEntry> get(BlockPos pos) {
        return CACHE.getOrDefault(pos, List.of());
    }

    public static void clear() {
        CACHE.clear();
    }
}