package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.context.CompostChanceServerResolver;
import net.kofllee.hoverhints.context.CookingFuelServerResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.OptionalInt;

public final class ContextualValueServerNetworking {

    private ContextualValueServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                ContextualValueRequestPayload.ID,
                (payload, context) ->
                        context.server().execute(
                                () -> handle(
                                        context.player(),
                                        payload
                                )
                        )
        );
    }

    private static void handle(
            ServerPlayer player,
            ContextualValueRequestPayload payload
    ) {
        if (player.distanceToSqr(
                payload.pos().getX() + 0.5,
                payload.pos().getY() + 0.5,
                payload.pos().getZ() + 0.5
        ) > 64.0) {
            sendInvalid(player, payload);
            return;
        }

        ItemStack stack =
                player.getMainHandItem();

        if (!matchesProvider(
                stack,
                payload
        )) {
            sendInvalid(player, payload);
            return;
        }

        ServerLevel level =
                player.level();

        OptionalInt value =
                switch (payload.valueType()) {
                    case FUEL_BURN_TIME ->
                            resolveFuel(
                                    level,
                                    payload,
                                    stack
                            );

                    case COMPOST_CHANCE ->
                            resolveCompost(
                                    level,
                                    player,
                                    payload,
                                    stack
                            );
                };

        if (value.isEmpty()) {
            sendInvalid(player, payload);
            return;
        }

        ServerPlayNetworking.send(
                player,
                new ContextualValueResponsePayload(
                        payload.requestId(),
                        payload.valueType(),
                        true,
                        value.getAsInt()
                )
        );
    }

    private static OptionalInt resolveFuel(
            ServerLevel level,
            ContextualValueRequestPayload payload,
            ItemStack stack
    ) {
        BlockEntity blockEntity =
                level.getBlockEntity(
                        payload.pos()
                );

        if (!(blockEntity instanceof AbstractFurnaceBlockEntity furnace)) {
            return OptionalInt.empty();
        }

        return CookingFuelServerResolver.resolve(
                level,
                furnace,
                stack
        );
    }

    private static OptionalInt resolveCompost(
            ServerLevel level,
            ServerPlayer player,
            ContextualValueRequestPayload payload,
            ItemStack stack
    ) {
        var state =
                level.getBlockState(
                        payload.pos()
                );

        if (!(state.getBlock() instanceof ComposterBlock)) {
            return OptionalInt.empty();
        }

        Compostable compostable =
                stack.get(
                        DataComponents.COMPOSTABLE
                );

        if (compostable == null) {
            return OptionalInt.empty();
        }

        return CompostChanceServerResolver.resolve(
                level,
                payload.pos(),
                state,
                player,
                compostable
        );
    }

    private static boolean matchesProvider(
            ItemStack stack,
            ContextualValueRequestPayload payload
    ) {
        ResolvableInt value =
                switch (payload.valueType()) {
                    case FUEL_BURN_TIME -> {
                        CookingFuel fuel =
                                stack.get(
                                        DataComponents.COOKING_FUEL
                                );

                        yield fuel == null
                                ? null
                                : fuel.burnTime();
                    }

                    case COMPOST_CHANCE -> {
                        Compostable compostable =
                                stack.get(
                                        DataComponents.COMPOSTABLE
                                );

                        yield compostable == null
                                ? null
                                : compostable.layers();
                    }
                };

        if (!(value instanceof ResolvableInt.Reference reference)) {
            return false;
        }

        return reference.key()
                .identifier()
                .equals(
                        payload.providerId()
                );
    }

    private static void sendInvalid(
            ServerPlayer player,
            ContextualValueRequestPayload payload
    ) {
        ServerPlayNetworking.send(
                player,
                new ContextualValueResponsePayload(
                        payload.requestId(),
                        payload.valueType(),
                        false,
                        0
                )
        );
    }
}