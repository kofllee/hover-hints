package net.kofllee.hoverhints.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ContextualValueRequestPayload(
        int requestId,
        ContextualValueType valueType,
        BlockPos pos,
        Identifier providerId
) implements CustomPacketPayload {

    public static final Type<ContextualValueRequestPayload> ID =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            "hover_hints",
                            "contextual_value_request"
                    )
            );

    public static final StreamCodec<FriendlyByteBuf, ContextualValueRequestPayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.requestId());
                        buf.writeEnum(payload.valueType());
                        buf.writeBlockPos(payload.pos());
                        buf.writeIdentifier(payload.providerId());
                    },
                    buf -> new ContextualValueRequestPayload(
                            buf.readVarInt(),
                            buf.readEnum(ContextualValueType.class),
                            buf.readBlockPos(),
                            buf.readIdentifier()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}