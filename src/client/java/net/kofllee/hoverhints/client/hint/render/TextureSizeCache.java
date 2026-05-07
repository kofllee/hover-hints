package net.kofllee.hoverhints.client.hint.render;

import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TextureSizeCache {

    private static final Map<Identifier, Vec2> CACHE = new HashMap<>();
    private static final Vec2 FALLBACK_SIZE = new Vec2(8, 8);

    private TextureSizeCache() {}

    public static Vec2 getSize(Minecraft client, Identifier textureId) {
        return CACHE.computeIfAbsent(textureId, id -> readSize(client, id));
    }

    private static Vec2 readSize(Minecraft client, Identifier textureId) {
        try {
            Resource resource = client.getResourceManager()
                    .getResource(textureId)
                    .orElse(null);

            if (resource == null) {
                return FALLBACK_SIZE;
            }

            try (NativeImage image = NativeImage.read(resource.open())) {
                return new Vec2(image.getWidth(), image.getHeight());
            }
        } catch (IOException e) {
            return FALLBACK_SIZE;
        }
    }
}