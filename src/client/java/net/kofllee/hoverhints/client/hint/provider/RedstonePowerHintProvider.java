package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class RedstonePowerHintProvider implements HintProvider {
    @Override
    public String id() {
        return "redstone_power";
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

        if (!isRedstoneProbeItem(hintContext.heldStack().getItem())) {
            return;
        }

        Level world = hintContext.world();
        BlockPos pos = blockHitResult.getBlockPos();

        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return;
        }

        int power = world.getBestNeighborSignal(pos);

        if (power <= 0) {
            out.add(new HintResult(
                    HintIcons.REDSTONE,
                    Component.translatable("hint.hover_hints.redstone_unpowered")
                            .withStyle(style -> style.withColor(0xAAAAAA))
            ));
            return;
        }

        out.add(new HintResult(HintIcons.REDSTONE, Component.translatable("hint.hover_hints.redstone_power", power).withStyle(style -> style.withColor(0xFF5555))));
    }

    private static boolean isRedstoneProbeItem(Item item) {
        return item == Items.REDSTONE
                || item == Items.REDSTONE_TORCH;
    }
}
