package net.kofllee.hoverhints.client.registry;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class HoverHintBlockTags {
    public static final TagKey<Block> SILK_TOUCH_RELEVANT =
            TagKey.of(RegistryKeys.BLOCK, Identifier.of("hover_hints", "silk_touch_relevant"));

    private HoverHintBlockTags() {}
}
