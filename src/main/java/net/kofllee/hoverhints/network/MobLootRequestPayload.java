package net.kofllee.hoverhints.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MobLootRequestPayload(
        int entityId,
        Identifier weaponItemId,
        int lootingLevel
) implements CustomPayload {

    public static final CustomPayload.Id<MobLootRequestPayload> ID =
            new CustomPayload.Id<>(Identifier.of("hover_hints", "mob_loot_request"));

    public static final PacketCodec<PacketByteBuf, MobLootRequestPayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.entityId());
                        buf.writeIdentifier(payload.weaponItemId());
                        buf.writeVarInt(payload.lootingLevel());
                    },
                    buf -> new MobLootRequestPayload(
                            buf.readVarInt(),
                            buf.readIdentifier(),
                            buf.readVarInt()
                    )
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}