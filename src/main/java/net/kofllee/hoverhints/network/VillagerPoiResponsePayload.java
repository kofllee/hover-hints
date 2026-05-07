package net.kofllee.hoverhints.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

public record VillagerPoiResponsePayload(
        BlockPos pos,
        boolean occupied
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VillagerPoiResponsePayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "villager_poi_response"));

    public static final StreamCodec<FriendlyByteBuf, VillagerPoiResponsePayload> CODEC =
            StreamCodec.ofMember(
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
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}