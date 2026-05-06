package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.HoverHints;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AnimalAgeResponsePayload(
        int entityId,
        int breedingAge,
        int loveTicks
) implements CustomPayload {
    public static final Id<AnimalAgeResponsePayload> ID =
            new Id<>(Identifier.of(HoverHints.MOD_ID, "animal_age_response"));

    public static final PacketCodec<PacketByteBuf, AnimalAgeResponsePayload> CODEC =
            PacketCodec.of(
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
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}