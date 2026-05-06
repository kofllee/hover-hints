package net.kofllee.hoverhints.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.animal.ClientAnimalAgeState;
import net.kofllee.hoverhints.network.AnimalAgeResponsePayload;

public final class AnimalAgeClientNetworking {
    private AnimalAgeClientNetworking() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                AnimalAgeResponsePayload.ID,
                (payload, context) -> context.client().execute(() ->
                        ClientAnimalAgeState.set(
                                payload.entityId(),
                                payload.breedingAge(),
                                payload.loveTicks()
                        )
                )
        );
    }
}