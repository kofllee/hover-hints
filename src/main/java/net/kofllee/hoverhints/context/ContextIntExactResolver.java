package net.kofllee.hoverhints.context;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.DispatcherProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.*;

import java.util.Optional;
import java.util.OptionalInt;

public final class ContextIntExactResolver {

    private ContextIntExactResolver() {}

    public static OptionalInt resolve(
            ResolvableInt resolvable,
            LootContext context
    ) {
        if (resolvable instanceof ResolvableInt.Constant constant) {
            return OptionalInt.of(constant.value());
        }

        if (!(resolvable instanceof ResolvableInt.Reference reference)) {
            return OptionalInt.empty();
        }

        var registry =
                context.getResolver()
                        .lookupOrThrow(
                                Registries.CONTEXT_INT_PROVIDER
                        );

        var holder =
                registry.get(reference.key());

        if (holder.isEmpty()) {
            return OptionalInt.empty();
        }

        return resolve(
                holder.get().value(),
                context
        );
    }

    public static OptionalInt resolve(
            Holder<ContextIntProvider> holder,
            LootContext context
    ) {
        return resolve(
                holder.value(),
                context
        );
    }

    public static OptionalInt resolve(
            ContextIntProvider provider,
            LootContext context
    ) {
        if (provider instanceof ConstantValue constant) {
            return OptionalInt.of(
                    constant.value()
            );
        }

        if (provider instanceof Quotient quotient) {
            OptionalInt left =
                    resolve(
                            quotient.left(),
                            context
                    );

            OptionalInt right =
                    resolve(
                            quotient.right(),
                            context
                    );

            if (left.isEmpty() || right.isEmpty()) {
                return OptionalInt.empty();
            }

            if (right.getAsInt() == 0) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(
                    left.getAsInt()
                            / right.getAsInt()
            );
        }

        if (provider instanceof ConditionalValue conditional) {
            Optional<Boolean> result =
                    testCondition(
                            conditional.condition(),
                            context
                    );

            if (result.isEmpty()) {
                return OptionalInt.empty();
            }

            return resolve(
                    result.get()
                            ? conditional.onTrue()
                            : conditional.onFalse(),
                    context
            );
        }

        if (provider instanceof NumberDispatcher dispatcher) {
            for (DispatcherProvider.Case<ContextIntProvider> valueCase :
                    dispatcher.cases()) {

                Optional<Boolean> result =
                        testCondition(
                                valueCase.condition(),
                                context
                        );

                if (result.isEmpty()) {
                    return OptionalInt.empty();
                }

                if (result.get()) {
                    return resolve(
                            valueCase.value(),
                            context
                    );
                }
            }

            return resolve(
                    dispatcher.defaultValue(),
                    context
            );
        }

        return OptionalInt.empty();
    }

    public static Optional<Boolean> testCondition(
            Holder<LootItemCondition> condition,
            LootContext context
    ) {
        LootItemCondition value =
                condition.value();

        // Known deterministic condition required by
        // vanilla fuel/composter providers.
        if (value instanceof MatchBlock) {
            return Optional.of(
                    value.test(context)
            );
        }

        return Optional.empty();
    }
}