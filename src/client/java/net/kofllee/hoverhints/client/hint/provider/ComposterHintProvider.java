package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Optional;

public final class ComposterHintProvider implements HintProvider {
    @Override
    public String id() {
        return "composter";
    }

    @Override
    public Optional<HintResult> getHint(HintContext hintContext) {
        if(!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return Optional.empty();
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());

        if(!(state.getBlock() instanceof ComposterBlock)) {
            return Optional.empty();
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return Optional.empty();
        }

        Float chance = ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.get(stack.getItem());

        if(chance == null) {
            return Optional.empty();
        }

        int percent = Math.round(chance * 100);

        return Optional.of(new HintResult(Text.literal("Compost chance: " + percent + "%").styled(style -> style.withColor(getColor(percent)))));
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
