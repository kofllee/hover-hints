package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.registry.HoverHintBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;

import java.util.List;

public final class SilkTouchHintProvider implements HintProvider {

    @Override
    public String id() {
        return "silk_touch";
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

        if(stack.get(DataComponentTypes.TOOL) == null) {
            return;
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());

        if (!state.getRegistryEntry()
                .isIn(HoverHintBlockTags.SILK_TOUCH_RELEVANT)) {
            return;
        }

        out.add(new HintResult(
                Items.DIAMOND_PICKAXE.getDefaultStack(),
                Text.translatable("hint.hover_hints.silk_touch")
                        .styled(s -> s.withColor(0x55FFFF))
        ));
    }
}
