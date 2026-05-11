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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

import java.util.ArrayList;
import java.util.List;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private static final int TOOLTIP_PADDING = 4;
    private static final int LINE_GAP = 1;

    private static final int ICON_GAP = 4;

    public static final int COLUMN_GAP = 4;
    

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

        List<HintResult> visibleResults = limitResults(results, config);
        List<List<HintResult>> columns = buildColumns(visibleResults, config);

        if (columns.isEmpty()) {
            return;
        }

        int contentWidth = 0;
        int contentHeight = 0;

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            List<HintResult> column = columns.get(columnIndex);

            contentWidth += getColumnWidth(client, column);

            if (columnIndex < columns.size() - 1) {
                contentWidth += COLUMN_GAP;
            }
        }

        int rowCount = getMaxColumnSize(columns);

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            contentHeight += getGridRowHeight(client, columns, rowIndex);

            if (rowIndex < rowCount - 1) {
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

        int x = contentX;
        rowCount = getMaxColumnSize(columns);

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            List<HintResult> column = columns.get(columnIndex);

            int y = contentY;
            int columnWidth = getColumnWidth(client, column);

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                int rowHeight = getGridRowHeight(client, columns, rowIndex);

                if (rowIndex < column.size()) {
                    HintResult result = column.get(rowIndex);

                    drawHintResult(
                            drawContext,
                            client,
                            result,
                            x,
                            y,
                            rowHeight
                    );
                }

                y += rowHeight;

                if (rowIndex < rowCount - 1) {
                    y += LINE_GAP;
                }
            }

            x += columnWidth;

            if (columnIndex < columns.size() - 1) {
                x += COLUMN_GAP;
            }
        }

        drawContext.getMatrices().pop();
    }

    private static int getMaxColumnSize(List<List<HintResult>> columns) {
        int max = 0;

        for (List<HintResult> column : columns) {
            max = Math.max(max, column.size());
        }

        return max;
    }

    private static int getGridRowHeight(
            MinecraftClient client,
            List<List<HintResult>> columns,
            int rowIndex
    ) {
        int height = 0;

        for (List<HintResult> column : columns) {
            if (rowIndex >= column.size()) {
                continue;
            }

            height = Math.max(height, getItemHeight(client, column.get(rowIndex)));
        }

        return height;
    }

    private static List<HintResult> limitResults(List<HintResult> results, HintRenderConfig config) {
        int maxRows = Math.max(1, config.maxHintRows);
        int maxColumns = Math.max(1, config.maxHintColumns);
        int maxResults = maxRows * maxColumns;

        if (results.size() <= maxResults) {
            return results;
        }

        int hiddenCount = results.size() - maxResults + 1;
        int visibleCount = maxResults - 1;

        List<HintResult> limited = new ArrayList<>();

        for (int i = 0; i < visibleCount; i++) {
            limited.add(results.get(i));
        }

        limited.add(new HintResult(
                (Identifier) null,
                Text.translatable("hint.hover_hints.more", hiddenCount)
                        .styled(style -> style.withColor(0xAAAAAA))
        ));

        return limited;
    }

    private static List<List<HintResult>> buildColumns(List<HintResult> results, HintRenderConfig config) {
        int maxRows = Math.max(1, config.maxHintRows);
        int maxColumns = Math.max(1, config.maxHintColumns);
        int maxResults = maxRows * maxColumns;

        int visibleResults = Math.min(results.size(), maxResults);

        List<List<HintResult>> columns = new ArrayList<>();

        for (int i = 0; i < visibleResults; i++) {
            if (i % maxRows == 0) {
                columns.add(new ArrayList<>());
            }

            columns.get(columns.size() - 1).add(results.get(i));
        }

        return columns;
    }

    private static int getColumnWidth(MinecraftClient client, List<HintResult> column) {
        int width = 0;

        for (HintResult result : column) {
            width = Math.max(width, getItemWidth(client, result));
        }

        return width;
    }

    private static int getColumnHeight(MinecraftClient client, List<HintResult> column) {
        int height = 0;

        for (int i = 0; i < column.size(); i++) {
            height += getItemHeight(client, column.get(i));

            if (i < column.size() - 1) {
                height += LINE_GAP;
            }
        }

        return height;
    }

    private static int getItemWidth(MinecraftClient client, HintResult result) {
        int textWidth = client.textRenderer.getWidth(result.text());

        boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

        if (!hasIcon) {
            return textWidth;
        }

        return getIconWidth(client, result) + ICON_GAP + textWidth;
    }

    private static int getItemHeight(MinecraftClient client, HintResult result) {
        int textHeight = client.textRenderer.fontHeight;
        int iconHeight = getIconHeight(client, result);

        return Math.max(textHeight, iconHeight);
    }

    private static int getIconWidth(MinecraftClient client, HintResult result) {
        if (result.iconStack() != null) {
            return 16;
        }

        if (result.iconTexture() != null) {
            return (int) TextureSizeCache.getSize(client, result.iconTexture()).x;
        }

        return 0;
    }

    private static int getIconHeight(MinecraftClient client, HintResult result) {
        if (result.iconStack() != null) {
            return 16;
        }

        if (result.iconTexture() != null) {
            return (int) TextureSizeCache.getSize(client, result.iconTexture()).y;
        }

        return 0;
    }

    private static void drawHintResult(
            DrawContext drawContext,
            MinecraftClient client,
            HintResult result,
            int x,
            int y,
            int rowHeight
    ) {
        int textHeight = client.textRenderer.fontHeight;

        boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

        int iconWidth = getIconWidth(client, result);
        int iconHeight = getIconHeight(client, result);

        int textX = x;

        if (hasIcon) {
            int iconY = y + (rowHeight - iconHeight) / 2;

            if (result.iconStack() != null) {
                drawContext.drawItem(result.iconStack(), x, iconY);
            } else {
                drawContext.drawTexture(
                        result.iconTexture(),
                        x,
                        iconY,
                        0,
                        0,
                        iconWidth,
                        iconHeight,
                        iconWidth,
                        iconHeight
                );
            }

            textX += iconWidth + ICON_GAP;
        }

        int textY = y + Math.round((rowHeight - textHeight) / 2f);

        drawContext.drawText(
                client.textRenderer,
                result.text(),
                textX,
                textY,
                0xFFFFFFFF,
                true
        );
    }
}
