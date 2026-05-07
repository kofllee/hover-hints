package net.kofllee.hoverhints.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kofllee.hoverhints.loot.MobLootCalculator;
import net.kofllee.hoverhints.loot.MobLootEntry;
import net.kofllee.hoverhints.loot.MobLootKey;
import net.kofllee.hoverhints.loot.ServerMobLootCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class MobLootServerNetworking {

    private MobLootServerNetworking() {}

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                MobLootRequestPayload.ID,
                (payload, context) -> context.server().execute(() -> handle(context.player(), payload))
        );
    }

    private static void handle(ServerPlayer player, MobLootRequestPayload payload) {
        Entity entity = player.level().getEntity(payload.entityId());

        if (!(entity instanceof LivingEntity)) {
            return;
        }

        Item weaponItem = BuiltInRegistries.ITEM.getValue(payload.weaponItemId());

        MobLootKey key = new MobLootKey(
                entity.getType(),
                weaponItem,
                payload.lootingLevel()
        );

        List<MobLootEntry> entries = ServerMobLootCache.getOrCompute(
                key,
                () -> MobLootCalculator.calculate(
                        player.level(),
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