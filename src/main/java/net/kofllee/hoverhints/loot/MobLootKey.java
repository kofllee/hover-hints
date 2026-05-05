package net.kofllee.hoverhints.loot;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public record MobLootKey (
        EntityType<?> entityType,
        Item weaponItem,
        int lootingLevel
){
}
