package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.archaeology.ArchaeologyLootCalculator;
import net.kofllee.hoverhints.archaeology.ArchaeologyLootEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;

import java.util.List;

public final class ArchaeologyLootServerNetworking {

    private ArchaeologyLootServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                ArchaeologyLootRequestPayload.ID,
                (payload, context) -> context.server().execute(() -> handle(context.player(), payload))
        );
    }

    private static void handle(ServerPlayer player, ArchaeologyLootRequestPayload payload) {
        BlockPos pos = payload.pos();

        if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0) {
            return;
        }

        var state = player.level().getBlockState(pos);

        if (!state.is(Blocks.SUSPICIOUS_SAND) && !state.is(Blocks.SUSPICIOUS_GRAVEL)) {
            return;
        }

        BlockEntity blockEntity = player.level().getBlockEntity(pos);

        if (!(blockEntity instanceof BrushableBlockEntity brushable)) {
            return;
        }

        List<ArchaeologyLootEntry> entries =
                ArchaeologyLootCalculator.calculate(player.level(), brushable);

        ServerPlayNetworking.send(
                player,
                new ArchaeologyLootResponsePayload(pos, entries)
        );
    }
}