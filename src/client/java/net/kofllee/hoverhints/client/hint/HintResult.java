package net.kofllee.hoverhints.client.hint;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record HintResult(@Nullable Identifier iconTexture, @Nullable ItemStack iconStack, Text text) {

    public HintResult (Identifier iconTexture, Text text) {
        this(iconTexture, null, text);
    }

    public HintResult(ItemStack iconStack, Text text) {
        this(null, iconStack, text);
    }
}
