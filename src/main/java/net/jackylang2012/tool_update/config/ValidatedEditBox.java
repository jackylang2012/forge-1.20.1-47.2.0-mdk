// ValidatedEditBox.java
package net.jackylang2012.tool_update.config;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

import java.util.function.IntPredicate;

public class ValidatedEditBox extends EditBox {
    private final IntPredicate validator;

    public ValidatedEditBox(Font font, int x, int y, int width, int height,
                            Component message, IntPredicate validator) {
        super(font, x, y, width, height, message);
        this.validator = validator;
        this.setFilter(this::validateInput);
    }

    private boolean validateInput(String input) {
        try {
            int value = input.isEmpty() ? 0 : Integer.parseInt(input);
            return validator.test(value);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        boolean valid = validateInput(this.getValue());
        this.setTextColor(valid ? 0xFFFFFF : 0xFF5555);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
}