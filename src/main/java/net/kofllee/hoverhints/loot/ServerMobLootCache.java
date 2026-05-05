package net.kofllee.hoverhints.loot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ServerMobLootCache {

    private static final Map<MobLootKey, List<MobLootEntry>> CACHE = new HashMap<>();

    private ServerMobLootCache() {}

    public static List<MobLootEntry> getOrCompute(
            MobLootKey key,
            Supplier<List<MobLootEntry>> supplier
    ) {
        return CACHE.computeIfAbsent(key, ignored -> supplier.get());
    }

    public static void clear() {
        CACHE.clear();
    }
}