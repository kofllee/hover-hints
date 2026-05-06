package net.kofllee.hoverhints.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.villager.ClientVillagerPoiState;
import net.kofllee.hoverhints.network.VillagerPoiResponsePayload;

public final class VillagerPoiClientNetworking {
    private VillagerPoiClientNetworking() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                VillagerPoiResponsePayload.ID,
                (payload, context) -> context.client().execute(() ->
                        ClientVillagerPoiState.set(payload.pos(), payload.occupied())
                )
        );
    }
}