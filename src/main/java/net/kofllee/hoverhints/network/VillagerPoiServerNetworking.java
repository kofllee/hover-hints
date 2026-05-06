package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.poi.PointOfInterestStorage;

public final class VillagerPoiServerNetworking {
    private VillagerPoiServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                VillagerPoiRequestPayload.ID,
                (payload, context) -> context.player().server.execute(() ->
                        handle(context.player(), payload)
                )
        );
    }

    private static void handle(ServerPlayerEntity player, VillagerPoiRequestPayload payload) {
        ServerWorld world = player.getServerWorld();

        BlockPos pos = normalizePoiPos(world, payload.pos());

        boolean occupied = world.getPointOfInterestStorage()
                .getInSquare(
                        poiType -> true,
                        pos,
                        0,
                        PointOfInterestStorage.OccupationStatus.IS_OCCUPIED
                )
                .anyMatch(poi -> poi.getPos().equals(pos));

        ServerPlayNetworking.send(
                player,
                new VillagerPoiResponsePayload(payload.pos(), occupied)
        );
    }
    private static BlockPos normalizePoiPos(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof BedBlock) {
            BedPart part = state.get(BedBlock.PART);
            Direction facing = state.get(BedBlock.FACING);

            if (part == BedPart.FOOT) {
                return pos.offset(facing);
            }
        }

        return pos;
    }
}