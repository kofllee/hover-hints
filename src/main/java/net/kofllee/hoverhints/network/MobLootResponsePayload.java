package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.loot.MobLootEntry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record MobLootResponsePayload(
        int entityId,
        List<MobLootEntry> entries
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<MobLootResponsePayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "mob_loot_response"));

    public static final StreamCodec<FriendlyByteBuf, MobLootResponsePayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.entityId());
                        buf.writeVarInt(payload.entries().size());

                        for (MobLootEntry entry : payload.entries()) {
                            buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(entry.item()));
                            buf.writeVarInt(entry.minCount());
                            buf.writeVarInt(entry.maxCount());
                            buf.writeFloat(entry.chancePercent());
                        }
                    },
                    buf -> {
                        int entityId = buf.readVarInt();
                        int size = buf.readVarInt();

                        List<MobLootEntry> entries = new ArrayList<>();

                        for (int i = 0; i < size; i++) {
                            Identifier itemId = buf.readIdentifier();
                            Item item = BuiltInRegistries.ITEM.getValue(itemId);

                            int min = buf.readVarInt();
                            int max = buf.readVarInt();
                            float chance = buf.readFloat();

                            entries.add(new MobLootEntry(item, min, max, chance));
                        }

                        return new MobLootResponsePayload(entityId, entries);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}