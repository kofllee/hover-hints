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

import java.util.List;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private static final int TOOLTIP_PADDING = 4;
    private static final int LINE_GAP = 0;

    private static final int ICON_GAP = 4;
    

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
        List<HintResult> results = HINT_MANAGER.resolve(hintContext);

        if(results.isEmpty()) {
            return;
        }

        drawHints(drawContext, client, results);
    }

    private static void drawHints(DrawContext drawContext, MinecraftClient client, List<HintResult> results) {
        HintRenderConfig config = HoverHintsConfigManager.getConfig().renderConfig;

        int contentWidth = 0;
        int contentHeight = 0;

        for(int i = 0; i < results.size(); i++) {
            HintResult result = results.get(i);

            int textWidth = client.textRenderer.getWidth(result.text());
            int textHeight = client.textRenderer.fontHeight;

            boolean hasIcon = result.icon() != null;

            Vec2f iconSize = hasIcon ? TextureSizeCache.getSize(client, result.icon()) : Vec2f.ZERO;
            int iconWidth = (int) iconSize.x;
            int iconHeight= (int) iconSize.y;
            int iconSpace = hasIcon ? iconWidth + ICON_GAP : 0;

            int lineWidth = iconSpace + textWidth;
            int lineHeight = Math.max(iconHeight, textHeight);

            contentWidth = Math.max(contentWidth, lineWidth);
            contentHeight += lineHeight;

            if(i < results.size() - 1) {
                contentHeight += LINE_GAP;
            }
        }

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

        int y = contentY;

        for(int i = 0; i < results.size(); i++) {
            HintResult result = results.get(i);

            int textHeight = client.textRenderer.fontHeight;

            boolean hasIcon = result.icon() != null;

            Vec2f iconSize = hasIcon ? TextureSizeCache.getSize(client, result.icon()) : Vec2f.ZERO;
            int iconWidth = (int) iconSize.x;
            int iconHeight= (int) iconSize.y;
            int lineHeight = Math.max(textHeight, iconHeight);

            int x = contentX;
            int textX = x;

            if(hasIcon) {
                int iconY = y + (lineHeight - iconHeight) / 2;

                drawContext.drawTexture(
                        result.icon(),
                        x,
                        iconY,
                        0,
                        0,
                        iconWidth,
                        iconHeight,
                        iconWidth,
                        iconHeight
                );

                textX += iconWidth + ICON_GAP;
            }

            int textY = y + Math.round((lineHeight - textHeight) / 2f);

            drawContext.drawText(
                    client.textRenderer,
                    result.text(),
                    textX,
                    textY,
                    0xFFFFFFFF,
                    true
            );

            y +=  lineHeight;

             if(i < results.size() - 1) {
                y += LINE_GAP;
            }
        }

        drawContext.getMatrices().pop();
    }
}
