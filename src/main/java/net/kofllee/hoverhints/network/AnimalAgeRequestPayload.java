package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.HoverHints;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record AnimalAgeRequestPayload(int entityId) implements CustomPayload {
    public static final Id<AnimalAgeRequestPayload> ID =
            new Id<>(Identifier.of(HoverHints.MOD_ID, "animal_age_request"));

    public static final PacketCodec<PacketByteBuf, AnimalAgeRequestPayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> buf.writeInt(payload.entityId()),
                    buf -> new AnimalAgeRequestPayload(buf.readInt())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}