package kai.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import kai.controller.AuthManager;
import kai.controller.SistemAlokasi;
import kai.model.AkunPengguna;
import kai.model.Kereta;

/**
 * Frame dashboard utama setelah pengguna berhasil login.
 * Memiliki sidebar navigasi dan area konten yang berganti panel.
 */
public class DashboardFrame extends JFrame {

    // ── Layout areas ───────────────────────────────────────────────────
    private JPanel         contentArea;
    private CardLayout     cardLayout;

    // ── Nav button references ──────────────────────────────────────────
    private JButton[]      navButtons;
    private String[]       navNames   = {"Dashboard", "Pesan Tiket", "Riwayat", "Profil"};
    // private String[]       navIcons   = {"\uD83C\uDFE0", "\uD83C\uDFAB", "\uD83D\uDCCB", "\uD83D\uDC64"};
    private String[]       panelKeys  = {"home", "pesan", "riwayat", "profil"};

    private RiwayatPanel   riwayatPanel;
    private JPanel         sidebarStatPanel;
    private JPanel         homeStatsRow;

    public DashboardFrame() {
        setTitle("KAI — Sistem Tiket & Alokasi Kursi");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setContentPane(buildLayout());
    }

    // ── Main Layout ────────────────────────────────────────────────────
    private JPanel buildLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.KAI_ABU);

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildContentArea(), BorderLayout.CENTER); //Ini dijalanin duluan biar ga error
        root.add(buildSidebar(), BorderLayout.WEST); //Tadi ketukar disini

        return root;
    }

    // ── Top Bar ────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU_GELAP,
                                                      getWidth(), 0, UIConstants.KAI_BIRU);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setPreferredSize(new Dimension(0, 60));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Kiri: Logo & nama app
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel emoji = new JLabel("\uD83D\uDE82");
        emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        JLabel appName = new JLabel("PT KERETA API INDONESIA");
        appName.setFont(UIConstants.FONT_BOLD);
        appName.setForeground(Color.WHITE);
        left.add(emoji); left.add(appName);
        bar.add(left, BorderLayout.WEST);

        // Kanan: Info user + logout
        AkunPengguna akun = AuthManager.getInstance().getAkunAktif();
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        if (akun != null) {
            JLabel greeting = new JLabel("Halo, " + akun.getNamaLengkap().split(" ")[0] + "!");
            greeting.setFont(UIConstants.FONT_NORMAL);
            greeting.setForeground(new Color(200, 220, 255));
            right.add(greeting);
        }

        StyledButton btnLogout = new StyledButton("Keluar", UIConstants.KAI_MERAH);
        btnLogout.setPreferredSize(new Dimension(90, 34));
        btnLogout.addActionListener(e -> logout());
        right.add(btnLogout);
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    // ── Sidebar ────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(UIConstants.KAI_BIRU_GELAP);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 0,12, 0));

        navButtons = new JButton[navNames.length];

        for (int i = 0; i < navNames.length; i++) {
            final int idx = i;
            JButton btn = createNavButton(navNames[i]);
            navButtons[i] = btn;
            btn.addActionListener(e -> switchPanel(idx));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // Stat kursi sisa
        sidebarStatPanel = new JPanel(new GridLayout(1, 1));
        sidebarStatPanel.setOpaque(false);
        sidebarStatPanel.add(buildStatPanelContent());
        sidebar.add(sidebarStatPanel);

        // Aktifkan tab pertama
        switchPanel(0);
        return sidebar;
    }

    private JButton createNavButton(String label) {
        // Memanggil Versi 2 dengan warna default (biru muda)
        return createNavButton(label, new Color(200, 220, 255));
    }

    private JButton createNavButton(String label, Color textColor) {
        JButton btn = new JButton(label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isSelected()) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(UIConstants.KAI_EMAS);
                    g2.fillRect(0, 0, 4, getHeight());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 12));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(UIConstants.FONT_NORMAL);
        btn.setForeground(textColor); // Menggunakan parameter warna dari Overloading
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setPreferredSize(new Dimension(240, 46));
        btn.setMinimumSize(new Dimension(100, 46));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 8));
        btn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel buildStatPanelContent() {
        JPanel p = new JPanel(new GridLayout(0, 1, 0, 4));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 12));

        JLabel hdr = new JLabel("Status Kereta");
        hdr.setFont(UIConstants.FONT_KECIL);
        hdr.setForeground(new Color(140, 170, 220));
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(hdr);

        for (Kereta k : SistemAlokasi.getInstance().getDaftarKereta()) {
            JLabel lbl = new JLabel(k.getNamaKereta() + ": " + k.getTotalKursiKosong() + " kosong");
            lbl.setFont(UIConstants.FONT_KECIL);
            lbl.setForeground(new Color(160, 200, 255));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(lbl);
        }
        return p;
    }

    public void refreshDashboardStats() {
        if (sidebarStatPanel != null) {
            sidebarStatPanel.removeAll();
            sidebarStatPanel.add(buildStatPanelContent());
            sidebarStatPanel.revalidate();
            sidebarStatPanel.repaint();
        }
        if (homeStatsRow != null) {
            homeStatsRow.removeAll();
            for (Kereta k : SistemAlokasi.getInstance().getDaftarKereta()) {
                homeStatsRow.add(buildKeretaCard(k));
            }
            homeStatsRow.revalidate();
            homeStatsRow.repaint();
        }
    }

    // ── Content Area ───────────────────────────────────────────────────
    private JPanel buildContentArea() {
        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIConstants.KAI_ABU); 
        contentArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Panel Home / Dashboard
        contentArea.add(buildHomePanel(), "home");

        // Panel Pesan Tiket
        riwayatPanel = new RiwayatPanel();
        PesanTiketPanel pesanPanel = new PesanTiketPanel(() -> {
            riwayatPanel.refresh();
            refreshDashboardStats();
        });
        contentArea.add(pesanPanel, "pesan");

        // Panel Riwayat
        contentArea.add(riwayatPanel, "riwayat");

        // Panel Profil
        contentArea.add(new ProfilPanel(), "profil");

        return contentArea;
    }

    private JPanel buildHomePanel() {
        JPanel home = new JPanel(new BorderLayout(0, 16));
        home.setOpaque(false);

        // 1. Welcome card
        RoundedPanel welcomeCard = new RoundedPanel(14, Color.WHITE, true);
        welcomeCard.setLayout(new BorderLayout()) ;
        welcomeCard.setPreferredSize(new Dimension(0, 120));

        JPanel wcInner = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU,
                                                    getWidth(), 0, new Color(0, 120, 220));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        wcInner.setOpaque(false);
        wcInner.setLayout(new GridBagLayout());
        GridBagConstraints wc = new GridBagConstraints();
        wc.gridx = 0; wc.gridy = 0; wc.anchor = GridBagConstraints.WEST;
        wc.insets = new Insets(0, 24, 4, 24);

        AkunPengguna akun = AuthManager.getInstance().getAkunAktif();
        JLabel wLabel = new JLabel("Selamat Datang" + (akun != null ? ", " + akun.getNamaLengkap() + "!" : "!"));
        wLabel.setFont(UIConstants.FONT_JUDUL);
        wLabel.setForeground(Color.WHITE);
        wcInner.add(wLabel, wc);

        wc.gridy = 1;
        JLabel wSub = new JLabel("Pesan tiket kereta Anda dengan mudah dan cepat bersama KAI.");
        wSub.setFont(UIConstants.FONT_NORMAL);
        wSub.setForeground(new Color(200, 225, 255));
        wcInner.add(wSub, wc);

        welcomeCard.add(wcInner, BorderLayout.CENTER);
        home.add(welcomeCard, BorderLayout.NORTH);

        // 2. Center Area: Stats Cards & Action Button
        JPanel centerPanel = new JPanel(new BorderLayout(0, 16));
        centerPanel.setOpaque(false);

        // Stat cards
        homeStatsRow = new JPanel(new GridLayout(1, 4, 12, 0));
        homeStatsRow.setOpaque(false);
        homeStatsRow.setPreferredSize(new Dimension(0, 100));
        for (Kereta k : SistemAlokasi.getInstance().getDaftarKereta()) {
            homeStatsRow.add(buildKeretaCard(k));
        }
        centerPanel.add(homeStatsRow, BorderLayout.NORTH);

        // TOMBOL MENUJU PESAN TIKET (Diletakkan di atas Rules)
        JPanel actionArea = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionArea.setOpaque(false);
        
        // Menggunakan method BtnPanel yang sudah kamu buat sebelumnya
        RoundedPanel btnPesan = BtnPanel("PESAN TIKET SEKARANG", UIConstants.KAI_EMAS);
        btnPesan.setPreferredSize(new Dimension(280, 45)); // Diperbesar agar menonjol
        actionArea.add(btnPesan);
        
        centerPanel.add(actionArea, BorderLayout.CENTER);
        home.add(centerPanel, BorderLayout.CENTER);

        // 3. Info aturan alokasi (South)
        home.add(buildRulesCard(), BorderLayout.SOUTH);

        return home;
    }

    private RoundedPanel buildKeretaCard(Kereta k) {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;

        JLabel nama = new JLabel(k.getNamaKereta());
        nama.setFont(UIConstants.FONT_BOLD);
        nama.setForeground(UIConstants.KAI_BIRU_GELAP);
        c.gridy = 0; c.insets = new Insets(14, 14, 2, 14);
        card.add(nama, c);

        JLabel rute = new JLabel(k.getRuteAsal() + " → " + k.getRuteTujuan());
        rute.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rute.setForeground(UIConstants.KAI_ABU_GELAP);
        c.gridy = 1; c.insets = new Insets(0, 14, 4, 14);
        card.add(rute, c);

        JLabel jadwal = new JLabel("\uD83D\uDD52 " + k.getJadwalBerangkat());
        jadwal.setFont(UIConstants.FONT_KECIL);
        jadwal.setForeground(UIConstants.KAI_ABU_GELAP);
        c.gridy = 2; c.insets = new Insets(0, 14, 4, 14);
        card.add(jadwal, c);

        int kosong   = k.getTotalKursiKosong();
        int kapasitas = k.getTotalKapasitas();
        JLabel kursi = new JLabel(kosong + " / " + kapasitas + " kursi tersedia");
        kursi.setFont(UIConstants.FONT_BOLD);
        kursi.setForeground(kosong > 0 ? UIConstants.KAI_HIJAU : UIConstants.KAI_MERAH);
        c.gridy = 3; c.insets = new Insets(0, 14, 14, 14);
        card.add(kursi, c);

        return card;
    }

    private RoundedPanel BtnPanel(String text, Color color) {
        RoundedPanel btn = new RoundedPanel(8, color, true);
        btn.setLayout(new GridBagLayout()); // Gunakan GridBagLayout agar teks benar-benar di tengah
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setForeground(Color.WHITE);
        btn.add(lbl);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchPanel(1); // Index 1 adalah "Pesan Tiket" sesuai navNames
            }
            @Override public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.brighter());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private RoundedPanel buildRulesCard() {
        RoundedPanel card = new RoundedPanel(12, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(0, 170));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;

        JLabel title = new JLabel("Aturan Alokasi Kursi KAI");
        title.setFont(UIConstants.FONT_BOLD);
        title.setForeground(UIConstants.KAI_BIRU_GELAP);
        c.gridy = 0; c.insets = new Insets(14, 20, 8, 20);
        card.add(title, c);

        String[] rules = {
            "Penumpang LANSIA, IBU HAMIL, DISABILITAS, atau SAKIT → Otomatis ke Gerbong PRIORITAS",
            "Penumpang PEREMPUAN NORMAL → Diprioritaskan di Gerbong Wanita, fallback ke Campur",
            " Penumpang LAKI-LAKI NORMAL → Dialokasikan langsung ke Gerbong Reguler Campur",
            "Nomor kursi diberikan otomatis oleh sistem berdasarkan urutan ketersediaan"
        };
        for (int i = 0; i < rules.length; i++) {
            JLabel r = new JLabel(rules[i]);
            r.setFont(UIConstants.FONT_KECIL);
            r.setForeground(new Color(50, 65, 90));
            c.gridy = i + 1; c.insets = new Insets(2, 20, 2, 20);
            card.add(r, c);
        }
        c.gridy = rules.length + 1; c.insets = new Insets(0, 0, 10, 0);
        card.add(new JLabel(" "), c);

        return card;
    }

    // ── Navigation ─────────────────────────────────────────────────────
    private void switchPanel(int idx) {
        cardLayout.show(contentArea, panelKeys[idx]);

        // Update tampilan tombol sidebar
        for (int i = 0; i < navButtons.length; i++) {
            navButtons[i].getModel().setSelected(i == idx);
            navButtons[i].setForeground(i == idx ? Color.WHITE : new Color(180, 210, 255));
            navButtons[i].setFont(i == idx ? UIConstants.FONT_BOLD : UIConstants.FONT_NORMAL);
        }

        // LOGIKA ABSTRAKSI: Cari panel yang aktif sekarang
        for (Component comp : contentArea.getComponents()) {
            // Jika panel sedang terlihat dan dia adalah "Refreshable"
            if (comp.isVisible() && comp instanceof RefreshablePanel) {
                ((RefreshablePanel) comp).refreshData();
            }
        }
    }

    // ── Logout ─────────────────────────────────────────────────────────
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin keluar dari akun?", "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            AuthManager.getInstance().logout();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
