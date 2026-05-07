package net.kofllee.hoverhints.client.config;

import net.fabricmc.loader.api.FabricLoader;

public final class HoverHintsConfigDependencies {
    public static final String CLOTH_CONFIG_ID = "cloth-config";

    private HoverHintsConfigDependencies() {}

    public static boolean hasClothConfig() {
        return FabricLoader.getInstance().isModLoaded(CLOTH_CONFIG_ID);
    }
}