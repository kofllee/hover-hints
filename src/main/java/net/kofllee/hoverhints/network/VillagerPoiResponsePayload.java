package net.kofllee.hoverhints.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record VillagerPoiResponsePayload(
        BlockPos pos,
        boolean occupied
) implements CustomPayload {
    public static final CustomPayload.Id<VillagerPoiResponsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("hover_hints", "villager_poi_response"));

    public static final PacketCodec<PacketByteBuf, VillagerPoiResponsePayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeBlockPos(payload.pos());
                        buf.writeBoolean(payload.occupied());
                    },
                    buf -> new VillagerPoiResponsePayload(
                            buf.readBlockPos(),
                            buf.readBoolean()
                    )
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}