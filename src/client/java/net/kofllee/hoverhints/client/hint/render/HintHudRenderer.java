package net.kofllee.hoverhints.client.hint.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.kofllee.hoverhints.client.config.HintRenderConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.HintActivationController;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintManager;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private static final int TOOLTIP_PADDING = 4;
    private static final int LINE_GAP = 1;

    private static final int ICON_GAP = 4;
    

    private HintHudRenderer(){}

    public static void register(){
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath("hover_hints", "hints"),
                HintHudRenderer::render
        );
    }

    private static void render(GuiGraphicsExtractor drawContext, DeltaTracker renderTickCounter) {

        Minecraft client = Minecraft.getInstance();

        if(client.player == null || client.level == null) {
            return;
        }

        if(client.screen != null) {
            return;
        }

        if(client.hitResult == null) {
            return;
        }

        if(!HintActivationController.shouldShowHints()) {
            return;
        }

        HintContext hintContext = new HintContext(
                client,
                client.player,
                client.level,
                client.hitResult,
                client.player.getMainHandItem());
        List<HintResult> results = HINT_MANAGER.resolve(hintContext);

        if(results.isEmpty()) {
            return;
        }

        drawHints(drawContext, client, results);
    }

    private static void drawHints(GuiGraphicsExtractor drawContext, Minecraft client, List<HintResult> results) {
        HintRenderConfig config = HoverHintsConfigManager.getConfig().renderConfig;

        int contentWidth = 0;
        int contentHeight = 0;

        for(int i = 0; i < results.size(); i++) {
            HintResult result = results.get(i);

            int textWidth = client.font.width(result.text());
            int textHeight = client.font.lineHeight;

            boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

            Vec2 iconSize = result.iconTexture() != null ? TextureSizeCache.getSize(client, result.iconTexture()) : Vec2.ZERO;
            iconSize = result.iconStack() != null ? new Vec2(16, 16) : iconSize;

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

        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();

        Vec2 position = HintAnchorResolver.resolveAnchor(
                config.anchor,
                client,
                screenWidth,
                screenHeight,
                hintWidth,
                hintHeight
        );

        int contentX = (int) position.x + config.offsetX + TOOLTIP_PADDING;
        int contentY = (int) position.y + config.offsetY + TOOLTIP_PADDING;

        drawContext.pose().pushMatrix();

        TooltipRenderUtil.extractTooltipBackground(
                drawContext,
                contentX,
                contentY,
                contentWidth,
                contentHeight,
                null
        );

        int y = contentY;

        for(int i = 0; i < results.size(); i++) {
            HintResult result = results.get(i);

            int textHeight = client.font.lineHeight;

            boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

            Vec2 iconSize = result.iconTexture() != null ? TextureSizeCache.getSize(client, result.iconTexture()) : Vec2.ZERO;
            iconSize = result.iconStack() != null ? new Vec2(16, 16) : iconSize;

            int iconWidth = (int) iconSize.x;
            int iconHeight= (int) iconSize.y;
            int lineHeight = Math.max(textHeight, iconHeight);

            int x = contentX;
            int textX = x;

            if(hasIcon) {
                int iconY = y + (lineHeight - iconHeight) / 2;

                if (result.iconStack() != null) {
                    drawContext.item(result.iconStack(), x, iconY);
                } else {
                    drawContext.blit(
                            RenderPipelines.GUI_TEXTURED,
                            result.iconTexture(),
                            x,
                            iconY,
                            0.0F,
                            0.0F,
                            iconWidth,
                            iconHeight,
                            iconWidth,
                            iconHeight
                    );
                }

                textX += iconWidth + ICON_GAP;
            }

            int textY = y + Math.round((lineHeight - textHeight) / 2f);

            drawContext.text(
                    client.font,
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

        drawContext.pose().popMatrix();
    }
}
