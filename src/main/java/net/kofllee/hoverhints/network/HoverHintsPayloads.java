package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class HoverHintsPayloads {

    private HoverHintsPayloads() {}

    public static void register() {
        PayloadTypeRegistry.playC2S().register(
                MobLootRequestPayload.ID,
                MobLootRequestPayload.CODEC
        );

        PayloadTypeRegistry.playS2C().register(
                MobLootResponsePayload.ID,
                MobLootResponsePayload.CODEC
        );
    }
}