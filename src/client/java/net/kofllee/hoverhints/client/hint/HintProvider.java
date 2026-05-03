package net.kofllee.hoverhints.client.hint;

import java.util.List;


public interface HintProvider {
    String id();
    public void getHint(HintContext hintContext, List<HintResult> out);
}
