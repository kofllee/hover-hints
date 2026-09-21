package net.kofllee.hoverhints.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.client.context.ClientContextualValueState;
import net.kofllee.hoverhints.network.ContextualValueResponsePayload;

public final class ContextualValueClientNetworking {

    private ContextualValueClientNetworking() {}

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                ContextualValueResponsePayload.ID,
                (payload, context) ->
                        context.client().execute(() -> {
                            long gameTime =
                                    context.client().level == null
                                            ? 0
                                            : context.client().level.getGameTime();

                            ClientContextualValueState.accept(
                                    payload,
                                    gameTime
                            );
                        })
        );
    }
}