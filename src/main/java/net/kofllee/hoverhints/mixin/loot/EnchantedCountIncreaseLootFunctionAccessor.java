package net.kofllee.hoverhints.mixin.loot;

import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantedCountIncreaseLootFunction.class)
public interface EnchantedCountIncreaseLootFunctionAccessor {

    @Accessor("count")
    LootNumberProvider hoverHints$getCount();

    @Accessor("limit")
    int hoverHints$getLimit();
}
