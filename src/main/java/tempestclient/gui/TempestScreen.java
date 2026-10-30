
package tempestclient.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TempestScreen extends Screen {

    private static final int BACKGROUND = 0xF20B0B0F;
    private static final int PANEL = 0xFF15151C;
    private static final int ACCENT = 0xFFE3263A;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF9999A8;

    public TempestScreen() {
        super(Component.literal("Tempest Client"));
    }

    @Override
    public void render(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Main background
        graphics.fill(
                0, 0,
                this.width, this.height,
                BACKGROUND
        );

        // Main window
        int left = centerX - 220;
        int top = centerY - 140;
        int right = centerX + 220;
        int bottom = centerY + 140;

        graphics.fill(left, top, right, bottom, PANEL);

        // Crimson top accent
        graphics.fill(
                left, top,
                right, top + 3,
                ACCENT
        );

        // Header
        graphics.drawString(
                this.font,
                "TEMPEST",
                left + 18,
                top + 18,
                ACCENT
        );

        graphics.drawString(
                this.font,
                "CLIENT",
                left + 18,
                top + 32,
                WHITE
        );

        graphics.drawString(
                this.font,
                "1.21.11  |  FABRIC",
                right - 115,
                top + 25,
                MUTED
        );

        // Separator
        graphics.fill(
                left + 15,
                top + 55,
                right - 15,
                top + 56,
                0xFF353540
        );

        // Sidebar
        graphics.fill(
                left + 12,
                top + 68,
                left + 115,
                bottom - 15,
                0xFF101015
        );

        graphics.fill(
                left + 12,
                top + 68,
                left + 15,
                top + 94,
                ACCENT
        );

        graphics.drawString(
                this.font,
                "VISUALS",
                left + 25,
                top + 77,
                WHITE
        );

        graphics.drawString(
                this.font,
                "UTILITY",
                left + 25,
                top + 110,
                MUTED
        );

        graphics.drawString(
                this.font,
                "PRACTICE",
                left + 25,
                top + 143,
                MUTED
        );

        // Main content
        graphics.drawString(
                this.font,
                "Welcome to Tempest",
                left + 135,
                top + 75,
                WHITE
        );

        graphics.drawString(
                this.font,
                "Your client experience starts here.",
                left + 135,
                top + 94,
                MUTED
        );

        graphics.drawString(
                this.font,
                "VISUAL MODULES",
                left + 135,
                top + 132,
                ACCENT
        );

        graphics.fill(
                left + 135,
                top + 151,
                right - 18,
                top + 193,
                0xFF20202A
        );

        graphics.drawString(
                this.font,
                "Visual settings will appear here.",
                left + 147,
                top + 167,
                WHITE
        );

        // Footer
        graphics.drawString(
                this.font,
                "TEMPEST CLIENT  -  IN DEVELOPMENT",
                left + 18,
                bottom - 25,
                MUTED
        );

        super.render(graphics, mouseX, mouseY, delta);
    }
}
