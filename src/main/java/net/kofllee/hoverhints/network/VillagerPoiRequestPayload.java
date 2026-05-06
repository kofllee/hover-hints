package net.kofllee.hoverhints.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record VillagerPoiRequestPayload(BlockPos pos) implements CustomPayload {
    public static final CustomPayload.Id<VillagerPoiRequestPayload> ID =
            new CustomPayload.Id<>(Identifier.of("hover_hints", "villager_poi_request"));

    public static final PacketCodec<PacketByteBuf, VillagerPoiRequestPayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> buf.writeBlockPos(payload.pos()),
                    buf -> new VillagerPoiRequestPayload(buf.readBlockPos())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}