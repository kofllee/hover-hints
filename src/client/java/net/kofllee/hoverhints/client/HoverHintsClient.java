package net.kofllee.hoverhints.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.kofllee.hoverhints.client.animal.ClientAnimalAgeState;
import net.kofllee.hoverhints.client.archaeology.ClientArchaeologyState;
import net.kofllee.hoverhints.client.command.HoverHintClientCommands;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.HoverHintProviders;
import net.kofllee.hoverhints.client.hint.input.HoverHintKeybinds;
import net.kofllee.hoverhints.client.hint.render.HintHudRenderer;
import net.kofllee.hoverhints.client.loot.ClientMobLootCache;
import net.kofllee.hoverhints.client.network.AnimalAgeClientNetworking;
import net.kofllee.hoverhints.client.network.ArchaeologyLootClientNetworking;
import net.kofllee.hoverhints.client.network.MobLootClientNetworking;
import net.kofllee.hoverhints.client.network.VillagerPoiClientNetworking;
import net.kofllee.hoverhints.client.villager.ClientVillagerPoiState;
import net.kofllee.hoverhints.network.HoverHintsPayloads;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.kofllee.hoverhints.client.hint.provider.*;

public class HoverHintsClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("HoverHints");
    @Override
    public void onInitializeClient() {
        LOGGER.info("HoverHints client initialized");

        HoverHintsPayloads.register();

        MobLootClientNetworking.register();
        ArchaeologyLootClientNetworking.register();
        VillagerPoiClientNetworking.register();
        AnimalAgeClientNetworking.register();

        HoverHintsConfigManager.load();

        HoverHintKeybinds.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> HoverHintKeybinds.tick());

        HintHudRenderer.register();

        HoverHintClientCommands.register();

        HoverHintProviders.register(new ComposterHintProvider());
        HoverHintProviders.register(new FuelHintProvider());
        HoverHintProviders.register(new GrindStoneProvider());
        HoverHintProviders.register(new BoneMealHintProvider());
        HoverHintProviders.register(new TameableAnimalHintProvider());
        HoverHintProviders.register(new RedstonePowerHintProvider());
        HoverHintProviders.register(new SilkTouchHintProvider());
        HoverHintProviders.register(new AnimalFeedHintProvider());
        HoverHintProviders.register(new MobLootHintProvider());
        HoverHintProviders.register(new ArchaeologyLootHintProvider());
        HoverHintProviders.register(new VillagerPoiHintProvider());

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientAnimalAgeState.clear();
            ClientMobLootCache.clear();
            ClientArchaeologyState.clear();
            ClientVillagerPoiState.clear();
        });
    }
}
