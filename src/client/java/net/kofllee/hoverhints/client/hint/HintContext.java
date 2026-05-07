package net.kofllee.hoverhints.client.hint;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;

public record HintContext(Minecraft minecraftClient,
                          LocalPlayer player,
                          ClientLevel world,
                          HitResult hitResult,
                          ItemStack heldStack){
}
