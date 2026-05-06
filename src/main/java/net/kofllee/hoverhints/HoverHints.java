package net.kofllee.hoverhints;

import net.fabricmc.api.ModInitializer;
import net.kofllee.hoverhints.network.*;

public class HoverHints implements ModInitializer {

    public static final String MOD_ID = "hover_hints";

    @Override
    public void onInitialize() {
        HoverHintsPayloads.register();
        MobLootServerNetworking.register();
        ArchaeologyLootServerNetworking.register();
        VillagerPoiServerNetworking.register();
        AnimalAgeServerNetworking.register();
    }
}