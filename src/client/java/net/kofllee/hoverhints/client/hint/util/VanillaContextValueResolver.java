package net.kofllee.hoverhints.client.hint.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.OptionalInt;

public final class VanillaContextValueResolver {

    private VanillaContextValueResolver() {}

    public static OptionalInt resolveFuelBurnTime(
            ResolvableInt value,
            BlockState furnaceState
    ) {
        if (value instanceof ResolvableInt.Constant constant) {
            return OptionalInt.of(constant.value());
        }

        if (!(value instanceof ResolvableInt.Reference reference)) {
            return OptionalInt.empty();
        }

        ResourceKey<ContextIntProvider> key = reference.key();

        int baseTicks;

        if (key.equals(ContextIntProviders.COOKING_TIME_BAMBOO)) {
            baseTicks = 50;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOL_SLABS)) {
            baseTicks = 50;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOL_CARPETS)) {
            baseTicks = 67;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_DRY_PLANTS)) {
            baseTicks = 100;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_EXTRA_SMALL)) {
            baseTicks = 100;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOL)) {
            baseTicks = 100;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOD_SLABS)) {
            baseTicks = 150;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_LARGE)) {
            baseTicks = 200;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_ROOTS)) {
            baseTicks = 300;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOD_BLOCKS)) {
            baseTicks = 300;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_WOOD_ITEMS_SMALL)) {
            baseTicks = 300;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_HANGING_SIGNS)) {
            baseTicks = 800;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_BOATS)) {
            baseTicks = 1200;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_COAL)) {
            baseTicks = 1600;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_BLAZE_ROD)) {
            baseTicks = 2400;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_DRIED_KELP_BLOCK)) {
            baseTicks = 4001;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_COAL_BLOCK)) {
            baseTicks = 16000;
        } else if (key.equals(ContextIntProviders.COOKING_TIME_LAVA_BUCKET)) {
            baseTicks = 20000;
        } else {
            return OptionalInt.empty();
        }

        boolean fastFurnace =
                furnaceState.is(Blocks.SMOKER)
                        || furnaceState.is(Blocks.BLAST_FURNACE);

        if (fastFurnace) {
            baseTicks /= 2;
        }

        return OptionalInt.of(baseTicks);
    }

    public static OptionalInt resolveCompostChance(
            ResolvableInt value,
            BlockState composterState
    ) {
        if (value instanceof ResolvableInt.Constant constant) {
            return OptionalInt.of(
                    constant.value() > 0 ? 100 : 0
            );
        }

        if (!(value instanceof ResolvableInt.Reference reference)) {
            return OptionalInt.empty();
        }

        ResourceKey<ContextIntProvider> key =
                reference.key();

        int chance;

        if (key.equals(ContextIntProviders.COMPOSTABLE_LOW)) {
            chance = 30;
        } else if (key.equals(ContextIntProviders.COMPOSTABLE_LOW_MEDIUM)) {
            chance = 50;
        } else if (key.equals(ContextIntProviders.COMPOSTABLE_MEDIUM)) {
            chance = 65;
        } else if (key.equals(ContextIntProviders.COMPOSTABLE_MEDIUM_HIGH)) {
            chance = 85;
        } else if (key.equals(ContextIntProviders.COMPOSTABLE_ALWAYS_ADD_ONE)) {
            chance = 100;
        } else {
            return OptionalInt.empty();
        }

        int level =
                composterState.getValue(
                        ComposterBlock.LEVEL
                );

        // Vanilla 26.3 guarantees the first layer.
        if (level == 0) {
            return OptionalInt.of(100);
        }

        return OptionalInt.of(chance);
    }
}