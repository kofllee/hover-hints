package net.kofllee.hoverhints.client.hint;

import net.kofllee.hoverhints.client.config.HoverHintsConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.provider.*;

import java.util.ArrayList;
import java.util.List;

public final class HintManager {

    private final List<HintProvider> hintProviders = List.of(
            new ComposterHintProvider(),
            new FuelHintProvider(),
            new GrindStoneProvider(),
            new BoneMealHintProvider(),
            new TameableAnimalHintProvider(),
            new AnimalFeedHintProvider()
    );

    public List<HintResult> resolve(HintContext hintContext) {
        HoverHintsConfig config = HoverHintsConfigManager.getConfig();

        if(!config.enabled) {
            return List.of();
        }

        List<HintResult> results = new ArrayList<>();

        for(HintProvider hintProvider : hintProviders) {
            if(!config.provider(hintProvider.id()).enabled) {
                continue;
            }

            hintProvider.getHint(hintContext, results);
        }

        return results;
    }
}
