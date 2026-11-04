package tempestclient.gui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import tempestclient.BalancedPerformance;
import tempestclient.FullbrightController;
import tempestclient.TempestClientClient;

public class TempestScreen extends Screen {

    private static final int BACKGROUND = 0xF20B0B0F;
    private static final int PANEL = 0xFF15151C;
    private static final int ACCENT = 0xFFE3263A;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int MUTED = 0xFF9999A8;

    private KeyMapping waitingForKey;

    public TempestScreen() {
        super(Component.literal("Tempest Client"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 190;
        int top = this.height / 2 - 125;
        int col1 = left + 18;
        int col2 = left + 202;
        int width = 170;
        int row = top + 72;

        addRenderableWidget(Button.builder(
                Component.literal("Toggle Brightness Boost"),
                button -> FullbrightController.toggle()
        ).bounds(col1, row, width, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Toggle Balanced Performance"),
                button -> BalancedPerformance.toggle()
        ).bounds(col2, row, width, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Brightness -"),
                button -> FullbrightController.setBrightness(
                        FullbrightController.getBrightness() - 0.1
                )
        ).bounds(col1, row + 28, width, 20).build());

        addRenderableWidget(Button.builder(
                Component.literal("Brightness +"),
                button -> FullbrightController.setBrightness(
                        FullbrightController.getBrightness() + 0.1
                )
        ).bounds(col2, row + 28, width, 20).build());

        addRenderableWidget(bindButton("Bind brightness toggle",
                TempestClientClient.getFullbrightKey(), col1, row + 64, width));
        addRenderableWidget(bindButton("Bind performance toggle",
                TempestClientClient.getBalancedPerformanceKey(), col2, row + 64, width));
        addRenderableWidget(bindButton("Bind brightness down",
                TempestClientClient.getBrightnessDownKey(), col1, row + 92, width));
        addRenderableWidget(bindButton("Bind brightness up",
                TempestClientClient.getBrightnessUpKey(), col2, row + 92, width));
    }

    private Button bindButton(String label, KeyMapping mapping, int x, int y, int width) {
        return Button.builder(
                Component.literal(label + ": " + mapping.getBoundKeyLocalizedText().getString()),
                button -> waitingForKey = mapping
        ).bounds(x, y, width, 20).build();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKey != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                waitingForKey = null;
                return true;
            }

            waitingForKey.setKey(InputConstants.Type.KEYSYM.getOrCreate(keyCode));
            KeyMapping.resetMapping();
            waitingForKey = null;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int left = centerX - 190;
        int top = centerY - 125;
        int right = centerX + 190;
        int bottom = centerY + 125;

        graphics.fill(0, 0, this.width, this.height, BACKGROUND);
        graphics.fill(left, top, right, bottom, PANEL);
        graphics.fill(left, top, right, top + 3, ACCENT);

        graphics.drawString(this.font, "TEMPEST", left + 18, top + 16, ACCENT);
        graphics.drawString(this.font, "CLIENT SETTINGS", left + 18, top + 32, WHITE);
        graphics.drawString(this.font, "1.21.11 | FABRIC", right - 112, top + 24, MUTED);

        graphics.fill(left + 18, top + 55, right - 18, top + 56, 0xFF353540);

        graphics.drawString(this.font,
                "Brightness boost: " + (FullbrightController.isEnabled() ? "ON" : "OFF"),
                left + 18, top + 61, FullbrightController.isEnabled() ? ACCENT : MUTED);

        graphics.drawString(this.font,
                "Balanced profile: " + (BalancedPerformance.isEnabled() ? "ON" : "OFF"),
                left + 202, top + 61, BalancedPerformance.isEnabled() ? ACCENT : MUTED);

        super.render(graphics, mouseX, mouseY, delta);

        if (waitingForKey != null) {
            graphics.drawCenteredString(this.font,
                    "Press a key to bind (ESC cancels)",
                    centerX, bottom - 19, ACCENT);
        } else {
            graphics.drawString(this.font,
                    "Click a Bind button, then press your chosen key.",
                    left + 18, bottom - 19, MUTED);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
