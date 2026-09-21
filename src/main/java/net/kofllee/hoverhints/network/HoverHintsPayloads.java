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

        PayloadTypeRegistry.serverboundPlay().register(
                MobLootRequestPayload.ID,
                MobLootRequestPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                MobLootResponsePayload.ID,
                MobLootResponsePayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                ArchaeologyLootRequestPayload.ID,
                ArchaeologyLootRequestPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                ArchaeologyLootResponsePayload.ID,
                ArchaeologyLootResponsePayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                VillagerPoiRequestPayload.ID,
                VillagerPoiRequestPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                VillagerPoiResponsePayload.ID,
                VillagerPoiResponsePayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                AnimalAgeRequestPayload.ID,
                AnimalAgeRequestPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                AnimalAgeResponsePayload.ID,
                AnimalAgeResponsePayload.CODEC
        );

        PayloadTypeRegistry.serverboundPlay().register(
                ContextualValueRequestPayload.ID,
                ContextualValueRequestPayload.CODEC
        );

        PayloadTypeRegistry.clientboundPlay().register(
                ContextualValueResponsePayload.ID,
                ContextualValueResponsePayload.CODEC
        );
    }
}