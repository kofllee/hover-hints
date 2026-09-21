package net.kofllee.hoverhints.client.context;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.network.ContextualValueRequestPayload;
import net.kofllee.hoverhints.network.ContextualValueResponsePayload;
import net.kofllee.hoverhints.network.ContextualValueType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public final class ClientContextualValueState {

    private static final long VALUE_TTL = 20;
    private static final long INVALID_TTL = 200;
    private static final long REQUEST_TIMEOUT = 40;

    private static final Map<Key, CacheEntry> CACHE =
            new HashMap<>();

    private static final Map<Key, PendingEntry> PENDING =
            new HashMap<>();

    private static final Map<Integer, Key> REQUEST_KEYS =
            new HashMap<>();

    private static int nextRequestId = 1;

    private ClientContextualValueState() {}

    public static OptionalInt getOrRequest(
            ContextualValueType type,
            BlockPos pos,
            Identifier providerId,
            BlockState state,
            long gameTime
    ) {
        Key key =
                new Key(
                        type,
                        pos.immutable(),
                        providerId,
                        state
                );

        CacheEntry cached =
                CACHE.get(key);

        if (cached != null) {
            long ttl =
                    cached.valid()
                            ? VALUE_TTL
                            : INVALID_TTL;

            if (gameTime - cached.tick() <= ttl) {
                return cached.valid()
                        ? OptionalInt.of(
                        cached.value()
                )
                        : OptionalInt.empty();
            }

            CACHE.remove(key);
        }

        PendingEntry pending =
                PENDING.get(key);

        if (pending != null) {
            if (gameTime - pending.tick()
                    <= REQUEST_TIMEOUT) {

                return OptionalInt.empty();
            }

            PENDING.remove(key);
            REQUEST_KEYS.remove(
                    pending.requestId()
            );
        }

        if (!ClientPlayNetworking.canSend(
                ContextualValueRequestPayload.ID
        )) {
            return OptionalInt.empty();
        }

        int requestId =
                nextRequestId++;

        PENDING.put(
                key,
                new PendingEntry(
                        requestId,
                        gameTime
                )
        );

        REQUEST_KEYS.put(
                requestId,
                key
        );

        ClientPlayNetworking.send(
                new ContextualValueRequestPayload(
                        requestId,
                        type,
                        pos,
                        providerId
                )
        );

        return OptionalInt.empty();
    }

    public static void accept(
            ContextualValueResponsePayload payload,
            long gameTime
    ) {
        Key key =
                REQUEST_KEYS.remove(
                        payload.requestId()
                );

        if (key == null) {
            return;
        }

        PendingEntry pending =
                PENDING.get(key);

        if (pending != null
                && pending.requestId()
                == payload.requestId()) {

            PENDING.remove(key);
        }

        CACHE.put(
                key,
                new CacheEntry(
                        payload.valid(),
                        payload.value(),
                        gameTime
                )
        );

        if (CACHE.size() > 128) {
            CACHE.clear();
        }
    }

    public static void clear() {
        CACHE.clear();
        PENDING.clear();
        REQUEST_KEYS.clear();
        nextRequestId = 1;
    }

    private record Key(
            ContextualValueType type,
            BlockPos pos,
            Identifier providerId,
            BlockState state
    ) {}

    private record CacheEntry(
            boolean valid,
            int value,
            long tick
    ) {}

    private record PendingEntry(
            int requestId,
            long tick
    ) {}
}