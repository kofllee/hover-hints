package net.kofllee.hoverhints.client.hint;

import java.util.ArrayList;
import java.util.List;

public final class HoverHintProviders {
    private static final List<HintProvider> PROVIDERS = new ArrayList<>();

    private HoverHintProviders() {}

    public static void register(HintProvider provider) {
        PROVIDERS.add(provider);
    }

    public static List<HintProvider> all() {
        return List.copyOf(PROVIDERS);
    }
}
