package net.kofllee.hoverhints.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public final class LootNumberProviderReader {

    private LootNumberProviderReader() {}

    public static LootNumberRange readIntRange(
            Holder<ContextIntProvider> holder
    ) {
        return readIntRange(holder.value());
    }

    private static LootNumberRange readIntRange(
            ContextIntProvider provider
    ) {
        if (provider instanceof
                net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue constant) {

            int value = constant.value();

            return new LootNumberRange(value, value);
        }

        if (provider instanceof
                net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator uniform) {

            LootNumberRange min = readIntRange(uniform.min());
            LootNumberRange max = readIntRange(uniform.max());

            return new LootNumberRange(
                    min.min(),
                    max.max()
            );
        }

        return LootNumberRange.one();
    }

    public static LootFloatRange readFloatRange(
            Holder<ContextFloatProvider> holder
    ) {
        return readFloatRange(holder.value());
    }

    private static LootFloatRange readFloatRange(
            ContextFloatProvider provider
    ) {
        if (provider instanceof
                net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue constant) {

            float value = constant.value();

            return new LootFloatRange(value, value);
        }

        if (provider instanceof
                net.minecraft.world.level.storage.loot.providers.number.floats.UniformGenerator uniform) {

            LootFloatRange min = readFloatRange(uniform.min());
            LootFloatRange max = readFloatRange(uniform.max());

            return new LootFloatRange(
                    min.min(),
                    max.max()
            );
        }

        return LootFloatRange.one();
    }
}