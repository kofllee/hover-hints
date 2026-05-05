package net.kofllee.hoverhints.loot;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public record MobLootEntry(
        Item item,
        int minCount,
        int maxCount,
        float chancePercent
) { }
