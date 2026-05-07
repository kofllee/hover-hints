package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

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
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if(!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());

        if(!(state.getBlock() instanceof ComposterBlock)) {
            return;
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return;
        }

        Float chance = ComposterBlock.COMPOSTABLES.get(stack.getItem());

        if(chance == null) {
            return;
        }

        int percent = Math.round(chance * 100);

        out.add(new HintResult(HintIcons.BONE_MEAL, Component.translatable("hint.hover_hints.compost_chance", percent).withStyle(style -> style.withColor(getColor(percent)))));
    }

    private int getColor(int percent) {
        if(percent >= 85) {
            return 0x55FF55;
        }

        if (percent >= 50) {
            return 0xFFFF55;
        }

        return 0xFFAA00;
    }
}
