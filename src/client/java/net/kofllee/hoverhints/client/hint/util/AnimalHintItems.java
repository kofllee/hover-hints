package net.kofllee.hoverhints.client.hint.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class AnimalHintItems {

    private AnimalHintItems() {}

    public static boolean isParrotTamingItem(ItemStack stack) {
        return stack.isOf(Items.WHEAT_SEEDS)
                || stack.isOf(Items.MELON_SEEDS)
                || stack.isOf(Items.PUMPKIN_SEEDS)
                || stack.isOf(Items.BEETROOT_SEEDS);
    }

}
