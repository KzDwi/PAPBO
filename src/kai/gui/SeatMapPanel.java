package kai.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import kai.model.Gerbong;
import kai.model.Kursi;

/**
 * Panel untuk menampilkan peta visual kursi dalam satu gerbong.
 * Menunjukkan kursi kosong (hijau) dan terisi (merah) secara grafis.
 */
public class SeatMapPanel extends JPanel {

    private Gerbong gerbong;
    private Kursi   kursiTerpilih;

    public SeatMapPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(420, 200));
    }

    public void setGerbong(Gerbong g) {
        this.gerbong      = g;
        this.kursiTerpilih = null;
        repaint();
    }

    public void setKursiTerpilih(Kursi k) {
        this.kursiTerpilih = k;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gerbong == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Kursi> kursiList = gerbong.getDaftarKursi();
        int total   = kursiList.size();
        int kolom   = 4;  // A B C D
        int baris   = (int) Math.ceil((double) total / kolom);

        int cellW  = 44;
        int cellH  = 36;
        int gapX   = 8;
        int gapY   = 8;
        int startX = 40;
        int startY = 30;

        // Label kolom
        String[] kolomLabel = {"A", "B", "C", "D"};
        g2.setFont(UIConstants.FONT_BOLD);
        g2.setColor(UIConstants.KAI_ABU_GELAP);
        for (int c = 0; c < kolom; c++) {
            int x = startX + c * (cellW + gapX);
            g2.drawString(kolomLabel[c], x + cellW / 2 - 4, startY - 8);
        }

        // Gambar kursi
        for (int i = 0; i < total; i++) {
            Kursi k  = kursiList.get(i);
            int row  = i / kolom;
            int col  = i % kolom;
            int x    = startX + col * (cellW + gapX);
            int y    = startY + row * (cellH + gapY);

            // Warna berdasarkan status
            Color bg, fg, border;
            if (kursiTerpilih != null && k == kursiTerpilih) {
                bg     = UIConstants.KAI_EMAS;
                fg     = Color.WHITE;
                border = UIConstants.KAI_EMAS.darker();
            } else if (k.isTerisi()) {
                bg     = new Color(220, 80, 80);
                fg     = Color.WHITE;
                border = new Color(180, 40, 40);
            } else {
                bg     = new Color(60, 180, 100);
                fg     = Color.WHITE;
                border = new Color(30, 140, 70);
            }

            // Gambar kotak kursi
            g2.setColor(bg);
            g2.fillRoundRect(x, y, cellW, cellH, 6, 6);
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, cellW, cellH, 6, 6);

            // Nomor kursi
            g2.setColor(fg);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (cellW - fm.stringWidth(k.getNomorKursi())) / 2;
            int ty = y + (cellH + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(k.getNomorKursi(), tx, ty);

            // Label baris di sisi kiri
            if (col == 0) {
                g2.setColor(UIConstants.KAI_ABU_GELAP);
                g2.setFont(UIConstants.FONT_KECIL);
                g2.drawString(String.valueOf(row + 1), startX - 22, y + cellH / 2 + 4);
            }
        }

        // Legenda
        int legY = startY + baris * (cellH + gapY) + 12;
        drawLegend(g2, startX, legY, new Color(60, 180, 100), "Kosong");
        drawLegend(g2, startX + 90, legY, new Color(220, 80, 80), "Terisi");
        // drawLegend(g2, startX + 170, legY, UIConstants.KAI_EMAS, "Dipilih");

        g2.dispose();
    }

    private void drawLegend(Graphics2D g2, int x, int y, Color c, String label) {
        g2.setColor(c);
        g2.fillRoundRect(x, y, 14, 14, 4, 4);
        g2.setColor(UIConstants.KAI_ABU_GELAP);
        g2.setFont(UIConstants.FONT_KECIL);
        g2.drawString(label, x + 18, y + 11);
    }
}
