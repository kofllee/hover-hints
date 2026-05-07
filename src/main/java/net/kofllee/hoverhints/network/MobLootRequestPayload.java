package net.kofllee.hoverhints.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.Identifier;

public record MobLootRequestPayload(
        int entityId,
        Identifier weaponItemId,
        int lootingLevel
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MobLootRequestPayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "mob_loot_request"));

    public static final StreamCodec<FriendlyByteBuf, MobLootRequestPayload> CODEC =
            StreamCodec.ofMember(
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
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}