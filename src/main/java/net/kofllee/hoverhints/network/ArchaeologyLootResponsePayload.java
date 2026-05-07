package net.kofllee.hoverhints.network;

import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.world.item.Item;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public record ArchaeologyLootResponsePayload(
        BlockPos pos,
        List<ArchaeologyLootEntry> entries
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ArchaeologyLootResponsePayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("hover_hints", "archaeology_loot_response"));

    public static final StreamCodec<FriendlyByteBuf, ArchaeologyLootResponsePayload> CODEC =
            StreamCodec.ofMember(
                    (payload, buf) -> {
                        buf.writeBlockPos(payload.pos());
                        buf.writeVarInt(payload.entries().size());

                        for (ArchaeologyLootEntry entry : payload.entries()) {
                            buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(entry.item()));
                            buf.writeFloat(entry.chancePercent());
                        }
                    },
                    buf -> {
                        BlockPos pos = buf.readBlockPos();
                        int size = buf.readVarInt();

                        List<ArchaeologyLootEntry> entries = new ArrayList<>();

                        for (int i = 0; i < size; i++) {
                            Identifier itemId = buf.readIdentifier();
                            Item item = BuiltInRegistries.ITEM.getValue(itemId);
                            float chance = buf.readFloat();

                            entries.add(new ArchaeologyLootEntry(item, chance));
                        }

                        return new ArchaeologyLootResponsePayload(pos, entries);
                    }
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}