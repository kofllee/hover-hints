package net.kofllee.hoverhints.client.hint.util;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class ItemTextureResolver {

    private ItemTextureResolver() {}

    public static Identifier getTexture(Item item) {
        Identifier id = Registries.ITEM.getId(item);

        return Identifier.of(
                id.getNamespace(),
                "textures/item/" + id.getPath() + ".png"
        );
    }
}