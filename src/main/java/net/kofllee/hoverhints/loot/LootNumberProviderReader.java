package net.kofllee.hoverhints.loot;

import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;

public final class LootNumberProviderReader {

    private LootNumberProviderReader(){}

    public static LootNumberRange readIntRange(LootNumberProvider provider){
        if(provider instanceof ConstantLootNumberProvider constant){
            int value = Math.round(constant.value());
            return new LootNumberRange(value, value);
        }

        if(provider instanceof UniformLootNumberProvider uniform){
            int min = Math.round(uniform.min().nextFloat(null));
            int max = Math.round(uniform.max().nextFloat(null));
            return new LootNumberRange(min, max);
        }

        return LootNumberRange.one();
    }

    public static Float readConstantFLoat(LootNumberProvider provider){
        if(provider instanceof ConstantLootNumberProvider constant){
            return constant.value();
        }

        return null;
    }
}
