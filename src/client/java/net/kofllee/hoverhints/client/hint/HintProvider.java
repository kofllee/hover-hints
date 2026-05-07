package net.kofllee.hoverhints.client.hint;

import net.minecraft.network.chat.Component;

import java.util.List;


public interface HintProvider {
    String id();

    boolean requiresServer();

    public void getHint(HintContext hintContext, List<HintResult> out);

    default Component configName() {
        return Component.translatable("config.hover_hints.provider." + id());
    }

    default Component configDescription() {
        return Component.translatable("config.hover_hints.provider." + id() + ".desc");
    }
}
