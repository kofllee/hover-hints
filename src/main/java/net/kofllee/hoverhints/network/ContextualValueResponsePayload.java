package net.kofllee.hoverhints.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ContextualValueResponsePayload(
        int requestId,
        ContextualValueType valueType,
        boolean valid,
        int value
) implements CustomPacketPayload {

    public static final Type<ContextualValueResponsePayload> ID =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            "hover_hints",
                            "contextual_value_response"
                    )
            );

    public static final StreamCodec<FriendlyByteBuf, ContextualValueResponsePayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.requestId());
                        buf.writeEnum(payload.valueType());
                        buf.writeBoolean(payload.valid());
                        buf.writeInt(payload.value());
                    },
                    buf -> new ContextualValueResponsePayload(
                            buf.readVarInt(),
                            buf.readEnum(ContextualValueType.class),
                            buf.readBoolean(),
                            buf.readInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}