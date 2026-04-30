package net.kofllee.hoverhints.client.hint.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kofllee.hoverhints.client.HoverHintClient;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintManager;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.input.HintKeybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.Optional;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private HintHudRenderer(){}

    public static void register(){
        HudRenderCallback.EVENT.register(HintHudRenderer::render);
    }

    private static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {

        MinecraftClient client = MinecraftClient.getInstance();

        if(client.player == null || client.world == null) {
            return;
        }

        if(client.currentScreen != null) {
            return;
        }

        if(client.crosshairTarget == null) {
            return;
        }

        long windowHandle = client.getWindow().getHandle();

        if(!HintKeybinds.isHintModeActive(windowHandle)) {
            return;
        }

        HintContext hintContext = new HintContext(
                client,
                client.player,
                client.world,
                client.crosshairTarget,
                client.player.getMainHandStack());
        Optional<HintResult> result = HINT_MANAGER.resolve(hintContext);

        if(result.isEmpty()) {
            return;
        }

        drawHint(drawContext, client, result.get());
    }

    private static void drawHint(DrawContext drawContext, MinecraftClient client, HintResult hintResult) {
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int tooltipWidth = client.textRenderer.getWidth(hintResult.text()) + 6;
        int tooltipHeight = client.textRenderer.fontHeight + 4;

        int x = (screenWidth - tooltipWidth) / 2;
        int y = screenHeight / 2 + tooltipHeight + 16;

        drawContext.drawTooltip(client.textRenderer, hintResult.text(), x, y);
    }
}
