package net.kofllee.hoverhints.client;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HoverHintClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("HoverHints");
    @Override
    public void onInitializeClient() {
        LOGGER.info("HoverHints client initialized");
    }
}
