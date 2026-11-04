package tempestclient.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import tempestclient.BalancedPerformance;
import tempestclient.FullbrightController;

public class TempestScreen extends Screen {
    private static final int PANEL = 0xFF111119;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF9999A8;
    private static final int ACCENT = 0xFFE3263A;

    public TempestScreen() {
        super(Component.literal("Tempest Client"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 175;
        int top = this.height / 2 - 100;
        int width = 150;

        addRenderableWidget(Button.builder(
                Component.literal("Toggle Fullbright"),
                button -> FullbrightController.toggle()
        ).bounds(left + 18, top + 75, width, 22).build());

        addRenderableWidget(Button.builder(
                Component.literal("Toggle Performance"),
                button -> BalancedPerformance.toggle()
        ).bounds(left + 182, top + 75, width, 22).build());

        addRenderableWidget(Button.builder(
                Component.literal("Brightness -"),
                button -> FullbrightController.setBrightness(
                        FullbrightController.getBrightness() - 0.1
                )
        ).bounds(left + 18, top + 108, width, 22).build());

        addRenderableWidget(Button.builder(
                Component.literal("Brightness +"),
                button -> FullbrightController.setBrightness(
                        FullbrightController.getBrightness() + 0.1
                )
        ).bounds(left + 182, top + 108, width, 22).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int left = centerX - 175;
        int top = centerY - 100;
        int right = centerX + 175;
        int bottom = centerY + 100;

        graphics.fill(0, 0, this.width, this.height, 0xA8000000);
        graphics.fill(left, top, right, bottom, PANEL);
        graphics.fill(left + 1, top + 1, right - 1, bottom - 1, 0xFF171720);

        long now = System.currentTimeMillis();
        int pulse = (int) (100 + 100 * (0.5 + 0.5 * Math.sin(now / 260.0)));
        int animatedAccent = (pulse << 24) | 0x00E3263A;
        graphics.fill(left, top, right, top + 3, animatedAccent);

        int sweepWidth = 54;
        int sweepX = left + 12 + (int) ((now / 12L) % (right - left - sweepWidth - 24));
        graphics.fill(sweepX, top + 3, sweepX + sweepWidth, top + 5, 0xFFE3263A);

        graphics.drawString(this.font, "TEMPEST", left + 18, top + 18, ACCENT);
        graphics.drawString(this.font, "CLIENT CONTROL CENTER", left + 18, top + 35, WHITE);
        graphics.drawString(this.font, "FABRIC 1.21.11", right - 112, top + 25, MUTED);
        graphics.fill(left + 18, top + 57, right - 18, top + 58, 0xFF383842);

        graphics.drawString(this.font,
                "Fullbright: " + (FullbrightController.isEnabled() ? "ON" : "OFF"),
                left + 18, top + 63, FullbrightController.isEnabled() ? ACCENT : MUTED);
        graphics.drawString(this.font,
                "Performance: " + (BalancedPerformance.isEnabled() ? "ON" : "OFF"),
                left + 182, top + 63, BalancedPerformance.isEnabled() ? ACCENT : MUTED);
        graphics.drawString(this.font,
                "Brightness level: " + Math.round(FullbrightController.getBrightness() * 100) + "%",
                left + 18, top + 139, WHITE);
        graphics.drawString(this.font,
                "F10  |  Close with ESC",
                left + 18, bottom - 20, MUTED);

        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
