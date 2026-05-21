package kai.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * TextField kustom dengan sudut membulat, placeholder, dan highlight saat fokus.
 */
public class StyledTextField extends JTextField {

    private String placeholder;
    private Color  borderNormal = UIConstants.KAI_BORDER;
    private Color  borderFocus  = UIConstants.KAI_BIRU;
    private Color  currentBorder;
    private final int radius    = 8;

    public StyledTextField(String placeholder) {
        this.placeholder   = placeholder;
        this.currentBorder = borderNormal;

        setOpaque(false);
        setFont(UIConstants.FONT_NORMAL);
        setForeground(new Color(30, 40, 60));
        setPreferredSize(new Dimension(200, 42));
        setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                currentBorder = borderFocus;
                repaint();
            }
            @Override public void focusLost(FocusEvent e) {
                currentBorder = borderNormal;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.setColor(currentBorder);
        g2.setStroke(new BasicStroke(currentBorder == borderFocus ? 2f : 1.5f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
        super.paintComponent(g);

        // Placeholder
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D gp = (Graphics2D) g.create();
            gp.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gp.setColor(new Color(160, 170, 190));
            gp.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fm = gp.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            gp.drawString(placeholder, 14, y);
            gp.dispose();
        }
    }
}
