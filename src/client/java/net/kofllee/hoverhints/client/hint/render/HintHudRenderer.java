package net.kofllee.hoverhints.client.hint.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.kofllee.hoverhints.client.config.HintRenderConfig;
import net.kofllee.hoverhints.client.config.HoverHintsConfigManager;
import net.kofllee.hoverhints.client.hint.HintActivationController;
import net.kofllee.hoverhints.client.hint.HintContext;
import net.kofllee.hoverhints.client.hint.HintManager;
import net.kofllee.hoverhints.client.hint.HintResult;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public final class HintHudRenderer {

    private static final HintManager HINT_MANAGER = new HintManager();

    private static final int TOOLTIP_PADDING = 4;
    private static final int LINE_GAP = 1;
    private static final int ICON_GAP = 4;

    public static final int COLUMN_GAP = 4;

    private HintHudRenderer() {}

    public static void register() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("hover_hints", "hints"),
                HintHudRenderer::render);
    }

    private static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        if (minecraft.screen != null) {
            return;
        }

        if (minecraft.hitResult == null) {
            return;
        }

        if (!HintActivationController.shouldShowHints()) {
            return;
        }

        HintContext hintContext = new HintContext(
                minecraft,
                minecraft.player,
                minecraft.level,
                minecraft.hitResult,
                minecraft.player.getMainHandItem()
        );

        List<HintResult> results = HINT_MANAGER.resolve(hintContext);

        if (results.isEmpty()) {
            return;
        }

        drawHints(graphics, minecraft, results);
    }

    private static void drawHints(
            GuiGraphics graphics,
            Minecraft minecraft,
            List<HintResult> results
    ) {
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

            contentWidth += getColumnWidth(minecraft, column);

            if (columnIndex < columns.size() - 1) {
                contentWidth += COLUMN_GAP;
            }
        }

        int rowCount = getMaxColumnSize(columns);

        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            contentHeight += getGridRowHeight(minecraft, columns, rowIndex);

            if (rowIndex < rowCount - 1) {
                contentHeight += LINE_GAP;
            }
        }

        int hintWidth = contentWidth + TOOLTIP_PADDING * 2;
        int hintHeight = contentHeight + TOOLTIP_PADDING * 2;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        Vec2 position = HintAnchorResolver.resolveAnchor(
                config.anchor,
                minecraft,
                screenWidth,
                screenHeight,
                hintWidth,
                hintHeight
        );

        int contentX = (int) position.x + config.offsetX + TOOLTIP_PADDING;
        int contentY = (int) position.y + config.offsetY + TOOLTIP_PADDING;

        TooltipRenderUtil.renderTooltipBackground(
                graphics,
                contentX,
                contentY,
                contentWidth,
                contentHeight,
                null
        );

        int x = contentX;
        rowCount = getMaxColumnSize(columns);

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            List<HintResult> column = columns.get(columnIndex);

            int y = contentY;
            int columnWidth = getColumnWidth(minecraft, column);

            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                int rowHeight = getGridRowHeight(minecraft, columns, rowIndex);

                if (rowIndex < column.size()) {
                    HintResult result = column.get(rowIndex);

                    drawHintResult(
                            graphics,
                            minecraft,
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
    }

    private static int getMaxColumnSize(List<List<HintResult>> columns) {
        int max = 0;

        for (List<HintResult> column : columns) {
            max = Math.max(max, column.size());
        }

        return max;
    }

    private static int getGridRowHeight(
            Minecraft minecraft,
            List<List<HintResult>> columns,
            int rowIndex
    ) {
        int height = 0;

        for (List<HintResult> column : columns) {
            if (rowIndex >= column.size()) {
                continue;
            }

            height = Math.max(height, getItemHeight(minecraft, column.get(rowIndex)));
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
                Component.translatable("hint.hover_hints.more", hiddenCount)
                        .withStyle(style -> style.withColor(0xAAAAAA))
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

            columns.getLast().add(results.get(i));
        }

        return columns;
    }

    private static int getColumnWidth(Minecraft minecraft, List<HintResult> column) {
        int width = 0;

        for (HintResult result : column) {
            width = Math.max(width, getItemWidth(minecraft, result));
        }

        return width;
    }

    private static int getItemWidth(Minecraft minecraft, HintResult result) {
        int textWidth = minecraft.font.width(result.text());

        boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

        if (!hasIcon) {
            return textWidth;
        }

        return getIconWidth(minecraft, result) + ICON_GAP + textWidth;
    }

    private static int getItemHeight(Minecraft minecraft, HintResult result) {
        int textHeight = minecraft.font.lineHeight;
        int iconHeight = getIconHeight(minecraft, result);

        return Math.max(textHeight, iconHeight);
    }

    private static int getIconWidth(Minecraft minecraft, HintResult result) {
        if (result.iconStack() != null) {
            return 16;
        }

        if (result.iconTexture() != null) {
            return (int) TextureSizeCache.getSize(minecraft, result.iconTexture()).x;
        }

        return 0;
    }

    private static int getIconHeight(Minecraft minecraft, HintResult result) {
        if (result.iconStack() != null) {
            return 16;
        }

        if (result.iconTexture() != null) {
            return (int) TextureSizeCache.getSize(minecraft, result.iconTexture()).y;
        }

        return 0;
    }

    private static void drawHintResult(
            GuiGraphics graphics,
            Minecraft minecraft,
            HintResult result,
            int x,
            int y,
            int rowHeight
    ) {
        int textHeight = minecraft.font.lineHeight;

        boolean hasIcon = result.iconTexture() != null || result.iconStack() != null;

        int iconWidth = getIconWidth(minecraft, result);
        int iconHeight = getIconHeight(minecraft, result);

        int textX = x;

        if (hasIcon) {
            int iconY = y + (rowHeight - iconHeight) / 2;

            if (result.iconStack() != null) {
                graphics.renderItem(result.iconStack(), x, iconY);
            } else {
                graphics.blit(
                        RenderPipelines.GUI_TEXTURED,
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

        int textY = y + Math.round((rowHeight - textHeight) / 2.0F);

        graphics.drawString(
                minecraft.font,
                result.text(),
                textX,
                textY,
                0xFFFFFFFF,
                true
        );
    }
}