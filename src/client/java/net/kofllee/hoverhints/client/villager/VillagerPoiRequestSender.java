package net.kofllee.hoverhints.client.villager;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.network.VillagerPoiRequestPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public final class VillagerPoiRequestSender {
    private static BlockPos lastPos;
    private static long lastRequestTick = -20;

    private VillagerPoiRequestSender() {}

    public static void request(BlockPos pos) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null) {
            return;
        }

        long tick = client.player.tickCount;

        if (!pos.equals(lastPos)) {
            lastPos = pos;
            lastRequestTick = tick;

            ClientPlayNetworking.send(new VillagerPoiRequestPayload(pos));
            return;
        }

        if (tick - lastRequestTick < 5) {
            return;
        }

        lastRequestTick = tick;

        ClientPlayNetworking.send(new VillagerPoiRequestPayload(pos));
    }
}