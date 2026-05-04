package net.kofllee.hoverhints.client.hint.provider;

import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintIcons;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class RedstonePowerHintProvider implements HintProvider {
    @Override
    public String id() {
        return "redstone_power";
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if (!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        if (!isRedstoneProbeItem(hintContext.heldStack().getItem())) {
            return;
        }

        World world = hintContext.world();
        BlockPos pos = blockHitResult.getBlockPos();

        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return;
        }

        int power = world.getReceivedRedstonePower(pos);

        if (power <= 0) {
            out.add(new HintResult(
                    HintIcons.REDSTONE,
                    Text.translatable("hint.hover_hints.redstone_unpowered")
                            .styled(style -> style.withColor(0xAAAAAA))
            ));
            return;
        }

        out.add(new HintResult(HintIcons.REDSTONE, Text.translatable("hint.hover_hints.redstone_power", power).styled(style -> style.withColor(0xFF5555))));
    }

    private static boolean isRedstoneProbeItem(Item item) {
        return item == Items.REDSTONE
                || item == Items.REDSTONE_TORCH;
    }
}
