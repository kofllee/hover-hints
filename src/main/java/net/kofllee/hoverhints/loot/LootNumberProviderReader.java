package net.kofllee.hoverhints.loot;

import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class LootNumberProviderReader {

    private LootNumberProviderReader(){}

    public static LootNumberRange readIntRange(NumberProvider provider){
        if(provider instanceof ConstantValue constant){
            int value = Math.round(constant.value());
            return new LootNumberRange(value, value);
        }

        if(provider instanceof UniformGenerator uniform){
            int min = Math.round(uniform.min().getFloat(null));
            int max = Math.round(uniform.max().getFloat(null));
            return new LootNumberRange(min, max);
        }

        return LootNumberRange.one();
    }

    public static Float readConstantFLoat(NumberProvider provider){
        if(provider instanceof ConstantValue constant){
            return constant.value();
        }

        return null;
    }
}
