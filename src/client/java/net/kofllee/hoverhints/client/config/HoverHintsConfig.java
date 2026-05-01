package net.kofllee.hoverhints.client.config;

import java.util.HashMap;
import java.util.Map;

public final class HoverHintsConfig {
    public boolean enabled = true;

    public HintRenderConfig renderConfig = new HintRenderConfig();

    public Map<String, ProviderConfig> providers = new HashMap<>();


    public ProviderConfig provider(String id) {
        return providers.computeIfAbsent(id, key -> new ProviderConfig());
    }
}

