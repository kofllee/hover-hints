package net.kofllee.hoverhints.client.animal;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.network.AnimalAgeRequestPayload;
import net.minecraft.client.MinecraftClient;

public final class AnimalAgeRequestSender {
    private static int lastEntityId = -1;
    private static long lastRequestTick = -20;

    private AnimalAgeRequestSender() {}

    public static void request(int entityId) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.world == null) {
            return;
        }

        if (!ClientPlayNetworking.canSend(AnimalAgeRequestPayload.ID)) {
            return;
        }

        long tick = client.world.getTime();

        if (lastEntityId == entityId && tick - lastRequestTick < 10) {
            return;
        }

        lastEntityId = entityId;
        lastRequestTick = tick;

        ClientPlayNetworking.send(new AnimalAgeRequestPayload(entityId));
    }
}