package net.jackylang2012.tool_update.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ToolUpdateConfigScreen extends Screen {
    private final Screen parent;

    private EditBox baseCostField;
    private EditBox maxLevelBonusField;
    private Button enableEffectsButton;
    private EditBox effectVolumeField;
    private Button showTooltipButton;
    private EditBox blacklistField;

    public ToolUpdateConfigScreen(Screen parent) {
        super(Component.literal("Tool Update 配置"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int y = this.height / 6;
        int spacing = 35;
        int width = 200;
        int left = this.width / 2 - width / 2;

        // 基础消耗 输入框
        baseCostField = new EditBox(this.font, left, y, width, 20, Component.literal(""));
        baseCostField.setValue(String.valueOf(ToolUpdateConfig.BASE_COST.get()));
        addRenderableWidget(baseCostField);
        y += spacing;

        // 最大等级额外消耗 输入框
        maxLevelBonusField = new EditBox(this.font, left, y, width, 20, Component.literal(""));
        maxLevelBonusField.setValue(String.valueOf(ToolUpdateConfig.MAX_LEVEL_BONUS.get()));
        addRenderableWidget(maxLevelBonusField);
        y += 24;

        // 升级特效 按钮
        enableEffectsButton = Button.builder(
                        Component.literal("升级特效: " + (ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get() ? "开" : "关")),
                        button -> {
                            ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.set(!ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get());
                            button.setMessage(Component.literal("升级特效: " + (ToolUpdateConfig.ENABLE_UPGRADE_EFFECTS.get() ? "开" : "关")));
                        })
                .bounds(left, y, width, 20)
                .build();
        addRenderableWidget(enableEffectsButton);
        y += spacing;

        // 特效音量 输入框
        effectVolumeField = new EditBox(this.font, left, y, width, 20, Component.literal(""));
        effectVolumeField.setValue(String.valueOf(ToolUpdateConfig.EFFECT_VOLUME.get()));
        addRenderableWidget(effectVolumeField);
        y += 24;

        // 显示熟练度提示 按钮
        showTooltipButton = Button.builder(
                        Component.literal("显示熟练度提示: " + (ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get() ? "开" : "关")),
                        button -> {
                            ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.set(!ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get());
                            button.setMessage(Component.literal("显示熟练度提示: " + (ToolUpdateConfig.SHOW_PROFICIENCY_TOOLTIP.get() ? "开" : "关")));
                        })
                .bounds(left, y, width, 20)
                .build();
        addRenderableWidget(showTooltipButton);
        y += spacing;

        // 禁止升级附魔 输入框
        blacklistField = new EditBox(this.font, left, y, width, 20, Component.literal(""));
        List<? extends String> blacklist = ToolUpdateConfig.BLACKLISTED_ENCHANTS.get();
        blacklistField.setValue(String.join(",", blacklist));
        addRenderableWidget(blacklistField);
        y += 24;

        // 保存按钮
        addRenderableWidget(Button.builder(Component.literal("保存配置"), button -> {
            try {
                ToolUpdateConfig.BASE_COST.set(Integer.parseInt(baseCostField.getValue()));
                ToolUpdateConfig.MAX_LEVEL_BONUS.set(Integer.parseInt(maxLevelBonusField.getValue()));
                ToolUpdateConfig.EFFECT_VOLUME.set(Double.parseDouble(effectVolumeField.getValue()));
                ToolUpdateConfig.BLACKLISTED_ENCHANTS.set(List.of(blacklistField.getValue().split("\\s*,\\s*")));
                // 布尔值按钮已即时修改，若需要可同步到配置文件
                ToolUpdateConfig.SPEC.save();
                this.minecraft.setScreen(parent);
            } catch (Exception e) {
                // 错误处理，比如显示提示（此处略）
            }
        }).bounds(left, y, width, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);

        // 画界面标题
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

        // 各输入框和按钮的描述文字，绘制在输入框上方（大约距离输入框顶部12像素）
        int y = this.height / 6;
        int spacing = 32;
        int left = this.width / 2 - 200 / 2;

        guiGraphics.drawString(this.font, "基础消耗", left, y - 10, 0xFFFFFF);
        guiGraphics.drawString(this.font, "最大等级额外消耗", left, y + spacing - 7, 0xFFFFFF);
        // 按钮自带文字，不用单独绘制
        guiGraphics.drawString(this.font, "特效音量", left, y + spacing * 3 - 12, 0xFFFFFF);
        // 按钮自带文字，不用单独绘制
        guiGraphics.drawString(this.font, "禁止升级附魔 (逗号分隔)", left, y + spacing * 5 - 17, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}
