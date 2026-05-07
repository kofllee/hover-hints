package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.mixin.animal.AnimalEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public final class AnimalAgeServerNetworking {
    private AnimalAgeServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                AnimalAgeRequestPayload.ID,
                (payload, context) -> context.server().execute(() ->
                        handle(context.player(), payload)
                )
        );
    }

    private static void handle(ServerPlayerEntity player, AnimalAgeRequestPayload payload) {
        Entity entity = player.getEntityWorld().getEntityById(payload.entityId());

        if (!(entity instanceof AnimalEntity animal)) {
            return;
        }

        ServerPlayNetworking.send(
                player,
                new AnimalAgeResponsePayload(
                        payload.entityId(),
                        animal.getBreedingAge(),
                        ((AnimalEntityAccessor) animal).hoverHints$getLoveTicks()
                )
        );
    }
}