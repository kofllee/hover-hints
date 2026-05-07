package net.kofllee.hoverhints.client.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.Identifier;

public final class HoverHintBlockTags {
    public static final TagKey<Block> SILK_TOUCH_RELEVANT =
            TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("hover_hints", "silk_touch_relevant"));

    private HoverHintBlockTags() {}
}
