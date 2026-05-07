package net.kofllee.hoverhints.mixin.loot;

import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantedCountIncreaseFunction.class)
public interface EnchantedCountIncreaseFunctionAccessor {

    @Accessor("value")
    NumberProvider hoverHints$getValue();

    @Accessor("limit")
    int hoverHints$getLimit();
}
