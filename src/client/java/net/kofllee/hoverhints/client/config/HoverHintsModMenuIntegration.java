package net.kofllee.hoverhints.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.util.NullScreenFactory;

public final class HoverHintsModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (!HoverHintsConfigDependencies.hasClothConfig()) {
            return new NullScreenFactory<>();
        }

        return HoverHintsConfigScreen::create;
    }
}