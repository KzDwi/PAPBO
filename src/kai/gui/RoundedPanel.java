package kai.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Panel kustom dengan sudut membulat dan bayangan halus.
 * Digunakan sebagai komponen card di seluruh GUI.
 */
public class RoundedPanel extends JPanel {

    private final int radius;
    private Color bgColor;
    private boolean hasShadow;
    private Color borderColor;
    private int borderWidth;

    public RoundedPanel(int radius) {
        this(radius, Color.WHITE, false);
    }

    public RoundedPanel(int radius, Color bgColor, boolean hasShadow) {
        this.radius      = radius;
        this.bgColor     = bgColor;
        this.hasShadow   = hasShadow;
        this.borderColor = null;
        this.borderWidth = 0;
        setOpaque(false);
    }

    public void setBorderColor(Color c, int width) {
        this.borderColor = c;
        this.borderWidth = width;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Bayangan halus
        if (hasShadow) {
            for (int i = 4; i >= 1; i--) {
                g2.setColor(new Color(0, 0, 0, 8 * i));
                g2.fillRoundRect(i, i + 2, w - i * 2, h - i * 2, radius, radius);
            }
        }

        // Background
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, w - 1, h - 1, radius, radius);

        // Border opsional
        if (borderColor != null && borderWidth > 0) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.drawRoundRect(borderWidth / 2, borderWidth / 2,
                             w - borderWidth, h - borderWidth, radius, radius);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    public void setBgColor(Color c) { this.bgColor = c; repaint(); }
}
