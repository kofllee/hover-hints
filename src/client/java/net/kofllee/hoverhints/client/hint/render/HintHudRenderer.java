package net.kofllee.hoverhints.client.hint.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kofllee.hoverhints.client.config.HintRenderConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.HintActivationController;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintManager;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.Vec2f;

import java.util.Optional;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private static final int TOOLTIP_PADDING = 4;

    private static final int ICON_SIZE = 9;
    private static final int ICON_GAP = 4;

    private static final int ICON_OFFSET_X = 1;
    private static final int ICON_OFFSET_Y = 0;

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

        if(!HintActivationController.shouldShowHints()) {
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

        boolean hasIcon = hintResult.icon() != null;

        int textWidth = client.textRenderer.getWidth(hintResult.text());
        int textHeight = client.textRenderer.fontHeight;

        int iconSpace = hasIcon ? ICON_SIZE + ICON_GAP : 0;

        int contentWidth = iconSpace + textWidth;
        int contentHeight = textHeight;

        int hintWidth = contentWidth + TOOLTIP_PADDING * 2;
        int hintHeight = contentHeight + TOOLTIP_PADDING * 2;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        Vec2f position = HintAnchorResolver.resolveAnchor(
                config.anchor,
                client,
                screenWidth,
                screenHeight,
                hintWidth,
                hintHeight
        );

        int contentX = (int) position.x + config.offsetX + TOOLTIP_PADDING;
        int contentY = (int) position.y + config.offsetY + TOOLTIP_PADDING;

        drawContext.getMatrices().push();

        TooltipBackgroundRenderer.render(
                drawContext,
                contentX,
                contentY,
                contentWidth,
                contentHeight,
                400
        );

        drawContext.getMatrices().translate(0.0F, 0.0F, 400.0F);

        int textX = contentX;

        if(hasIcon) {
            int iconX = contentX + ICON_OFFSET_X;
            int iconY = contentY + ICON_OFFSET_Y;

            drawContext.drawTexture(
                    hintResult.icon(),
                    iconX,
                    iconY,
                    0,
                    0,
                    ICON_SIZE,
                    ICON_SIZE,
                    ICON_SIZE,
                    ICON_SIZE
            );

            textX += iconSpace;
        }

        drawContext.drawText(
                client.textRenderer,
                hintResult.text(),
                textX,
                contentY,
                0xFFFFFFFF,
                true
        );

        drawContext.getMatrices().pop();
    }
}
