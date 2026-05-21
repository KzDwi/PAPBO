package kai.gui;

import kai.controller.AuthManager;
import kai.model.AkunPengguna;

import javax.swing.*;
import java.awt.*;

/**
 * Panel profil pengguna yang sedang login.
 * Menampilkan data dari KTP/NIK yang terdaftar.
 */
public class ProfilPanel extends JPanel {

    public ProfilPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        build();
    }

    private void build() {
        AkunPengguna akun = AuthManager.getInstance().getAkunAktif();
        if (akun == null) return;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1;

        RoundedPanel card = new RoundedPanel(16, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(550, 420));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.weightx = 1;

        // Avatar area
        JPanel avatarArea = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU_GELAP,
                                                      getWidth(), 0, UIConstants.KAI_BIRU);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        avatarArea.setOpaque(false);
        avatarArea.setPreferredSize(new Dimension(500, 110));
        avatarArea.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 20));

        // Avatar circle
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(0, 0, 70, 70);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                String init = akun.getNamaLengkap().substring(0, 1).toUpperCase();
                int sx = (70 - fm.stringWidth(init)) / 2;
                int sy = (70 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(init, sx, sy);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(70, 70));

        JPanel namePanel = new JPanel(new GridLayout(3, 1, 0, 2));
        namePanel.setOpaque(false);
        JLabel lblNama = new JLabel(akun.getNamaLengkap());
        lblNama.setFont(UIConstants.FONT_SUBJUDUL);
        lblNama.setForeground(Color.WHITE);
        JLabel lblNIK = new JLabel("NIK: " + akun.getNik());
        lblNIK.setFont(UIConstants.FONT_MONO);
        lblNIK.setForeground(new Color(200, 220, 255));
        JLabel lblGender = new JLabel(akun.getGender().getDisplay() + "  |  " + akun.getUmur() + " tahun");
        lblGender.setFont(UIConstants.FONT_KECIL);
        lblGender.setForeground(new Color(180, 210, 255));
        namePanel.add(lblNama); namePanel.add(lblNIK); namePanel.add(lblGender);

        avatarArea.add(avatar);
        avatarArea.add(namePanel);

        c.gridy = 0; c.insets = new Insets(0, 0, 0, 0);
        card.add(avatarArea, c);

        // Detail info
        addDetailRow(card, c, 1, "Email",   akun.getEmail());
        addDetailRow(card, c, 2, "Alamat",  akun.getAlamat());
        addDetailRow(card, c, 3, "Gender",  akun.getGender().getDisplay());
        addDetailRow(card, c, 4, "Umur",    akun.getUmur() + " tahun");

        // Badge KTP
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        badge.setOpaque(false);
        badge.setBorder(BorderFactory.createEmptyBorder(8, 20, 0, 20));
        JLabel badgeLbl = new JLabel("  \u2713  Identitas Terverifikasi — Data KTP");
        badgeLbl.setFont(UIConstants.FONT_KECIL);
        badgeLbl.setForeground(UIConstants.KAI_HIJAU);
        badgeLbl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.KAI_HIJAU, 1),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        badge.add(badgeLbl);
        c.gridy = 5; c.insets = new Insets(14, 20, 20, 20);
        card.add(badge, c);

        gbc.gridy = 0;
        add(card, gbc);
    }

    private void addDetailRow(JPanel p, GridBagConstraints c, int row, String label, String val) {
        JPanel row_panel = new JPanel(new GridLayout(1, 2, 0, 0));
        row_panel.setOpaque(false);
        row_panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.KAI_BORDER),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        JLabel lblKey = new JLabel(label);
        lblKey.setFont(UIConstants.FONT_LABEL);
        lblKey.setForeground(UIConstants.KAI_ABU_GELAP);

        JLabel lblVal = new JLabel(val);
        lblVal.setFont(UIConstants.FONT_NORMAL);
        lblVal.setForeground(new Color(30, 40, 60));

        row_panel.add(lblKey);
        row_panel.add(lblVal);
        c.gridy = row; c.insets = new Insets(0, 0, 0, 0);
        p.add(row_panel, c);
    }
}
