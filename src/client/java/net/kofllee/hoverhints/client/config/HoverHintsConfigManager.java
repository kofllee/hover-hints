package net.kofllee.hoverhints.client.config;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public final class HoverHintsConfigManager {

    private static final Gson GSON = new Gson();

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("hover-hints.json");

    private static HoverHintsConfig config = new HoverHintsConfig();

    public static HoverHintsConfig getConfig() {
        if (config == null) {
            config = new HoverHintsConfig();
        }
        return config;
    }

    public static void load(){
        if(!CONFIG_PATH.toFile().exists()){
            save();
            return;
        }

        try{
            config = GSON.fromJson(Files.readString(CONFIG_PATH), HoverHintsConfig.class);

            validateConfig();
            save();

        } catch(IOException e) {
            config = new HoverHintsConfig();
            save();
        }
    }

    private static void validateConfig() {
        if (config == null) {
            config = new HoverHintsConfig();
        }

        if (config.mode == null) {
            config.mode = HintActivationMode.HOLD_KEY;
        }

        if (config.renderConfig == null) {
            config.renderConfig = new HintRenderConfig();
        }

        if (config.renderConfig.anchor == null) {
            config.renderConfig.anchor = HintAnchor.BELOW_CROSSHAIR;
        }

        if (config.providers == null) {
            config.providers = new HashMap<>();
        }

        config.providers.entrySet().removeIf(entry ->
                entry.getKey() == null || entry.getValue() == null
        );
    }

    public static void save(){
        try{
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(config));
        }catch(Exception e){}
    }

    private HoverHintsConfigManager() {}
}
