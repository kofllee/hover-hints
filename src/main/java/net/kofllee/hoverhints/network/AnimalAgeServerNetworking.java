package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.mixin.animal.AnimalAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerPlayer;

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

    private static void handle(ServerPlayer player, AnimalAgeRequestPayload payload) {
        Entity entity = player.level().getEntity(payload.entityId());

        if (!(entity instanceof Animal animal)) {
            return;
        }

        ServerPlayNetworking.send(
                player,
                new AnimalAgeResponsePayload(
                        payload.entityId(),
                        animal.getAge(),
                        ((AnimalAccessor) animal).hoverHints$getInLove()
                )
        );
    }
}