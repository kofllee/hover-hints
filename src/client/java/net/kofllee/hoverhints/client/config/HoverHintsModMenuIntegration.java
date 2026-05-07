package net.kofllee.hoverhints.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public final class HoverHintsModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!HoverHintsConfigDependencies.hasClothConfig()) {
            return null;
        }

        return HoverHintsConfigScreen::create;
    }
}