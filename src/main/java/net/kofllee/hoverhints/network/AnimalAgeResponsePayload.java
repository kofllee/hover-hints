package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.HoverHints;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record AnimalAgeResponsePayload(
        int entityId,
        int breedingAge,
        int loveTicks
) implements CustomPacketPayload {
    public static final Type<AnimalAgeResponsePayload> ID =
            new Type<>(Identifier.fromNamespaceAndPath(HoverHints.MOD_ID, "animal_age_response"));

    public static final StreamCodec<FriendlyByteBuf, AnimalAgeResponsePayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> {
                        buf.writeInt(payload.entityId());
                        buf.writeInt(payload.breedingAge());
                        buf.writeInt(payload.loveTicks());
                    },
                    buf -> new AnimalAgeResponsePayload(
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt()
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}