package net.kofllee.hoverhints.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

public record VillagerPoiRequestPayload(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<VillagerPoiRequestPayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "villager_poi_request"));

    public static final StreamCodec<FriendlyByteBuf, VillagerPoiRequestPayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> buf.writeBlockPos(payload.pos()),
                    buf -> new VillagerPoiRequestPayload(buf.readBlockPos())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}