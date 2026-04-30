package net.kofllee.hoverhints.client.hint;

import java.util.Optional;

public interface HintProvider {
    Optional<HintResult> getHint(HintContext hintContext);
}
