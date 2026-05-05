package net.kofllee.hoverhints.client.loot;

import net.kofllee.hoverhints.loot.MobLootEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientMobLootCache {

    private static final Map<Integer, List<MobLootEntry>> CACHE = new HashMap<>();

    private ClientMobLootCache() {}

    public static void put(int entityId, List<MobLootEntry> entries) {
        CACHE.put(entityId, entries);
    }

    public static List<MobLootEntry> get(int entityId) {
        return CACHE.getOrDefault(entityId, List.of());
    }

    public static void clear() {
        CACHE.clear();
    }
}
