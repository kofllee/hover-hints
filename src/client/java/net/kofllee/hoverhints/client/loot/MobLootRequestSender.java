package net.kofllee.hoverhints.client.loot;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kofllee.hoverhints.network.MobLootRequestPayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class MobLootRequestSender {

    private MobLootRequestSender() {}

    public static void request(int entityId, ItemStack weaponStack, int lootingLevel) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.getNetworkHandler() == null) {
            return;
        }

        Identifier weaponId = Registries.ITEM.getId(weaponStack.getItem());

        ClientPlayNetworking.send(
                new MobLootRequestPayload(entityId, weaponId, lootingLevel)
        );
    }
}