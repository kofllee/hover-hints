package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.registry.HoverHintBlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public final class SilkTouchHintProvider implements HintProvider {

    @Override
    public String id() {
        return "silk_touch";
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

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return;
        }

        if(stack.get(DataComponents.TOOL) == null) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());

        if (!state.is(HoverHintBlockTags.SILK_TOUCH_RELEVANT)) {
            return;
        }

        out.add(new HintResult(
                Items.DIAMOND_PICKAXE.getDefaultInstance(),
                Component.translatable("hint.hover_hints.silk_touch")
                        .withStyle(s -> s.withColor(0x55FFFF))
        ));
    }
}
