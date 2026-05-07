package net.kofllee.hoverhints.client.villager;

import net.minecraft.util.math.BlockPos;

public final class ClientVillagerPoiState {
    private static BlockPos lastPos;
    private static Boolean lastOccupied;

    private ClientVillagerPoiState() {}

    public static void set(BlockPos pos, boolean occupied) {
        lastPos = pos;
        lastOccupied = occupied;
    }

    public static Boolean get(BlockPos pos) {
        if (lastPos == null || !lastPos.equals(pos)) {
            return null;
        }

        return lastOccupied;
    }

    public static void clear() {
        lastPos = null;
        lastOccupied = null;
    }
}