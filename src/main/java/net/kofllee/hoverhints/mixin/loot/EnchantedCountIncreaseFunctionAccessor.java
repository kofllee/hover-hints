package net.kofllee.hoverhints.mixin.loot;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantedCountIncreaseFunction.class)
public interface EnchantedCountIncreaseFunctionAccessor {

    @Accessor("count")
    Holder<ContextFloatProvider> hoverHints$getCount();

    @Accessor("limit")
    int hoverHints$getLimit();
}