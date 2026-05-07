    package net.kofllee.hoverhints.client.archaeology;

    import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
    import net.kofllee.hoverhints.network.ArchaeologyLootRequestPayload;
    import net.minecraft.util.math.BlockPos;

    import java.util.HashSet;
    import java.util.Set;

    public final class ArchaeologyLootRequestSender {

        private static final Set<BlockPos> REQUESTED = new HashSet<>();

        private ArchaeologyLootRequestSender() {}

        public static void request(BlockPos pos) {
            BlockPos key = pos.toImmutable();

            if (!REQUESTED.add(key)) {
                return;
            }

            ClientPlayNetworking.send(new ArchaeologyLootRequestPayload(key));
        }

        public static void clear() {
            REQUESTED.clear();
        }
    }