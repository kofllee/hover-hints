package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.archaeology.ArchaeologyLootCalculator;
import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BrushableBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public final class ArchaeologyLootServerNetworking {

    private ArchaeologyLootServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                ArchaeologyLootRequestPayload.ID,
                (payload, context) -> {
                    context.player().server.execute(() -> handle(context.player(), payload));
                }
        );
    }

    private static void handle(ServerPlayerEntity player, ArchaeologyLootRequestPayload payload) {
        BlockPos pos = payload.pos();

        if (player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0) {
            return;
        }

        var state = player.getWorld().getBlockState(pos);

        if (!state.isOf(Blocks.SUSPICIOUS_SAND) && !state.isOf(Blocks.SUSPICIOUS_GRAVEL)) {
            return;
        }

        BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);

        if (!(blockEntity instanceof BrushableBlockEntity brushable)) {
            return;
        }

        List<ArchaeologyLootEntry> entries =
                ArchaeologyLootCalculator.calculate(player.getServerWorld(), brushable);

        ServerPlayNetworking.send(
                player,
                new ArchaeologyLootResponsePayload(pos, entries)
        );
    }
}