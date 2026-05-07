package net.kofllee.hoverhints.loot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record MobLootEntry(
        Item item,
        int minCount,
        int maxCount,
        float chancePercent
) { }
