package net.jackylang2012.tool_update.config;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ToolUpdateConfigScreen extends Screen {
    private final Screen parent;
    private ScrollPanel scrollPanel;
    private static final Logger LOGGER = LogUtils.getLogger();

    // 配置字段
    private EditBox baseCostField;
    private EditBox maxLevelBonusField;
    private Button enableEffectsButton;
    private Button blacklistResetButton;
    private EditBox effectVolumeField;
    private EditBox maxEnchantLevelField;
    private Button showTooltipButton;
    private EditBox blacklistField;
    private EditBox swordProficiencyField;
    private EditBox pickaxeProficiencyField;
    private EditBox axeProficiencyField;
    private EditBox shovelProficiencyField;
    private EditBox hoeProficiencyField;
    private EditBox tridentProficiencyField;
    private EditBox fishingRodProficiencyField;
    private EditBox bowProficiencyField;
    private EditBox crossbowProficiencyField;
    private EditBox flintAndSteelProficiencyField;
    private EditBox shearsProficiencyField;
    private EditBox shieldProficiencyField;
    private EditBox brushProficiencyField;

    // 布局常量
    private static final int FIELD_WIDTH = 200;
    private static final int LABEL_WIDTH = 120;
    private static final int SPACING = 25;
    private static final int BUTTON_WIDTH = 150;
    private static final int SCROLL_BAR_WIDTH = 6;
    private static final int TOP_PADDING = 30;
    private static final int BOTTOM_PADDING = 60;
    private static final int EDGE_FADE_HEIGHT = 20;
    private static final int EDGE_PADDING = 5;
    private static final int RESET_BUTTON_SIZE = 20;

    public ToolUpdateConfigScreen(Screen parent) {
        super(Component.literal("工具升级配置"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int left = this.width / 2 - (FIELD_WIDTH + LABEL_WIDTH + 10 + RESET_BUTTON_SIZE) / 2;
        initializeFieldsAndButtons();
        setInitialValues();

        scrollPanel = new ScrollPanel(
                this.minecraft,
                this.width,
                this.height,
                TOP_PADDING,
                this.height - BOTTOM_PADDING,
                left
        );
        this.addRenderableWidget(scrollPanel);
        setupBottomButtons();
    }

    private void initializeFieldsAndButtons() {
        baseCostField = createStyledEditBox();
        maxLevelBonusField = createStyledEditBox();
        effectVolumeField = createStyledEditBox();
        maxEnchantLevelField = createStyledEditBox();
        blacklistField = createStyledEditBox();

        blacklistResetButton = Button.builder(Component.literal("↺"), button -> {
                    List<String> defaultList = ToolUpdateConfig.getDefaultBlacklistedEnchants();
                    blacklistField.setValue(String.join(", ", defaultList));
                })
                .size(RESET_BUTTON_SIZE, RESET_BUTTON_SIZE)
                .build();

        swordProficiencyField = createStyledEditBox();
        pickaxeProficiencyField = createStyledEditBox();
        axeProficiencyField = createStyledEditBox();
        shovelProficiencyField = createStyledEditBox();
        hoeProficiencyField = createStyledEditBox();
        tridentProficiencyField = createStyledEditBox();
        fishingRodProficiencyField = createStyledEditBox();
        bowProficiencyField = createStyledEditBox();
        crossbowProficiencyField = createStyledEditBox();
        flintAndSteelProficiencyField = createStyledEditBox();
        shearsProficiencyField = createStyledEditBox();
        shieldProficiencyField = createStyledEditBox();
        brushProficiencyField = createStyledEditBox();

        enableEffectsButton = createStyledButton("升级特效: ",
                () -> ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get());
        showTooltipButton = createStyledButton("显示熟练度提示: ",
                () -> ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get());
    }

    private EditBox createStyledEditBox() {
        EditBox box = new EditBox(this.font, 0, 0, FIELD_WIDTH, 20, Component.empty()) {
            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
                guiGraphics.fill(this.getX(), this.getY(),
                        this.getX() + this.width, this.getY() + this.height,
                        0x66000000);
                super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
            }
        };
        box.setMaxLength(Integer.MAX_VALUE); // 移除字符限制
        box.setValue(""); // 清空初始值
        return box;
    }

    private Button createStyledButton(String prefix, java.util.function.BooleanSupplier getter) {
        return Button.builder(Component.empty(), button -> {
                    boolean newValue = !getter.getAsBoolean();
                    if (prefix.contains("升级特效")) {
                        ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.set(newValue);
                    } else {
                        ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.set(newValue);
                    }
                    button.setMessage(Component.literal(prefix + (newValue ? "✔" : "✘")));
                })
                .size(FIELD_WIDTH, 20)
                .build();
    }

    private void setInitialValues() {
        baseCostField.setValue(String.valueOf(ToolUpdateConfig.BASE_COST.get()));
        maxLevelBonusField.setValue(String.valueOf(ToolUpdateConfig.MAX_LEVEL_BONUS.get()));
        effectVolumeField.setValue(String.valueOf(ToolUpdateConfig.EFFECT_VOLUME.get()));
        maxEnchantLevelField.setValue(String.valueOf(ToolUpdateConfig.MAX_ENCHANT_LEVEL.get()));
        maxLevelBonusField.setValue(String.valueOf(ToolUpdateConfig.MAX_LEVEL_BONUS.get()));
        blacklistField.setValue(
                ToolUpdateConfig.BLACKLISTED_ENCHANTS.get().stream()
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(", "))
        );

        swordProficiencyField.setValue(String.valueOf(ToolUpdateConfig.SWORD_PROFICIENCY_PER_USE.get()));
        pickaxeProficiencyField.setValue(String.valueOf(ToolUpdateConfig.PICKAXE_PROFICIENCY_PER_USE.get()));
        axeProficiencyField.setValue(String.valueOf(ToolUpdateConfig.AXE_PROFICIENCY_PER_USE.get()));
        shovelProficiencyField.setValue(String.valueOf(ToolUpdateConfig.SHOVEL_PROFICIENCY_PER_USE.get()));
        hoeProficiencyField.setValue(String.valueOf(ToolUpdateConfig.HOE_PROFICIENCY_PER_USE.get()));
        tridentProficiencyField.setValue(String.valueOf(ToolUpdateConfig.TRIDENT_PROFICIENCY_PER_USE.get()));
        fishingRodProficiencyField.setValue(String.valueOf(ToolUpdateConfig.FISHING_ROD_PROFICIENCY_PER_USE.get()));
        bowProficiencyField.setValue(String.valueOf(ToolUpdateConfig.BOW_PROFICIENCY_PER_USE.get()));
        crossbowProficiencyField.setValue(String.valueOf(ToolUpdateConfig.CROSSBOW_PROFICIENCY_PER_USE.get()));
        flintAndSteelProficiencyField.setValue(String.valueOf(ToolUpdateConfig.FLINT_AND_STEEL_PROFICIENCY_PER_USE.get()));
        shearsProficiencyField.setValue(String.valueOf(ToolUpdateConfig.SHEARS_PROFICIENCY_PER_USE.get()));
        shieldProficiencyField.setValue(String.valueOf(ToolUpdateConfig.SHIELD_PROFICIENCY_PER_USE.get()));
        brushProficiencyField.setValue(String.valueOf(ToolUpdateConfig.BRUSH_PROFICIENCY_PER_USE.get()));

        enableEffectsButton.setMessage(Component.literal("升级特效: " + (ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get() ? "✔" : "✘")));
        showTooltipButton.setMessage(Component.literal("显示熟练度提示: " + (ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get() ? "✔" : "✘")));
    }

    private void setupBottomButtons() {
        int buttonY = this.height - 30;
        int centerX = this.width / 2;

        this.addRenderableWidget(Button.builder(Component.literal("保存并返回"), button -> saveConfig())
                .bounds(centerX - BUTTON_WIDTH - 5, buttonY, BUTTON_WIDTH, 20)
                .build());

        this.addRenderableWidget(Button.builder(Component.literal("取消"), button -> this.minecraft.setScreen(parent))
                .bounds(centerX + 5, buttonY, BUTTON_WIDTH, 20)
                .build());
    }

    private void saveConfig() {
        try {
            ToolUpdateConfig.BASE_COST.set(Integer.parseInt(baseCostField.getValue()));
            ToolUpdateConfig.MAX_LEVEL_BONUS.set(Integer.parseInt(maxLevelBonusField.getValue()));
            ToolUpdateConfig.EFFECT_VOLUME.set(Math.max(0, Math.min(1, Double.parseDouble(effectVolumeField.getValue()))));
            ToolUpdateConfig.MAX_ENCHANT_LEVEL.set(Math.max(1, Math.min(114514, Integer.parseInt(maxEnchantLevelField.getValue()))));
            // 处理黑名单保存

            String blacklistText = blacklistField.getValue();
            LOGGER.debug("原始黑名单输入: {}", blacklistText); // 添加这行日志

            List<String> enchants = Arrays.stream(blacklistText.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            LOGGER.debug("处理后黑名单: {}", enchants);
            // 直接保存列表而不是长字符串
            ToolUpdateConfig.BLACKLISTED_ENCHANTS.set(enchants);

            ToolUpdateConfig.SWORD_PROFICIENCY_PER_USE.set(Integer.parseInt(swordProficiencyField.getValue()));
            ToolUpdateConfig.PICKAXE_PROFICIENCY_PER_USE.set(Integer.parseInt(pickaxeProficiencyField.getValue()));
            ToolUpdateConfig.AXE_PROFICIENCY_PER_USE.set(Integer.parseInt(axeProficiencyField.getValue()));
            ToolUpdateConfig.SHOVEL_PROFICIENCY_PER_USE.set(Integer.parseInt(shovelProficiencyField.getValue()));
            ToolUpdateConfig.HOE_PROFICIENCY_PER_USE.set(Integer.parseInt(hoeProficiencyField.getValue()));
            ToolUpdateConfig.TRIDENT_PROFICIENCY_PER_USE.set(Integer.parseInt(tridentProficiencyField.getValue()));
            ToolUpdateConfig.FISHING_ROD_PROFICIENCY_PER_USE.set(Integer.parseInt(fishingRodProficiencyField.getValue()));
            ToolUpdateConfig.BOW_PROFICIENCY_PER_USE.set(Integer.parseInt(bowProficiencyField.getValue()));
            ToolUpdateConfig.CROSSBOW_PROFICIENCY_PER_USE.set(Integer.parseInt(crossbowProficiencyField.getValue()));
            ToolUpdateConfig.FLINT_AND_STEEL_PROFICIENCY_PER_USE.set(Integer.parseInt(flintAndSteelProficiencyField.getValue()));
            ToolUpdateConfig.SHEARS_PROFICIENCY_PER_USE.set(Integer.parseInt(shearsProficiencyField.getValue()));
            ToolUpdateConfig.SHIELD_PROFICIENCY_PER_USE.set(Integer.parseInt(shieldProficiencyField.getValue()));
            ToolUpdateConfig.BRUSH_PROFICIENCY_PER_USE.set(Integer.parseInt(brushProficiencyField.getValue()));

            ToolUpdateConfig.SPEC.save();
            this.minecraft.setScreen(parent);
        } catch (NumberFormatException e) {
            // 错误处理
            LOGGER.error("保存配置时出错", e);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2 + 1, 16, 0x555555);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        scrollPanel.render(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private class ScrollPanel extends AbstractWidget {
        private final int contentLeft;
        private int scrollDistance;
        private boolean scrolling;
        private float smoothScrollDistance;
        private float targetScrollDistance;
        private float scrollVelocity;
        private long lastScrollTime;
        private static final float SMOOTH_SCROLL_FACTOR = 0.2f;
        private static final float FRICTION = 0.9f;
        private static final float SCROLL_SENSITIVITY = 0.5f;

        public ScrollPanel(Minecraft minecraft, int width, int screenHeight, int top, int bottom, int contentLeft) {
            super(0, top, width, bottom - top, Component.empty());
            this.contentLeft = contentLeft;
            this.lastScrollTime = System.currentTimeMillis();

            addRenderableWidget(baseCostField);
            addRenderableWidget(maxLevelBonusField);
            addRenderableWidget(enableEffectsButton);
            addRenderableWidget(effectVolumeField);
            addRenderableWidget(maxEnchantLevelField);
            addRenderableWidget(showTooltipButton);
            addRenderableWidget(blacklistField);
            addRenderableWidget(blacklistResetButton);
            addRenderableWidget(swordProficiencyField);
            addRenderableWidget(pickaxeProficiencyField);
            addRenderableWidget(axeProficiencyField);
            addRenderableWidget(shovelProficiencyField);
            addRenderableWidget(hoeProficiencyField);
            addRenderableWidget(tridentProficiencyField);
            addRenderableWidget(fishingRodProficiencyField);
            addRenderableWidget(bowProficiencyField);
            addRenderableWidget(crossbowProficiencyField);
            addRenderableWidget(flintAndSteelProficiencyField);
            addRenderableWidget(shearsProficiencyField);
            addRenderableWidget(shieldProficiencyField);
            addRenderableWidget(brushProficiencyField);
        }

        private void updateSmoothScrolling() {
            long currentTime = System.currentTimeMillis();
            float deltaTime = Math.min((currentTime - lastScrollTime) / 1000f, 0.1f);
            lastScrollTime = currentTime;

            if (Math.abs(scrollVelocity) > 0.1f) {
                targetScrollDistance += scrollVelocity * deltaTime * 50;
                scrollVelocity *= FRICTION;
            } else {
                scrollVelocity = 0;
            }

            int contentHeight = getContentHeight();
            if (contentHeight > this.height) {
                targetScrollDistance = Math.max(0,
                        Math.min(targetScrollDistance, contentHeight - this.height));
            } else {
                targetScrollDistance = 0;
            }

            smoothScrollDistance = Mth.lerp(SMOOTH_SCROLL_FACTOR, smoothScrollDistance, targetScrollDistance);
        }

        private int getContentHeight() {
            return getConfigEntries().size() * SPACING + 40;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            updateSmoothScrolling();
            this.scrollDistance = (int)smoothScrollDistance;

            drawRoundedBackground(guiGraphics);
            guiGraphics.enableScissor(
                    this.getX() + EDGE_PADDING,
                    this.getY() + EDGE_PADDING,
                    this.getX() + this.width - EDGE_PADDING,
                    this.getY() + this.height - EDGE_PADDING
            );

            drawContent(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.disableScissor();
            drawEdgeOverlays(guiGraphics);

            if (getContentHeight() > this.height) {
                drawScrollBar(guiGraphics);
            }
        }

        private void drawRoundedBackground(GuiGraphics guiGraphics) {
            int bgColor = 0xAA202020;
            int cornerRadius = 5;

            guiGraphics.fill(this.getX(), this.getY() + cornerRadius,
                    this.getX() + this.width, this.getY() + this.height - cornerRadius, bgColor);
            guiGraphics.fill(this.getX() + cornerRadius, this.getY(),
                    this.getX() + this.width - cornerRadius, this.getY() + this.height, bgColor);
            guiGraphics.fill(this.getX(), this.getY(),
                    this.getX() + cornerRadius, this.getY() + cornerRadius, bgColor);
            guiGraphics.fill(this.getX() + this.width - cornerRadius, this.getY(),
                    this.getX() + this.width, this.getY() + cornerRadius, bgColor);
            guiGraphics.fill(this.getX(), this.getY() + this.height - cornerRadius,
                    this.getX() + cornerRadius, this.getY() + this.height, bgColor);
            guiGraphics.fill(this.getX() + this.width - cornerRadius, this.getY() + this.height - cornerRadius,
                    this.getX() + this.width, this.getY() + this.height, bgColor);
        }

        private void drawContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            int y = this.getY() - this.scrollDistance + 10;
            int left = this.contentLeft;

            for (ConfigEntry entry : getConfigEntries()) {
                int relativeY = y - this.getY();
                boolean isVisible = (relativeY + entry.height >= EDGE_FADE_HEIGHT + EDGE_PADDING) &&
                        (relativeY <= this.height - EDGE_FADE_HEIGHT - EDGE_PADDING);

                if (isVisible) {
                    // 绘制标签
                    guiGraphics.drawString(font, entry.label, left, y + 5, 0xEEEEEE, false);

                    // 特殊处理黑名单行
                    if (entry.widget == blacklistField) {
                        // 计算可用宽度（考虑按钮和间距）
                        int availableWidth = FIELD_WIDTH - RESET_BUTTON_SIZE - 5;

                        // 设置输入框位置和大小
                        blacklistField.setWidth(availableWidth);
                        blacklistField.setMaxLength(Integer.MAX_VALUE); // 确保这里也设置了
                        blacklistField.setX(left + LABEL_WIDTH + 5);
                        blacklistField.setY(y);
                        blacklistField.render(guiGraphics, mouseX, mouseY, partialTicks);

                        // 设置重置按钮位置
                        blacklistResetButton.setX(left + LABEL_WIDTH + 5 + availableWidth + 5);
                        blacklistResetButton.setY(y);
                        blacklistResetButton.setWidth(RESET_BUTTON_SIZE);
                        blacklistResetButton.setHeight(20); // 与输入框同高
                        blacklistResetButton.render(guiGraphics, mouseX, mouseY, partialTicks);

                        // 显示按钮工具提示
                        if (blacklistResetButton.isMouseOver(mouseX, mouseY)) {
                            guiGraphics.renderTooltip(font,
                                    Component.literal("重置为默认黑名单"),
                                    mouseX, mouseY);
                        }
                    } else {
                        // 普通组件渲染
                        entry.widget.setX(left + LABEL_WIDTH + 5);
                        entry.widget.setY(y);
                        entry.widget.render(guiGraphics, mouseX, mouseY, partialTicks);
                    }
                } else {
                    // 移出不可见组件
                    entry.widget.setY(-1000);
                    if (entry.widget == blacklistField) {
                        blacklistResetButton.setY(-1000);
                    }
                }
                y += entry.height;
            }
        }

        private List<ConfigEntry> getConfigEntries() {
            return List.of(
                    new ConfigEntry("基础消耗:", baseCostField, SPACING),
                    new ConfigEntry("最大等级额外消耗:", maxLevelBonusField, SPACING),
                    new ConfigEntry("升级特效:", enableEffectsButton, SPACING),
                    new ConfigEntry("特效音量 (0-1):", effectVolumeField, SPACING),
                    new ConfigEntry("升级附魔最大等级", maxEnchantLevelField, SPACING),
                    new ConfigEntry("显示熟练度提示:", showTooltipButton, SPACING),
                    new ConfigEntry("禁止升级的附魔:", blacklistField, SPACING),
                    new ConfigEntry("剑每次增加熟练度:", swordProficiencyField, SPACING),
                    new ConfigEntry("镐每次增加熟练度:", pickaxeProficiencyField, SPACING),
                    new ConfigEntry("斧每次增加熟练度:", axeProficiencyField, SPACING),
                    new ConfigEntry("锹每次增加熟练度:", shovelProficiencyField, SPACING),
                    new ConfigEntry("锄每次增加熟练度:", hoeProficiencyField, SPACING),
                    new ConfigEntry("三叉戟每次增加熟练度:", tridentProficiencyField, SPACING),
                    new ConfigEntry("钓鱼竿每次增加熟练度:", fishingRodProficiencyField, SPACING),
                    new ConfigEntry("弓每次增加熟练度:", bowProficiencyField, SPACING),
                    new ConfigEntry("弩每次增加熟练度:", crossbowProficiencyField, SPACING),
                    new ConfigEntry("打火石每次增加熟练度:", flintAndSteelProficiencyField, SPACING),
                    new ConfigEntry("剪刀每次增加熟练度:", shearsProficiencyField, SPACING),
                    new ConfigEntry("盾牌每次增加熟练度:", shieldProficiencyField, SPACING),
                    new ConfigEntry("刷子每次增加熟练度:", brushProficiencyField, SPACING)
            );
        }

        private void drawEdgeOverlays(GuiGraphics guiGraphics) {
            guiGraphics.fillGradient(
                    this.getX(), this.getY(),
                    this.getX() + this.width, this.getY() + EDGE_FADE_HEIGHT,
                    0xFF1A1A1A, 0x001A1A1A
            );
            guiGraphics.fillGradient(
                    this.getX(), this.getY() + this.height - EDGE_FADE_HEIGHT,
                    this.getX() + this.width, this.getY() + this.height,
                    0x001A1A1A, 0xFF1A1A1A
            );
        }

        private void drawScrollBar(GuiGraphics guiGraphics) {
            int contentHeight = getContentHeight();
            if (contentHeight <= this.height) return;

            int availableHeight = this.height - EDGE_FADE_HEIGHT * 2;
            int barHeight = (int) ((float)availableHeight * availableHeight / contentHeight);
            barHeight = Math.max(barHeight, 32);

            float scrollProgress = targetScrollDistance / (contentHeight - this.height);
            int barTop = EDGE_FADE_HEIGHT + (int)(scrollProgress * (availableHeight - barHeight));

            guiGraphics.fill(
                    this.getX() + this.width - SCROLL_BAR_WIDTH - 2,
                    this.getY() + EDGE_FADE_HEIGHT,
                    this.getX() + this.width - 2,
                    this.getY() + this.height - EDGE_FADE_HEIGHT,
                    0x80000000
            );

            guiGraphics.fill(
                    this.getX() + this.width - SCROLL_BAR_WIDTH - 2,
                    this.getY() + barTop,
                    this.getX() + this.width - 2,
                    this.getY() + barTop + barHeight,
                    0xFFAAAAAA
            );
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
            int contentHeight = getContentHeight();
            if (contentHeight > this.height) {
                scrollVelocity = - (float)delta * 20 * SCROLL_SENSITIVITY;
                targetScrollDistance = (float)Math.max(0,
                        Math.min(targetScrollDistance - (float)delta * 20,
                                contentHeight - this.height));
                lastScrollTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (this.scrolling) {
                int contentHeight = getContentHeight();
                if (contentHeight <= this.height) {
                    targetScrollDistance = 0;
                    return false;
                }

                float scrollRatio = (float)contentHeight / this.height;
                targetScrollDistance = (float)(mouseY * scrollRatio - this.height / 2f);
                scrollVelocity = (float)(-dragY * scrollRatio * 5);

                targetScrollDistance = Math.max(0,
                        Math.min(targetScrollDistance, contentHeight - this.height));

                lastScrollTime = System.currentTimeMillis();
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                if (mouseX >= this.getX() + this.width - SCROLL_BAR_WIDTH &&
                        mouseX <= this.getX() + this.width) {
                    this.scrolling = true;
                    return true;
                }
                if (blacklistResetButton.isMouseOver(mouseX, mouseY)) {
                    return blacklistResetButton.mouseClicked(mouseX, mouseY, button);
                }
            }
            return false;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.scrolling = false;
            }
            blacklistResetButton.mouseReleased(mouseX, mouseY, button);
            return false;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        }

        private record ConfigEntry(String label, AbstractWidget widget, int height) {}
    }
}