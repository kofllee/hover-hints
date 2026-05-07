package net.kofllee.hoverhints.client.loot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.network.MobLootRequestPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class MobLootRequestSender {

    private MobLootRequestSender() {}

    public static void request(int entityId, ItemStack weaponStack, int lootingLevel) {
        Minecraft client = Minecraft.getInstance();

        if (client.getConnection() == null) {
            return;
        }

        Identifier weaponId = BuiltInRegistries.ITEM.getKey(weaponStack.getItem());

        ClientPlayNetworking.send(
                new MobLootRequestPayload(entityId, weaponId, lootingLevel)
        );
    }
}