package net.kofllee.hoverhints.context;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.DispatcherProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public final class CompostChanceServerResolver {

    private CompostChanceServerResolver() {}

    public static OptionalInt resolve(
            ServerLevel level,
            BlockPos pos,
            BlockState state,
            Entity interactingEntity,
            Compostable compostable
    ) {
        int fillLevel =
                state.getValue(
                        ComposterBlock.LEVEL
                );

        if (fillLevel >= 7) {
            return OptionalInt.empty();
        }

        LootContext context =
                new LootContext.Builder(
                        new LootParams.Builder(level)
                                .withParameter(
                                        LootContextParams.BLOCK_STATE,
                                        state
                                )
                                .withParameter(
                                        LootContextParams.ORIGIN,
                                        Vec3.atCenterOf(pos)
                                )
                                .withOptionalParameter(
                                        LootContextParams.INTERACTING_ENTITY,
                                        interactingEntity
                                )
                                .create(
                                        LootContextParamSets.BLOCK_INTERACT
                                )
                ).create(Optional.empty());

        OptionalDouble chance =
                getChance(
                        compostable.layers(),
                        context
                );

        if (chance.isEmpty()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(
                Math.round(
                        (float) (
                                chance.getAsDouble()
                                        * 100.0
                        )
                )
        );
    }

    private static OptionalDouble getChance(
            ResolvableInt resolvable,
            LootContext context
    ) {
        if (resolvable instanceof ResolvableInt.Constant constant) {
            return OptionalDouble.of(
                    constant.value() > 0
                            ? 1.0
                            : 0.0
            );
        }

        if (!(resolvable instanceof ResolvableInt.Reference reference)) {
            return OptionalDouble.empty();
        }

        var registry =
                context.getResolver()
                        .lookupOrThrow(
                                Registries.CONTEXT_INT_PROVIDER
                        );

        var holder =
                registry.get(reference.key());

        if (holder.isEmpty()) {
            return OptionalDouble.empty();
        }

        return getChance(
                holder.get().value(),
                context
        );
    }

    private static OptionalDouble getChance(
            Holder<ContextIntProvider> holder,
            LootContext context
    ) {
        return getChance(
                holder.value(),
                context
        );
    }

    private static OptionalDouble getChance(
            ContextIntProvider provider,
            LootContext context
    ) {
        if (provider instanceof ConstantValue constant) {
            return OptionalDouble.of(
                    constant.value() > 0
                            ? 1.0
                            : 0.0
            );
        }

        if (provider instanceof NumberDispatcher dispatcher) {
            for (DispatcherProvider.Case<ContextIntProvider> valueCase :
                    dispatcher.cases()) {

                Optional<Boolean> result =
                        ContextIntExactResolver.testCondition(
                                valueCase.condition(),
                                context
                        );

                if (result.isEmpty()) {
                    return OptionalDouble.empty();
                }

                if (result.get()) {
                    return getChance(
                            valueCase.value(),
                            context
                    );
                }
            }

            return getChance(
                    dispatcher.defaultValue(),
                    context
            );
        }

        if (provider instanceof ConditionalValue conditional) {
            Optional<Boolean> result =
                    ContextIntExactResolver.testCondition(
                            conditional.condition(),
                            context
                    );

            if (result.isEmpty()) {
                return OptionalDouble.empty();
            }

            return getChance(
                    result.get()
                            ? conditional.onTrue()
                            : conditional.onFalse(),
                    context
            );
        }

        if (provider instanceof WeightedListValue weighted) {
            double weightedChance = 0.0;
            int totalWeight = 0;

            for (Weighted<Holder<ContextIntProvider>> entry :
                    weighted.distribution().unwrap()) {

                if (entry.weight() <= 0) {
                    continue;
                }

                OptionalDouble chance =
                        getChance(
                                entry.value(),
                                context
                        );

                if (chance.isEmpty()) {
                    return OptionalDouble.empty();
                }

                weightedChance +=
                        chance.getAsDouble()
                                * entry.weight();

                totalWeight +=
                        entry.weight();
            }

            if (totalWeight <= 0) {
                return OptionalDouble.empty();
            }

            return OptionalDouble.of(
                    weightedChance
                            / totalWeight
            );
        }

        OptionalInt deterministicValue =
                ContextIntExactResolver.resolve(
                        provider,
                        context
                );

        if (deterministicValue.isPresent()) {
            return OptionalDouble.of(
                    deterministicValue.getAsInt() > 0
                            ? 1.0
                            : 0.0
            );
        }

        return OptionalDouble.empty();
    }
}