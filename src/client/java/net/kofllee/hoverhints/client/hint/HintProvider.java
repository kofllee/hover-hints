package net.kofllee.hoverhints.client.hint;

import java.util.Optional;

public interface HintProvider {
    String id();
    Optional<HintResult> getHint(HintContext hintContext);
}
