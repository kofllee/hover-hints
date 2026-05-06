package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.util.BoneMealInfo;
import net.kofllee.hoverhints.client.hint.util.BoneMealResolver;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;

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

        if (!hintContext.heldStack().isOf(Items.BONE_MEAL)) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());
        BlockState stateAbove = hintContext.world().getBlockState(blockHitResult.getBlockPos().up());

        if(stateAbove.isOf(Blocks.WATER) && stateAbove.getFluidState().isStill() && state.isSideSolidFullSquare(hintContext.world(), blockHitResult.getBlockPos(), Direction.UP)) {
            out.add(new HintResult(HintIcons.GROWTH, Text.translatable("hint.hover_hints.bone_meal_generic").styled(style -> style.withColor(0x55FF55))));
            return;
        }

        if (!(state.getBlock() instanceof Fertilizable fertilizable)) {
            return;
        }

        if(!fertilizable.isFertilizable(hintContext.world(), blockHitResult.getBlockPos(), state)) {
            return;
        }

        Optional<BoneMealInfo> info = BoneMealResolver.resolve(state);

        if(info.isEmpty()) {
            return;
        }

        Text text = toText(info.get());

        out.add(new HintResult(HintIcons.GROWTH, text.copy().styled(style -> style.withColor(0x55FF55))));
    }

    private Text toText(BoneMealInfo info) {
        return switch (info.type()) {
            case GENERIC -> Text.translatable(
                    "hint.hover_hints.bone_meal_generic",
                    info.min(), info.max(), info.chance()
            );
            case STAGES -> info.min() == info.max()
                    ? Text.translatable(
                    "hint.hover_hints.bone_meal_stage_one_value",
                    info.min())
                    : Text.translatable(
                    "hint.hover_hints.bone_meal_stages",
                    info.min(), info.max()
            );

            case BLOCKS -> info.min() == info.max()
                    ? Text.translatable(
                    "hint.hover_hints.bone_meal_blocks_one_value",
                    info.min())
                    : Text.translatable(
                    "hint.hover_hints.bone_meal_blocks",
                    info.min(), info.max()
            );

            case UNBOUNDED_BLOCKS -> Text.translatable(
                    "hint.hover_hints.bone_meal_unbounded_blocks",
                    info.min()
            );

            case CHANCE -> Text.translatable(
                    "hint.hover_hints.bone_meal_chance",
                    info.chance()
            );

            case CHANCE_STAGE -> Text.translatable(
                    "hint.hover_hints.bone_meal_stage_chance",
                    info.chance(),
                    info.min()
            );

            case DROPS_ITEM -> Text.translatable("hint.hover_hints.bone_meal_drops_item");

            case TALLER -> Text.translatable("hint.hover_hints.bone_meal_taller");

            case SPREAD -> Text.translatable("hint.hover_hints.bone_meal_spread");
        };
    }
}
