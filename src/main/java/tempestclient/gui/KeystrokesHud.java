package tempestclient.gui;

import java.util.ArrayDeque;
import java.util.Deque;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

/** Lightweight input display with rolling one-second mouse click counters. */
public final class KeystrokesHud {
    private static final Deque<Long> leftClicks = new ArrayDeque<>();
    private static final Deque<Long> rightClicks = new ArrayDeque<>();
    private static boolean wasLeftPressed;
    private static boolean wasRightPressed;

    private KeystrokesHud() {}

    public static void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options.hideGui) return;

        long now = System.currentTimeMillis();
        boolean leftPressed = client.mouseHandler.isLeftPressed();
        boolean rightPressed = client.mouseHandler.isRightPressed();

        if (leftPressed && !wasLeftPressed) leftClicks.addLast(now);
        if (rightPressed && !wasRightPressed) rightClicks.addLast(now);
        wasLeftPressed = leftPressed;
        wasRightPressed = rightPressed;
        trim(leftClicks, now);
        trim(rightClicks, now);

        int x = 12, y = 12, cell = 18, gap = 3;
        int panelWidth = 69, panelHeight = 119;
        graphics.fill(x - 5, y - 5, x + panelWidth, y + panelHeight, 0xB8101018);
        graphics.fill(x - 5, y - 5, x + panelWidth, y - 4, 0xFFE3263A);
        graphics.drawString(client.font, "TEMPEST", x, y - 1, 0xFFE3263A, true);

        drawKey(graphics, client, "W", x + cell + gap, y + 12, client.options.keyUp.isDown());
        drawKey(graphics, client, "A", x, y + 12 + cell + gap, client.options.keyLeft.isDown());
        drawKey(graphics, client, "S", x + cell + gap, y + 12 + cell + gap, client.options.keyDown.isDown());
        drawKey(graphics, client, "D", x + (cell + gap) * 2, y + 12 + cell + gap, client.options.keyRight.isDown());
        drawKey(graphics, client, "LMB", x, y + 12 + (cell + gap) * 2, leftPressed, 30);
        drawKey(graphics, client, "RMB", x + 33, y + 12 + (cell + gap) * 2, rightPressed, 30);
        drawKey(graphics, client, "SPACE", x, y + 12 + (cell + gap) * 3, client.options.keyJump.isDown(), 63);

        graphics.fill(x, y + 12 + (cell + gap) * 4, x + 63, y + 12 + (cell + gap) * 4 + 1, 0xFF454550);
        graphics.drawString(client.font, "CPS  " + leftClicks.size() + " / " + rightClicks.size(),
                x, y + 12 + (cell + gap) * 4 + 5, 0xFFFFFFFF, true);
    }

    private static void trim(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() >= 1000L) clicks.removeFirst();
    }

    private static void drawKey(GuiGraphics graphics, Minecraft client,
                                String label, int x, int y, boolean pressed) {
        drawKey(graphics, client, label, x, y, pressed, 18);
    }

    private static void drawKey(GuiGraphics graphics, Minecraft client,
                                String label, int x, int y, boolean pressed, int width) {
        int background = pressed ? 0xFFE3263A : 0xCC15151C;
        int border = pressed ? 0xFFFF6674 : 0xFF454550;
        graphics.fill(x, y, x + width, y + 18, background);
        graphics.fill(x, y, x + width, y + 1, border);
        graphics.fill(x, y + 17, x + width, y + 18, border);
        graphics.fill(x, y, x + 1, y + 18, border);
        graphics.fill(x + width - 1, y, x + width, y + 18, border);
        int textWidth = client.font.width(label);
        graphics.drawString(client.font, label, x + (width - textWidth) / 2, y + 5, 0xFFFFFFFF);
    }
}
