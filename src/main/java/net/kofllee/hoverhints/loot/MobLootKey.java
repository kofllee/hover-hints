package net.kofllee.hoverhints.loot;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public record MobLootKey (
        EntityType<?> entityType,
        Item weaponItem,
        int lootingLevel
){
}
