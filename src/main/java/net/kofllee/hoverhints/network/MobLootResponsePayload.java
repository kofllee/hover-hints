package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.loot.MobLootEntry;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record MobLootResponsePayload(
        int entityId,
        List<MobLootEntry> entries
) implements CustomPayload {

    public static final CustomPayload.Id<MobLootResponsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("hover_hints", "mob_loot_response"));

    public static final PacketCodec<PacketByteBuf, MobLootResponsePayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeVarInt(payload.entityId());
                        buf.writeVarInt(payload.entries().size());

                        for (MobLootEntry entry : payload.entries()) {
                            buf.writeIdentifier(Registries.ITEM.getId(entry.item()));
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
                            Item item = Registries.ITEM.get(itemId);

                            int min = buf.readVarInt();
                            int max = buf.readVarInt();
                            float chance = buf.readFloat();

                            entries.add(new MobLootEntry(item, min, max, chance));
                        }

                        return new MobLootResponsePayload(entityId, entries);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}