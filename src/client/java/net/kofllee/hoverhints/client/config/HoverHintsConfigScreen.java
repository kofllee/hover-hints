package net.kofllee.hoverhints.client.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.kofllee.hoverhints.client.hint.HoverHintProviders;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public final class HoverHintsConfigScreen {

    public static Screen create(Screen parentScreen) {
        HoverHintsConfig config = HoverHintsConfigManager.getConfig();

        ConfigBuilder builder =  ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Text.translatable("config.hover_hints.title"));

        builder.setSavingRunnable(HoverHintsConfigManager::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory settings = builder.getOrCreateCategory(Text.translatable("config.hover_hints.category.settings"));

        settings.addEntry(
                entryBuilder.startSubCategory(Text.translatable("config.hover_hints.section.general"), List.of(
                        entryBuilder.startBooleanToggle(
                                        Text.translatable("config.hover_hints.enabled"),
                                        config.enabled
                                )
                                .setDefaultValue(true)
                                .setSaveConsumer(value -> config.enabled = value)
                                .build(),

                        entryBuilder.startEnumSelector(
                                        Text.translatable("config.hover_hints.activation_mode"),
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
                entryBuilder.startSubCategory(Text.translatable("config.hover_hints.section.rendering"), List.of(
                        entryBuilder.startEnumSelector(
                                        Text.translatable("config.hover_hints.anchor"),
                                        HintAnchor.class,
                                        config.renderConfig.anchor
                                )
                                .setDefaultValue(HintAnchor.BELOW_CROSSHAIR)
                                .setSaveConsumer(value -> config.renderConfig.anchor = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Text.translatable("config.hover_hints.offset_x"),
                                        config.renderConfig.offsetX
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetX = value)
                                .build(),

                        entryBuilder.startIntField(
                                        Text.translatable("config.hover_hints.offset_y"),
                                        config.renderConfig.offsetY
                                )
                                .setDefaultValue(0)
                                .setSaveConsumer(value -> config.renderConfig.offsetY = value)
                                .build()
                )).setExpanded(true).build()
        );

        List<AbstractConfigListEntry> providerEntries = HoverHintProviders.all().stream()
                .map(provider -> (AbstractConfigListEntry) providerToggle(
                        entryBuilder,
                        config,
                        provider.id(),
                        Text.translatable("config.hover_hints.provider." + provider.id())
                ))
                .toList();

        settings.addEntry(
                entryBuilder.startSubCategory(
                        Text.translatable("config.hover_hints.section.hints"),
                        providerEntries
                ).setExpanded(true).build()
        );

        return builder.build();
    }

    private static me.shedaniel.clothconfig2.api.AbstractConfigListEntry<Boolean> providerToggle(
            ConfigEntryBuilder entryBuilder,
            HoverHintsConfig config,
            String id,
            Text name) {
        ProviderConfig providerConfig = config.provider(id);

        return entryBuilder.startBooleanToggle(name, providerConfig.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(value -> providerConfig.enabled = value)
                .build();

    }
}