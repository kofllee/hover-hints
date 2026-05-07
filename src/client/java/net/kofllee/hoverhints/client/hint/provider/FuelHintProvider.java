package net.kofllee.hoverhints.client.hint.provider;

import net.minecraft.world.level.block.entity.FuelValues;
import net.kofllee.hoverhints.client.hint.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public final class FuelHintProvider implements HintProvider {

    public String id() {
        return "fuel";
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

        BlockEntity blockEntity = hintContext.world().getBlockEntity(blockHitResult.getBlockPos());

        if(!(blockEntity instanceof AbstractFurnaceBlockEntity)) {
            return;
        }

        ItemStack stack = hintContext.heldStack();

        if(stack.isEmpty()) {
            return;
        }

        FuelValues fuelRegistry = hintContext.world().fuelValues();
        int burnTicks = fuelRegistry.burnDuration(stack);

        if(burnTicks <= 0) {
            return;
        }

        out.add(new HintResult(HintIcons.FIRE, Component.translatable("hint.hover_hints.fuel_burn_time", HintTimeFormatter.formatTicks(burnTicks)).withStyle(style -> style.withColor(0xd84c45))));
    }
}
