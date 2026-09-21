package net.kofllee.hoverhints.context;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.OptionalInt;

public final class CookingFuelServerResolver {

    private CookingFuelServerResolver() {}

    public static OptionalInt resolve(
            ServerLevel level,
            AbstractFurnaceBlockEntity furnace,
            ItemStack stack
    ) {
        CookingFuel fuel =
                stack.get(
                        DataComponents.COOKING_FUEL
                );

        if (fuel == null) {
            return OptionalInt.empty();
        }

        LootContext context =
                new LootContext.Builder(
                        new LootParams.Builder(level)
                                .withParameter(
                                        LootContextParams.BLOCK_STATE,
                                        furnace.getBlockState()
                                )
                                .withParameter(
                                        LootContextParams.BLOCK_ENTITY,
                                        furnace
                                )
                                .withParameter(
                                        LootContextParams.ORIGIN,
                                        Vec3.atCenterOf(
                                                furnace.getBlockPos()
                                        )
                                )
                                .withParameter(
                                        LootContextParams.CONTAINER,
                                        furnace
                                )
                                .create(
                                        LootContextParamSets.CONTAINER_PROCESS
                                )
                ).create(Optional.empty());

        OptionalInt value =
                ContextIntExactResolver.resolve(
                        fuel.burnTime(),
                        context
                );

        if (value.isEmpty()
                || value.getAsInt() <= 0) {

            return OptionalInt.empty();
        }

        return value;
    }
}