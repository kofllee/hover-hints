package net.kofllee.hoverhints.client.hint.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kofllee.hoverhints.client.config.HintRenderConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintManager;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.kofllee.hoverhints.client.hint.input.HintKeybinds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.Vec2f;

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
        HintRenderConfig config = HoverHintsConfigManager.getConfig().renderConfig;

        int tooltipPadding = 4;

        int textWidth = client.textRenderer.getWidth(hintResult.text());
        int textHeight = client.textRenderer.fontHeight;

        int hintWidth = textWidth + tooltipPadding * 2;
        int hintHeight = textHeight + tooltipPadding * 2;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        Vec2f position = HintAnchorResolver.resolveAnchor(
                config.anchor,
                screenWidth,
                screenHeight,
                hintWidth,
                hintHeight
        );

        int x = (int) position.x + config.anchor.defaultOffsetX + config.offsetX + tooltipPadding;
        int y = (int) position.y + config.anchor.defaultOffsetY + config.offsetY + tooltipPadding;

        drawContext.getMatrices().push();

        TooltipBackgroundRenderer.render(
                drawContext,
                x,
                y,
                textWidth,
                textHeight,
                400
        );

        drawContext.getMatrices().translate(0.0F, 0.0F, 400.0F);

        drawContext.drawText(
                client.textRenderer,
                hintResult.text(),
                x,
                y,
                0xFFFFFFFF,
                false
        );

        drawContext.getMatrices().pop();
    }
}
