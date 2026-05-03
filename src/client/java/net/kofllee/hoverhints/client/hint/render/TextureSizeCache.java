package net.kofllee.hoverhints.client.hint.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TextureSizeCache {

    private static final Map<Identifier, Vec2f> CACHE = new HashMap<>();
    private static final Vec2f FALLBACK_SIZE = new Vec2f(8, 8);

    private TextureSizeCache() {}

    public static Vec2f getSize(MinecraftClient client, Identifier textureId) {
        return CACHE.computeIfAbsent(textureId, id -> readSize(client, id));
    }

    private static Vec2f readSize(MinecraftClient client, Identifier textureId) {
        try {
            Resource resource = client.getResourceManager()
                    .getResource(textureId)
                    .orElse(null);

            if (resource == null) {
                return FALLBACK_SIZE;
            }

            try (NativeImage image = NativeImage.read(resource.getInputStream())) {
                return new Vec2f(image.getWidth(), image.getHeight());
            }
        } catch (IOException e) {
            return FALLBACK_SIZE;
        }
    }
}