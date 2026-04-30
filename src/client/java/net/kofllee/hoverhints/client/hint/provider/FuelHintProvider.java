package net.kofllee.hoverhints.client.hint.provider;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Optional;

public final class FuelHintProvider implements HintProvider {

    @Override
    public Optional<HintResult> getHint(HintContext hintContext) {
        if(!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return Optional.empty();
        }

        BlockState state = hintContext.world().getBlockState(blockHitResult.getBlockPos());
        BlockEntity blockEntity = hintContext.world().getBlockEntity(blockHitResult.getBlockPos());

        if(!(blockEntity instanceof AbstractFurnaceBlockEntity)) {
            return Optional.empty();
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return Optional.empty();
        }

        Integer burnTicks = FuelRegistry.INSTANCE.get(stack.getItem());

        if(burnTicks == null || burnTicks < 0) {
            return Optional.empty();
        }

        int seconds = burnTicks / 20;

        return Optional.of(new HintResult(Text.literal("Burn time: " + seconds + "s").styled(style -> style.withColor(Formatting.GOLD)), Formatting.GOLD.getColorValue()));
    }
}
