package net.kofllee.hoverhints.client.hint;

import net.minecraft.text.Text;

import java.util.List;


public interface HintProvider {
    String id();

    boolean requiresServer();

    public void getHint(HintContext hintContext, List<HintResult> out);

    default Text configName() {
        return Text.translatable("config.hover_hints.provider." + id());
    }

    default Text configDescription() {
        return Text.translatable("config.hover_hints.provider." + id() + ".desc");
    }
}
