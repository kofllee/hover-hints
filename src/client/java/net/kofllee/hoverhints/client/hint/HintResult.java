package net.kofllee.hoverhints.client.hint;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public record HintResult(@Nullable Identifier iconTexture, @Nullable ItemStack iconStack, Component text) {

    public HintResult (Identifier iconTexture, Component text) {
        this(iconTexture, null, text);
    }

    public HintResult(ItemStack iconStack, Component text) {
        this(null, iconStack, text);
    }
}
