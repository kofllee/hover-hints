package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.util.BoneMealInfo;
import net.kofllee.hoverhints.client.hint.util.BoneMealResolver;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Optional;

public class BoneMealHintProvider implements HintProvider {
    @Override
    public String id() {
        return "bone_meal";
    }

    @Override
    public boolean requiresServer() {
        return false;
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        if (!hintContext.heldStack().is(Items.BONE_MEAL)) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());
        BlockState stateAbove = hintContext.world().getBlockState(blockHitResult.getBlockPos().above());

        if(stateAbove.is(Blocks.WATER) && stateAbove.getFluidState().isSource() && state.isFaceSturdy(hintContext.world(), blockHitResult.getBlockPos(), Direction.UP)) {
            out.add(new HintResult(HintIcons.GROWTH, Component.translatable("hint.hover_hints.bone_meal_generic").withStyle(style -> style.withColor(0x55FF55))));
            return;
        }

        if (!(state.getBlock() instanceof BonemealableBlock fertilizable)) {
            return;
        }

        if(!fertilizable.isValidBonemealTarget(hintContext.world(), blockHitResult.getBlockPos(), state, BonemealSource.INTERACTION)) {
            return;
        }

        Optional<BoneMealInfo> info = BoneMealResolver.resolve(state);

        if(info.isEmpty()) {
            return;
        }

        Component text = toText(info.get());

        out.add(new HintResult(HintIcons.GROWTH, text.copy().withStyle(style -> style.withColor(0x55FF55))));
    }

    private Component toText(BoneMealInfo info) {
        return switch (info.type()) {
            case GENERIC -> Component.translatable(
                    "hint.hover_hints.bone_meal_generic",
                    info.min(), info.max(), info.chance()
            );
            case STAGES -> info.min() == info.max()
                    ? Component.translatable(
                    "hint.hover_hints.bone_meal_stage_one_value",
                    info.min())
                    : Component.translatable(
                    "hint.hover_hints.bone_meal_stages",
                    info.min(), info.max()
            );

            case BLOCKS -> info.min() == info.max()
                    ? Component.translatable(
                    "hint.hover_hints.bone_meal_blocks_one_value",
                    info.min())
                    : Component.translatable(
                    "hint.hover_hints.bone_meal_blocks",
                    info.min(), info.max()
            );

            case UNBOUNDED_BLOCKS -> Component.translatable(
                    "hint.hover_hints.bone_meal_unbounded_blocks",
                    info.min()
            );

            case CHANCE -> Component.translatable(
                    "hint.hover_hints.bone_meal_chance",
                    info.chance()
            );

            case CHANCE_STAGE -> Component.translatable(
                    "hint.hover_hints.bone_meal_stage_chance",
                    info.chance(),
                    info.min()
            );

            case DROPS_ITEM -> Component.translatable("hint.hover_hints.bone_meal_drops_item");

            case TALLER -> Component.translatable("hint.hover_hints.bone_meal_taller");

            case SPREAD -> Component.translatable("hint.hover_hints.bone_meal_spread");
        };
    }
}
