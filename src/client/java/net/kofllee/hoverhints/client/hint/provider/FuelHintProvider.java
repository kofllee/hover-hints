package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.context.ClientContextualValueState;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.HintTimeFormatter;
import net.kofllee.hoverhints.client.hint.util.VanillaContextValueResolver;
import net.kofllee.hoverhints.network.ContextualValueType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.OptionalInt;

public final class FuelHintProvider implements HintProvider {

    @Override
    public String id() {
        return "fuel";
    }

    @Override
    public boolean requiresServer() {
        return false;
    }

    @Override
    public void getHint(
            HintContext hintContext,
            List<HintResult> out
    ) {
        if (!(hintContext.hitResult()
                instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockEntity blockEntity =
                hintContext.world()
                        .getBlockEntity(
                                blockHit.getBlockPos()
                        );

        if (!(blockEntity
                instanceof AbstractFurnaceBlockEntity)) {
            return;
        }

        ItemStack stack =
                hintContext.heldStack();

        if (stack.isEmpty()) {
            return;
        }

        CookingFuel fuel =
                stack.get(
                        DataComponents.COOKING_FUEL
                );

        if (fuel == null) {
            return;
        }

        var state =
                hintContext.world()
                        .getBlockState(
                                blockHit.getBlockPos()
                        );

        OptionalInt burnTicks =
                VanillaContextValueResolver
                        .resolveFuelBurnTime(
                                fuel.burnTime(),
                                state
                        );

        if (burnTicks.isEmpty()
                && fuel.burnTime()
                instanceof ResolvableInt.Reference reference) {

            burnTicks =
                    ClientContextualValueState.getOrRequest(
                            ContextualValueType.FUEL_BURN_TIME,
                            blockHit.getBlockPos(),
                            reference.key().identifier(),
                            state,
                            hintContext.world().getGameTime()
                    );
        }

        if (burnTicks.isEmpty()
                || burnTicks.getAsInt() <= 0) {
            return;
        }

        out.add(
                new HintResult(
                        HintIcons.FIRE,
                        Component.translatable(
                                "hint.hover_hints.fuel_burn_time",
                                HintTimeFormatter.formatTicks(
                                        burnTicks.getAsInt()
                                )
                        ).withStyle(
                                style ->
                                        style.withColor(
                                                0xD84C45
                                        )
                        )
                )
        );
    }
}