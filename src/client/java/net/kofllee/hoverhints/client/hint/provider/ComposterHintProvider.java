package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.context.ClientContextualValueState;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.util.VanillaContextValueResolver;
import net.kofllee.hoverhints.network.ContextualValueType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.OptionalInt;

public final class ComposterHintProvider implements HintProvider {

    @Override
    public String id() {
        return "composter";
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

        var state =
                hintContext.world()
                        .getBlockState(
                                blockHit.getBlockPos()
                        );

        if (!(state.getBlock()
                instanceof ComposterBlock)) {
            return;
        }

        int level =
                state.getValue(
                        ComposterBlock.LEVEL
                );

        if (level >= 7) {
            return;
        }

        ItemStack stack =
                hintContext.heldStack();

        if (stack.isEmpty()) {
            return;
        }

        Compostable compostable =
                stack.get(
                        DataComponents.COMPOSTABLE
                );

        if (compostable == null) {
            return;
        }

        OptionalInt chance =
                VanillaContextValueResolver
                        .resolveCompostChance(
                                compostable.layers(),
                                state
                        );

        if (chance.isEmpty()
                && compostable.layers()
                instanceof ResolvableInt.Reference reference) {

            chance =
                    ClientContextualValueState.getOrRequest(
                            ContextualValueType.COMPOST_CHANCE,
                            blockHit.getBlockPos(),
                            reference.key().identifier(),
                            state,
                            hintContext.world().getGameTime()
                    );
        }

        if (chance.isEmpty()) {
            return;
        }

        int percent =
                chance.getAsInt();

        out.add(
                new HintResult(
                        HintIcons.BONE_MEAL,
                        Component.translatable(
                                "hint.hover_hints.compost_chance",
                                percent
                        ).withStyle(
                                style ->
                                        style.withColor(
                                                getColor(percent)
                                        )
                        )
                )
        );
    }

    private int getColor(int percent) {
        if (percent >= 85) {
            return 0x55FF55;
        }

        if (percent >= 50) {
            return 0xFFFF55;
        }

        return 0xFFAA00;
    }
}