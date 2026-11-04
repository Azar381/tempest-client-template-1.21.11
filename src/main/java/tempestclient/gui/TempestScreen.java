package tempestclient.gui;

import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import tempestclient.BalancedPerformance;

public class TempestScreen extends Screen {
    private static final int BG = 0xF20B0B10;
    private static final int PANEL = 0xF216161F;
    private static final int PANEL_ALT = 0xF21D1D28;
    private static final int ACCENT = 0xFFE3263A;
    private static final int TEXT = 0xFFF2F2F6;
    private static final int MUTED = 0xFF92929F;
    private static final String[] CATEGORIES = {"Visuals", "Performance", "HUD", "Settings"};
    private int activeCategory;
    private boolean expanded;
    private EditBox searchBox;

    public TempestScreen() {
        super(Component.literal("Tempest Client"));
    }

    @Override
    protected void init() {
        clearWidgets();
        if (expanded) {
            initExpanded();
        } else {
            initMain();
        }
    }

    private void initMain() {
        int left = width / 2 - 280;
        int top = height / 2 - 172;
        searchBox = new EditBox(font, left + 154, top + 48, 330, 22, Component.literal("Search"));
        searchBox.setHint(Component.literal("Search features..."));
        addRenderableWidget(searchBox);

        int sy = top + 82;
        for (int i = 0; i < CATEGORIES.length; i++) {
            final int index = i;
            addRenderableWidget(Button.builder(Component.literal(CATEGORIES[i]), b -> {
                activeCategory = index;
                init();
            }).bounds(left + 12, sy + i * 34, 126, 27).build());
        }

        addFeatureButtons(left, top);
    }

    private void addFeatureButtons(int left, int top) {
        int x = left + 158;
        int y = top + 104;
        if (activeCategory == 0) {
            // Fullbright controls are intentionally unavailable in this compatibility test.
        } else if (activeCategory == 1) {
            addRenderableWidget(Button.builder(toggleLabel("Balanced Performance", BalancedPerformance.isEnabled()),
                    b -> BalancedPerformance.toggle()).bounds(x + 305, y, 105, 24).build());
        } else if (activeCategory == 2) {
            addRenderableWidget(Button.builder(Component.literal("Keystrokes HUD: visible"),
                    b -> {}).bounds(x + 305, y, 105, 24).build());
            addRenderableWidget(Button.builder(Component.literal("CPS is shown in the HUD"),
                    b -> {}).bounds(x, y + 42, 240, 24).build());
        } else {
            addRenderableWidget(Button.builder(Component.literal("F10 opens this menu"),
                    b -> {}).bounds(x, y, 240, 24).build());
            addRenderableWidget(Button.builder(Component.literal("Press 1 for panel view"),
                    b -> expanded = true).bounds(x, y + 42, 240, 24).build());
        }
    }

    private void initExpanded() {
        int panelWidth = 132;
        int gap = 8;
        int total = panelWidth * 4 + gap * 3;
        int left = (width - total) / 2;
        int top = height / 2 - 135;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int x = left + i * (panelWidth + gap);
            final int category = i;
            addRenderableWidget(Button.builder(Component.literal("Open " + CATEGORIES[i]),
                    b -> { activeCategory = category; expanded = false; init(); })
                    .bounds(x + 8, top + 34, panelWidth - 16, 22).build());
        }
    }

    private Component toggleLabel(String name, boolean enabled) {
        return Component.literal(name + "  " + (enabled ? "ON" : "OFF"));
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        g.fill(0, 0, width, height, 0x99000000);
        long now = System.currentTimeMillis();
        int pulse = 150 + (int)(55 * (0.5 + 0.5 * Math.sin(now / 300.0)));
        int accent = (pulse << 24) | 0x00E3263A;

        if (expanded) {
            renderExpanded(g, now, accent);
        } else {
            renderMain(g, now, accent);
        }
        super.render(g, mouseX, mouseY, delta);
    }

    private void renderMain(GuiGraphics g, long now, int accent) {
        int left = width / 2 - 280, top = height / 2 - 172;
        int right = left + 560, bottom = top + 344;
        g.fill(left, top, right, bottom, BG);
        g.fill(left, top, right, top + 3, accent);
        g.fill(left + 1, top + 1, left + 145, bottom - 1, PANEL);
        g.fill(left + 145, top + 1, left + 146, bottom - 1, 0xFF353541);
        g.drawString(font, "TEMPEST", left + 17, top + 17, ACCENT);
        g.drawString(font, "CLIENT", left + 17, top + 31, TEXT);
        g.drawString(font, "Premium visual experience", left + 17, top + 49, MUTED);
        g.drawString(font, CATEGORIES[activeCategory].toUpperCase(), left + 158, top + 18, TEXT);
        g.drawString(font, "Search and configure your client", left + 158, top + 31, MUTED);
        g.fill(left + 158, top + 78, right - 18, top + 79, 0xFF373743);

        int x = left + 158, y = top + 104;
        if (activeCategory == 0) {
            featureRow(g, x, y, "Fullbright disabled", "Temporarily removed for Mojo compatibility testing", false);
        } else if (activeCategory == 1) {
            featureRow(g, x, y, "Balanced Performance", "Conservative graphics profile", BalancedPerformance.isEnabled());
        } else if (activeCategory == 2) {
            featureRow(g, x, y, "Keystrokes HUD", "WASD, mouse buttons and rolling CPS", true);
        } else {
            featureRow(g, x, y, "GUI keybind", "F10 opens and closes this menu", true);
            g.drawString(font, "Press 1 while this screen is open for panel view", x, y + 38, MUTED);
        }
        g.drawString(font, "F10  CLOSE     1  PANEL VIEW", left + 17, bottom - 22, MUTED);
        int sweep = left + 150 + (int)((now / 15L) % 380);
        g.fill(sweep, top + 3, Math.min(sweep + 32, right), top + 5, ACCENT);
    }

    private void featureRow(GuiGraphics g, int x, int y, String title, String desc, boolean enabled) {
        g.fill(x, y - 8, width / 2 + 260, y + 25, PANEL_ALT);
        g.drawString(font, title, x + 10, y, TEXT);
        g.drawString(font, desc, x + 10, y + 12, MUTED);
        g.fill(width / 2 + 185, y - 2, width / 2 + 245, y + 17,
                enabled ? 0xFF6B2633 : 0xFF383842);
        g.fill(width / 2 + (enabled ? 216 : 188), y + 1, width / 2 + (enabled ? 240 : 212), y + 14,
                enabled ? ACCENT : 0xFF777783);
    }

    private void renderExpanded(GuiGraphics g, long now, int accent) {
        int panelWidth = 132, gap = 8, total = panelWidth * 4 + gap * 3;
        int left = (width - total) / 2, top = height / 2 - 135;
        for (int i = 0; i < CATEGORIES.length; i++) {
            int x = left + i * (panelWidth + gap);
            g.fill(x, top, x + panelWidth, top + 270, 0xE912121A);
            g.fill(x, top, x + panelWidth, top + 2, accent);
            g.drawString(font, CATEGORIES[i], x + 9, top + 13, TEXT);
            if (i == 0) {
                g.drawString(font, "Fullbright", x + 9, top + 70, MUTED);
                g.drawString(font, "Disabled", x + 9, top + 85, ACCENT);
                g.drawString(font, "Mojo test", x + 9, top + 116, MUTED);
                g.drawString(font, "Temporary", x + 9, top + 131, TEXT);
            } else if (i == 1) {
                g.drawString(font, "Performance", x + 9, top + 70, MUTED);
                g.drawString(font, BalancedPerformance.isEnabled() ? "ON" : "OFF", x + 9, top + 85, ACCENT);
                g.drawString(font, "Render distance", x + 9, top + 116, MUTED);
                g.drawString(font, "≤ 8 chunks", x + 9, top + 131, TEXT);
            } else if (i == 2) {
                g.drawString(font, "Keystrokes", x + 9, top + 70, MUTED);
                g.drawString(font, "W A S D", x + 9, top + 85, TEXT);
                g.drawString(font, "Mouse CPS", x + 9, top + 116, MUTED);
                g.drawString(font, "LMB / RMB", x + 9, top + 131, TEXT);
            } else {
                g.drawString(font, "GUI key", x + 9, top + 70, MUTED);
                g.drawString(font, "F10", x + 9, top + 85, TEXT);
                g.drawString(font, "Layout", x + 9, top + 116, MUTED);
                g.drawString(font, "Panel view", x + 9, top + 131, TEXT);
            }
        }
        g.drawString(font, "TEMPEST  /  PANEL VIEW     Press 1 to return", left, top + 288, MUTED);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == GLFW.GLFW_KEY_1) {
            expanded = !expanded;
            init();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
