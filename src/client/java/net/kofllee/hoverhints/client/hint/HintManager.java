package net.kofllee.hoverhints.client.hint;

import net.kofllee.hoverhints.client.hint.provider.ComposterHintProvider;
import net.kofllee.hoverhints.client.hint.provider.FuelHintProvider;

import java.util.List;
import java.util.Optional;

public final class HintManager {

    private final List<HintProvider> hintProviders = List.of(
            new ComposterHintProvider(),
            new FuelHintProvider()
    );

    public Optional<HintResult> resolve(HintContext hintContext) {
        for(HintProvider hintProvider : hintProviders) {
            Optional<HintResult> hintResult = hintProvider.getHint(hintContext);

            if(hintResult.isPresent()) {
                return hintResult;
            }
        }

        return Optional.empty();
    }
}
