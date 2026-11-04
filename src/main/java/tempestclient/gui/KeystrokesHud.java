package tempestclient.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Lightweight, client-side input display. It only reads the local player's
 * key and mouse state; it does not send packets or change gameplay.
 */
public final class KeystrokesHud {
    private KeystrokesHud() {}

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) {
            return;
        }

        int x = 12;
        int y = 12;
        int cell = 18;
        int gap = 3;

        drawKey(graphics, client, "W", x + cell + gap, y,
                client.options.keyUp.isDown());
        drawKey(graphics, client, "A", x, y + cell + gap,
                client.options.keyLeft.isDown());
        drawKey(graphics, client, "S", x + cell + gap, y + cell + gap,
                client.options.keyDown.isDown());
        drawKey(graphics, client, "D", x + (cell + gap) * 2, y + cell + gap,
                client.options.keyRight.isDown());

        drawKey(graphics, client, "LMB", x, y + (cell + gap) * 2,
                client.mouseHandler.isLeftPressed(), 30);
        drawKey(graphics, client, "RMB", x + 33, y + (cell + gap) * 2,
                client.mouseHandler.isRightPressed(), 30);

        drawKey(graphics, client, "SPACE", x, y + (cell + gap) * 3,
                client.options.keyJump.isDown(), 63);
    }

    private static void drawKey(GuiGraphicsExtractor graphics, Minecraft client,
                                String label, int x, int y, boolean pressed) {
        drawKey(graphics, client, label, x, y, pressed, 18);
    }

    private static void drawKey(GuiGraphicsExtractor graphics, Minecraft client,
                                String label, int x, int y, boolean pressed, int width) {
        int background = pressed ? 0xFFE3263A : 0xCC15151C;
        int border = pressed ? 0xFFFF6674 : 0xFF454550;
        int text = 0xFFFFFFFF;

        graphics.fill(x, y, x + width, y + 18, background);
        graphics.fill(x, y, x + width, y + 1, border);
        graphics.fill(x, y + 17, x + width, y + 18, border);
        graphics.fill(x, y, x + 1, y + 18, border);
        graphics.fill(x + width - 1, y, x + width, y + 18, border);

        int textWidth = client.font.width(label);
        graphics.drawString(client.font, label,
                x + (width - textWidth) / 2, y + 5, text);
    }
}
