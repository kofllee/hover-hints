package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.HoverHints;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record AnimalAgeRequestPayload(int entityId) implements CustomPacketPayload {
    public static final Type<AnimalAgeRequestPayload> ID =
            new Type<>(Identifier.fromNamespaceAndPath(HoverHints.MOD_ID, "animal_age_request"));

    public static final StreamCodec<FriendlyByteBuf, AnimalAgeRequestPayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> buf.writeInt(payload.entityId()),
                    buf -> new AnimalAgeRequestPayload(buf.readInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}