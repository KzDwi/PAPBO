package kai.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Tombol kustom dengan efek hover dan tampilan modern sesuai brand KAI.
 */
public class StyledButton extends JButton {

    private Color normalColor;
    private Color hoverColor;
    private Color pressColor;
    private Color currentColor;
    private final int radius;
    private boolean isOutline;

    // ── Constructor Solid ──────────────────────────────────────────────
    public StyledButton(String text, Color color) {
        this(text, color, false);
    }

    // ── Constructor Outline ────────────────────────────────────────────
    public StyledButton(String text, Color color, boolean isOutline) {
        super(text);
        this.normalColor = color;
        this.isOutline   = isOutline;
        this.radius      = 10;

        // Hitung warna hover dan press
        this.hoverColor  = color.darker();
        this.pressColor  = color.darker().darker();
        this.currentColor = normalColor;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(UIConstants.FONT_BOLD);
        setForeground(isOutline ? color : Color.WHITE);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(getPreferredSize().width, 42));

        // Efek hover
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                setForeground(isOutline ? hoverColor : Color.WHITE);
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                currentColor = normalColor;
                setForeground(isOutline ? normalColor : Color.WHITE);
                repaint();
            }
            @Override public void mousePressed(MouseEvent e) {
                currentColor = pressColor;
                repaint();
            }
            @Override public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        if (isOutline) {
            g2.setColor(new Color(currentColor.getRed(), currentColor.getGreen(),
                                   currentColor.getBlue(), 20));
            g2.fillRoundRect(0, 0, w, h, radius, radius);
            g2.setColor(currentColor);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, radius, radius);
        } else {
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, w, h, radius, radius);
        }

        g2.dispose();
        super.paintComponent(g);
    }

    public void setNormalColor(Color c) {
        this.normalColor  = c;
        this.hoverColor   = c.darker();
        this.pressColor   = c.darker().darker();
        this.currentColor = c;
        repaint();
    }
}
