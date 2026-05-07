package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.village.poi.PoiManager;

public final class VillagerPoiServerNetworking {
    private VillagerPoiServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                VillagerPoiRequestPayload.ID,
                (payload, context) -> context.server().execute(() ->
                        handle(context.player(), payload)
                )
        );
    }

    private static void handle(ServerPlayer player, VillagerPoiRequestPayload payload) {
        ServerLevel world = player.level();

        BlockPos pos = normalizePoiPos(world, payload.pos());

        boolean occupied = world.getPoiManager()
                .getInSquare(
                        poiType -> true,
                        pos,
                        0,
                        PoiManager.Occupancy.IS_OCCUPIED
                )
                .anyMatch(poi -> poi.getPos().equals(pos));

        ServerPlayNetworking.send(
                player,
                new VillagerPoiResponsePayload(payload.pos(), occupied)
        );
    }
    private static BlockPos normalizePoiPos(ServerLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BedBlock) {
            BedPart part = state.getValue(BedBlock.PART);
            Direction facing = state.getValue(BedBlock.FACING);

            if (part == BedPart.FOOT) {
                return pos.relative(facing);
            }
        }

        return pos;
    }
}