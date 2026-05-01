package net.kofllee.hoverhints.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public final class HoverHintsConfigScreen {

    public static Screen create(Screen parentScreen) {
        HoverHintsConfig config = HoverHintsConfigManager.getConfig();

        ConfigBuilder builder =  ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Text.literal("Hover Hints"));

        builder.setSavingRunnable(HoverHintsConfigManager::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Text.literal("Settings"));

        settings.addEntry(
                entryBuilder.startSubCategory(Text.literal("General"), List.of(
                        entryBuilder.startBooleanToggle(
                                        Text.literal("Enabled"),
                                        config.enabled
                                )
                                .setDefaultValue(true)
                                .setSaveConsumer(value -> config.enabled = value)
                                .build(),

                        entryBuilder.startEnumSelector(
                                        Text.literal("Activation Mode"),
                                        HintActivationMode.class,
                                        config.mode
                                )
                                .setDefaultValue(HintActivationMode.HOLD_KEY)
                                .setSaveConsumer(value -> config.mode = value)
                                .build()
                )
        ).build());

        settings.addEntry(
                entryBuilder.startSubCategory(Text.literal("Rendering"), List.of(
                        entryBuilder.startEnumSelector(
                                        Text.literal("Anchor"),
                                        HintAnchor.class,
                                        config.renderConfig.anchor
                                )
                                .setDefaultValue(HintAnchor.BELOW_CROSSHAIR)
                                .setSaveConsumer(value -> config.renderConfig.anchor = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Text.literal("Offset X"),
                                        config.renderConfig.offsetX
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetX = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Text.literal("Offset Y"),
                                        config.renderConfig.offsetY
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetY = value)
                                .build()
                )).build()
        );

        settings.addEntry(
                entryBuilder.startSubCategory(Text.literal("Hints"), List.of(
                        providerToggle(entryBuilder, config, "composter", "Composting"),
                        providerToggle(entryBuilder, config, "fuel", "Fuel Burn Time")
                )).build()
        );

        return builder.build();
    }

    private static me.shedaniel.clothconfig2.api.AbstractConfigListEntry<Boolean> providerToggle(
            ConfigEntryBuilder entryBuilder,
            HoverHintsConfig config,
            String id,
            String name) {
        ProviderConfig providerConfig = config.provider(id);

        return entryBuilder.startBooleanToggle(Text.literal(name), providerConfig.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> providerConfig.enabled = value)
                .build();

    }
}