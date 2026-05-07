package net.kofllee.hoverhints.client.hint.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AnimalHintItems {

    private AnimalHintItems() {}

    public static boolean isParrotTamingItem(ItemStack stack) {
        return stack.is(Items.WHEAT_SEEDS)
                || stack.is(Items.MELON_SEEDS)
                || stack.is(Items.PUMPKIN_SEEDS)
                || stack.is(Items.BEETROOT_SEEDS);
    }

}
