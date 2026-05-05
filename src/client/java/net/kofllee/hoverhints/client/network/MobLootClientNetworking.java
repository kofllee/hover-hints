package net.kofllee.hoverhints.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.loot.ClientMobLootCache;
import net.kofllee.hoverhints.network.MobLootResponsePayload;

public class MobLootClientNetworking {

    private MobLootClientNetworking() {}

    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(
                MobLootResponsePayload.ID,
                (payload, context) -> {
                    context.client().execute(() ->
                            ClientMobLootCache.put(payload.entityId(), payload.entries())
                    );
                }
        );
    }
}
