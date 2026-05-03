package net.kofllee.hoverhints.client.hint.provider;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kofllee.hoverhints.client.hint.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;

import java.util.List;

public final class FuelHintProvider implements HintProvider {

    public String id() {
        return "fuel";
    }

    @Override
    public void getHint(HintContext hintContext, List<HintResult> out) {
        if(!(hintContext.hitResult() instanceof BlockHitResult blockHitResult)) {
            return;
        }

        BlockEntity blockEntity = hintContext.world().getBlockEntity(blockHitResult.getBlockPos());

        if(!(blockEntity instanceof AbstractFurnaceBlockEntity)) {
            return;
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return;
        }

        Integer burnTicks = FuelRegistry.INSTANCE.get(stack.getItem());

        if(burnTicks == null || burnTicks < 0) {
            return;
        }

        out.add(new HintResult(HintIcons.FIRE, Text.translatable("hint.hover_hints.fuel_burn_time", HintTimeFormatter.formatTicks(burnTicks)).styled(style -> style.withColor(0xa11250))));
    }
}
