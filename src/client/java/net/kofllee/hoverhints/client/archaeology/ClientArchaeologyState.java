package net.kofllee.hoverhints.client.archaeology;

import net.kofllee.hoverhints.client.archaeology.ArchaeologyLootRequestSender;
import net.kofllee.hoverhints.client.archaeology.ClientArchaeologyLootCache;

public final class ClientArchaeologyState {
    private ClientArchaeologyState() {}

    public static void clear() {
        ArchaeologyLootRequestSender.clear();
        ClientArchaeologyLootCache.clear();
    }
}