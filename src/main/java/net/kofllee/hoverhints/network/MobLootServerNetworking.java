package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.loot.MobLootCalculator;
import net.kofllee.hoverhints.loot.MobLootEntry;
import net.kofllee.hoverhints.loot.MobLootKey;
import net.kofllee.hoverhints.loot.ServerMobLootCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public final class MobLootServerNetworking {

    private MobLootServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                MobLootRequestPayload.ID,
                (payload, context) -> context.server().execute(() -> handle(context.player(), payload))
        );
    }

    private static void handle(ServerPlayerEntity player, MobLootRequestPayload payload) {
        Entity entity = player.getEntityWorld().getEntityById(payload.entityId());

        if (!(entity instanceof LivingEntity)) {
            return;
        }

        Item weaponItem = Registries.ITEM.get(payload.weaponItemId());

        MobLootKey key = new MobLootKey(
                entity.getType(),
                weaponItem,
                payload.lootingLevel()
        );

        List<MobLootEntry> entries = ServerMobLootCache.getOrCompute(
                key,
                () -> MobLootCalculator.calculate(
                        player.getEntityWorld(),
                        new MobLootKey(
                                entity.getType(),
                                weaponItem,
                                payload.lootingLevel()
                        ))
        );

        ServerPlayNetworking.send(
                player,
                new MobLootResponsePayload(payload.entityId(), entries)
        );
    }
}