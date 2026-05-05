package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public record ArchaeologyLootResponsePayload(
        BlockPos pos,
        List<ArchaeologyLootEntry> entries
) implements CustomPayload {

    public static final CustomPayload.Id<ArchaeologyLootResponsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("hover_hints", "archaeology_loot_response"));

    public static final PacketCodec<PacketByteBuf, ArchaeologyLootResponsePayload> CODEC =
            PacketCodec.of(
                    (payload, buf) -> {
                        buf.writeBlockPos(payload.pos());
                        buf.writeVarInt(payload.entries().size());

                        for (ArchaeologyLootEntry entry : payload.entries()) {
                            buf.writeIdentifier(Registries.ITEM.getId(entry.item()));
                            buf.writeFloat(entry.chancePercent());
                        }
                    },
                    buf -> {
                        BlockPos pos = buf.readBlockPos();
                        int size = buf.readVarInt();

                        List<ArchaeologyLootEntry> entries = new ArrayList<>();

                        for (int i = 0; i < size; i++) {
                            Identifier itemId = buf.readIdentifier();
                            Item item = Registries.ITEM.get(itemId);
                            float chance = buf.readFloat();

                            entries.add(new ArchaeologyLootEntry(item, chance));
                        }

                        return new ArchaeologyLootResponsePayload(pos, entries);
                    }
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}