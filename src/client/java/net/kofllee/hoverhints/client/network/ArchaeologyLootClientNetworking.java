package net.kofllee.hoverhints.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.archaeology.ClientArchaeologyLootCache;
import net.kofllee.hoverhints.network.ArchaeologyLootResponsePayload;

public final class ArchaeologyLootClientNetworking {

    private ArchaeologyLootClientNetworking() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                ArchaeologyLootResponsePayload.ID,
                (payload, context) -> {
                    context.client().execute(() ->
                            ClientArchaeologyLootCache.put(payload.pos(), payload.entries())
                    );
                }
        );
    }
}