package net.kofllee.hoverhints.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

public record ArchaeologyLootRequestPayload(BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArchaeologyLootRequestPayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "archaeology_loot_request"));

    public static final StreamCodec<FriendlyByteBuf, ArchaeologyLootRequestPayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> buf.writeBlockPos(payload.pos()),
                    buf -> new ArchaeologyLootRequestPayload(buf.readBlockPos())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}