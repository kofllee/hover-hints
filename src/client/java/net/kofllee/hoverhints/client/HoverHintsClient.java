package net.kofllee.hoverhints.client;

import net.fabricmc.api.ClientModInitializer;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.input.HoverHintKeybinds;
import net.kofllee.hoverhints.client.hint.render.HintHudRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HoverHintsClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("HoverHints");
    @Override
    public void onInitializeClient() {
        LOGGER.info("HoverHints client initialized");

        HoverHintsConfigManager.load();
        HoverHintKeybinds.register();
        HintHudRenderer.register();

    }
}
