package net.kofllee.hoverhints.client.hint;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;

public record HintContext(MinecraftClient minecraftClient,
                          ClientPlayerEntity player,
                          ClientWorld world,
                          HitResult hitResult,
                          ItemStack heldStack){
}
