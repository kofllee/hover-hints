package net.kofllee.hoverhints.client.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.kofllee.hoverhints.client.hint.HintProvider;
import net.kofllee.hoverhints.client.hint.HoverHintProviders;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class HoverHintsConfigScreen {

    public static Screen create(Screen parentScreen) {
        HoverHintsConfig config = HoverHintsConfigManager.getConfig();

        ConfigBuilder builder =  ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Component.translatable("config.hover_hints.title"));

        builder.setSavingRunnable(HoverHintsConfigManager::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Component.translatable("config.hover_hints.category.settings"));

        settings.addEntry(
                entryBuilder.startSubCategory(Component.translatable("config.hover_hints.section.general"), List.of(
                        entryBuilder.startBooleanToggle(
                                        Component.translatable("config.hover_hints.enabled"),
                                        config.enabled
                                )
                                .setDefaultValue(true)
                                .setSaveConsumer(value -> config.enabled = value)
                                .build(),

                        entryBuilder.startEnumSelector(
                                        Component.translatable("config.hover_hints.activation_mode"),
                                        HintActivationMode.class,
                                        config.mode
                                )
                                .setDefaultValue(HintActivationMode.HOLD_KEY)
                                .setEnumNameProvider(mode -> ((HintActivationMode) mode).asText())
                                .setSaveConsumer(value -> config.mode = value)
                                .build()
                )).setExpanded(true).build()
        );

        settings.addEntry(
                entryBuilder.startSubCategory(Component.translatable("config.hover_hints.section.rendering"), List.of(
                        entryBuilder.startEnumSelector(
                                        Component.translatable("config.hover_hints.anchor"),
                                        HintAnchor.class,
                                        config.renderConfig.anchor
                                )
                                .setDefaultValue(HintAnchor.BELOW_CROSSHAIR)
                                .setEnumNameProvider(HintAnchor::getDisplayName)
                                .setSaveConsumer(value -> config.renderConfig.anchor = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Component.translatable("config.hover_hints.offset_x"),
                                        config.renderConfig.offsetX
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetX = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Component.translatable("config.hover_hints.offset_y"),
                                        config.renderConfig.offsetY
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetY = value)
                                .build()
                )).setExpanded(true).build()
        );

        List<AbstractConfigListEntry> providerEntries = new ArrayList<>();

        for (HintProvider provider : HoverHintProviders.all()) {
            providerEntries.add(providerToggle(entryBuilder, config, provider));
        }

        settings.addEntry(
                entryBuilder.startSubCategory(
                        Component.translatable("config.hover_hints.section.hints"),
                        providerEntries
                ).setExpanded(true).build()
        );

        return builder.build();
    }

    private static AbstractConfigListEntry<Boolean> providerToggle(
            ConfigEntryBuilder entryBuilder,
            HoverHintsConfig config,
            HintProvider provider
    ) {
        ProviderConfig providerConfig = config.provider(provider.id());

        Component name = provider.configName();

        if (provider.requiresServer()) {
            name = name.copy().append(Component.translatable("config.hover_hints.server_only"));
        }

        return entryBuilder.startBooleanToggle(
                        name,
                        providerConfig.enabled
                )
                .setDefaultValue(true)
                .setTooltip(provider.configDescription())
                .setSaveConsumer(value -> providerConfig.enabled = value)
                .build();
    }
}