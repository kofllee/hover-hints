package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class HoverHintsPayloads {
    private static boolean registered = false;

    private HoverHintsPayloads() {}

    public static void register() {
        if (registered) {
            return;
        }

        registered = true;

        PayloadTypeRegistry.playC2S().register(
                MobLootRequestPayload.ID,
                MobLootRequestPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                MobLootResponsePayload.ID,
                MobLootResponsePayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                ArchaeologyLootRequestPayload.ID,
                ArchaeologyLootRequestPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                ArchaeologyLootResponsePayload.ID,
                ArchaeologyLootResponsePayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                VillagerPoiRequestPayload.ID,
                VillagerPoiRequestPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                VillagerPoiResponsePayload.ID,
                VillagerPoiResponsePayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                AnimalAgeRequestPayload.ID,
                AnimalAgeRequestPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                AnimalAgeResponsePayload.ID,
                AnimalAgeResponsePayload.CODEC
        );
    }
}