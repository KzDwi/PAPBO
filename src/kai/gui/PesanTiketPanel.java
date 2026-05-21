package kai.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import kai.controller.AuthManager;
import kai.controller.SistemAlokasi;
import kai.model.AkunPengguna;
import kai.model.Gerbong;
import kai.model.HasilPemesanan;
import kai.model.JenisKelamin;
import kai.model.Kereta;
import kai.model.Penumpang;
import kai.model.StatusKesehatan;

/**
 * Panel utama untuk memesan tiket kereta.
 * Menampilkan form input penumpang, pilih kereta, dan visualisasi kursi.
 */
public class PesanTiketPanel extends JPanel {

    // ── Form fields ────────────────────────────────────────────────────
    private JComboBox<Kereta>         cmbKereta;
    private JComboBox<StatusKesehatan> cmbStatus;
    private JComboBox<JenisKelamin>   cmbGender;
    private StyledTextField           txtNama;
    private JSpinner                  spnUmur;
    private JLabel                    lblNIK;

    // ── Visualisasi ────────────────────────────────────────────────────
    private SeatMapPanel              seatMap;
    private JLabel                    lblInfoGerbong;
    private JLabel                    lblSisaKursi;
    private JTextArea                 taRuleInfo;

    // ── Callback ke parent ─────────────────────────────────────────────
    private final Runnable onPesanBerhasil;

    public PesanTiketPanel(Runnable onPesanBerhasil) {
        this.onPesanBerhasil = onPesanBerhasil;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        build();
    }

    private void build() {
        // ── Kiri: Form input ──────────────────────────────────────────
        JPanel formPanel = buildFormPanel();
        // ── Kanan: Visualisasi gerbong ────────────────────────────────
        JPanel visPanel  = buildVisualisasiPanel();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, visPanel);
        split.setDividerLocation(340);
        split.setBorder(null);
        split.setOpaque(false);
        split.setDividerSize(6);
        add(split, BorderLayout.CENTER);

        updateVisualisasi();
    }

    // ── Panel Form Kiri ────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        RoundedPanel panel = new RoundedPanel(14, Color.WHITE, true);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1;

        int row = 0;

        // Judul
        JLabel judul = new JLabel("Form Pemesanan Tiket");
        judul.setFont(UIConstants.FONT_SUBJUDUL);
        judul.setForeground(UIConstants.KAI_BIRU_GELAP);
        gbc.gridy = row++; gbc.insets = new Insets(18, 20, 4, 20);
        panel.add(judul, gbc);

        // NIK penumpang (dari akun aktif)
        AkunPengguna akun = AuthManager.getInstance().getAkunAktif();
        String nikTampil  = akun != null ? akun.getNik() : "-";
        JPanel nikRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nikRow.setOpaque(false);
        JLabel nikBadge = new JLabel("NIK: " + nikTampil);
        nikBadge.setFont(UIConstants.FONT_MONO);
        nikBadge.setForeground(UIConstants.KAI_BIRU);
        nikBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIConstants.KAI_BIRU_MUDA, 1),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        nikRow.add(nikBadge);
        lblNIK = nikBadge;
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 14, 20);
        panel.add(nikRow, gbc);

        // Pilih Kereta
        addFieldLabel(panel, gbc, row++, "Pilih Kereta");
        cmbKereta = new JComboBox<>();
        SistemAlokasi.getInstance().getDaftarKereta()
                     .forEach(k -> cmbKereta.addItem(k));
        cmbKereta.setFont(UIConstants.FONT_NORMAL);
        cmbKereta.setBackground(Color.WHITE);
        cmbKereta.addActionListener(e -> updateVisualisasi());
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 10, 20);
        panel.add(cmbKereta, gbc);

        // Nama penumpang
        addFieldLabel(panel, gbc, row++, "Nama Penumpang");
        txtNama = new StyledTextField("Nama sesuai KTP");
        if (akun != null) txtNama.setText(akun.getNamaLengkap());
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 10, 20);
        panel.add(txtNama, gbc);

        // Jenis Kelamin
        addFieldLabel(panel, gbc, row++, "Jenis Kelamin");
        cmbGender = new JComboBox<>(JenisKelamin.values());
        if (akun != null) cmbGender.setSelectedItem(akun.getGender());
        cmbGender.setFont(UIConstants.FONT_NORMAL);
        cmbGender.setBackground(Color.WHITE);
        cmbGender.addActionListener(e -> updateRuleInfo());
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 10, 20);
        panel.add(cmbGender, gbc);

        // Umur
        addFieldLabel(panel, gbc, row++, "Umur");
        SpinnerNumberModel spinModel = new SpinnerNumberModel(
            akun != null ? akun.getUmur() : 25, 0, 120, 1);
        spnUmur = new JSpinner(spinModel);
        spnUmur.setFont(UIConstants.FONT_NORMAL);
        spnUmur.setPreferredSize(new Dimension(200, 38));
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 10, 20);
        panel.add(spnUmur, gbc);

        // Status Kesehatan
        addFieldLabel(panel, gbc, row++, "Status Kesehatan");
        cmbStatus = new JComboBox<>(StatusKesehatan.values());
        cmbStatus.setFont(UIConstants.FONT_NORMAL);
        cmbStatus.setBackground(Color.WHITE);
        cmbStatus.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int idx, boolean sel, boolean focus) {
                super.getListCellRendererComponent(list, value, idx, sel, focus);
                if (value instanceof StatusKesehatan) {
                    StatusKesehatan sk = (StatusKesehatan) value;
                    setText(sk.getDisplay() + " — " + sk.getKeterangan());
                }
                return this;
            }
        });
        cmbStatus.addActionListener(e -> updateRuleInfo());
        gbc.gridy = row++; gbc.insets = new Insets(2, 20, 10, 20);
        panel.add(cmbStatus, gbc);

        // Rule info
        taRuleInfo = new JTextArea(3, 20);
        taRuleInfo.setFont(UIConstants.FONT_KECIL);
        taRuleInfo.setEditable(false);
        taRuleInfo.setLineWrap(true);
        taRuleInfo.setWrapStyleWord(true);
        taRuleInfo.setBackground(UIConstants.KAI_BIRU_MUDA);
        taRuleInfo.setForeground(UIConstants.KAI_BIRU_GELAP);
        taRuleInfo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        gbc.gridy = row++; gbc.insets = new Insets(0, 20, 12, 20);
        panel.add(taRuleInfo, gbc);

        // Tombol Pesan
        final StyledButton btnPesan = new StyledButton("PESAN TIKET SEKARANG", UIConstants.KAI_MERAH);
        btnPesan.setPreferredSize(new Dimension(300, 46));

        // 2. Tambahkan Listener pada cmbStatus agar tombol update secara real-time
        cmbStatus.addActionListener(e -> {
            updateRuleInfo(); // Tetap panggil info rule
            updateTombolPesan(btnPesan); // Cek status tombol di sini!
        });

        // 3. Set status awal tombol saat form pertama kali muncul
        updateTombolPesan(btnPesan); 

        gbc.gridy = row; gbc.insets = new Insets(4, 20, 20, 20);
        panel.add(btnPesan, gbc);
        btnPesan.addActionListener(e -> prosesPemesanan());

        return panel;
    }

    // ── Panel Visualisasi Kanan ────────────────────────────────────────
    private JPanel buildVisualisasiPanel() {
        RoundedPanel panel = new RoundedPanel(14, Color.WHITE, true);
        panel.setLayout(new BorderLayout(0, 8));

        JPanel top = new JPanel(new GridLayout(3, 1, 0, 4));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));

        JLabel judul = new JLabel("Peta Kursi Gerbong");
        judul.setFont(UIConstants.FONT_SUBJUDUL);
        judul.setForeground(UIConstants.KAI_BIRU_GELAP);
        top.add(judul);

        lblInfoGerbong = new JLabel("Pilih kereta untuk melihat gerbong");
        lblInfoGerbong.setFont(UIConstants.FONT_BOLD);
        lblInfoGerbong.setForeground(UIConstants.KAI_ABU_GELAP);
        top.add(lblInfoGerbong);

        lblSisaKursi = new JLabel(" ");
        lblSisaKursi.setFont(UIConstants.FONT_KECIL);
        lblSisaKursi.setForeground(UIConstants.KAI_HIJAU);
        top.add(lblSisaKursi);

        panel.add(top, BorderLayout.NORTH);

        // Tab gerbong
        JTabbedPane tabGerbong = new JTabbedPane();
        tabGerbong.setFont(UIConstants.FONT_NORMAL);
        tabGerbong.setBackground(Color.WHITE);
        panel.add(tabGerbong, BorderLayout.CENTER);

        // Rebuild tab saat ganti kereta
        cmbKereta.addActionListener(e -> rebuildTabs(tabGerbong));

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        legendPanel.setOpaque(false);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 8, 12));
        addLegendItem(legendPanel, new Color(60, 180, 100), "Kosong");
        addLegendItem(legendPanel, new Color(220, 80, 80),  "Terisi");
        // addLegendItem(legendPanel, UIConstants.KAI_EMAS,    "Akan dipilih");
        panel.add(legendPanel, BorderLayout.SOUTH);

        rebuildTabs(tabGerbong);
        return panel;
    }
    
    private void updateTombolPesan(JButton btnPesan) {
        StatusKesehatan status = (StatusKesehatan) cmbStatus.getSelectedItem();
        // Tombol aktif HANYA JIKA yang dipilih BUKAN "PILIH"
        btnPesan.setEnabled(status != null && status != StatusKesehatan.PILIH);
    }

    private void addLegendItem(JPanel p, Color c, String label) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        item.setOpaque(false);
        JPanel box = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(c);
                g.fillRoundRect(0, 0, 12, 12, 3, 3);
            }
        };
        box.setOpaque(false);
        box.setPreferredSize(new Dimension(12, 12));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIConstants.FONT_KECIL);
        lbl.setForeground(UIConstants.KAI_ABU_GELAP);
        item.add(box); item.add(lbl);
        p.add(item);
    }

    private void rebuildTabs(JTabbedPane tabs) {
        tabs.removeAll();
        Kereta kereta = (Kereta) cmbKereta.getSelectedItem();
        if (kereta == null) return;
        for (Gerbong g : kereta.getDaftarGerbong()) {
            SeatMapPanel sm = new SeatMapPanel();
            sm.setGerbong(g);
            JScrollPane scroll = new JScrollPane(sm);
            scroll.setBorder(null);
            String tabTitle = g.getKodeGerbong() + " (" + g.hitungKursiKosong() + ")";
            tabs.addTab(tabTitle, scroll);
        }
        updateVisualisasi();
    }

    private void updateVisualisasi() {
        Kereta k = (Kereta) cmbKereta.getSelectedItem();
        if (k == null) return;
        lblInfoGerbong.setText(k.getNamaKereta() + " · " + k.getRuteAsal() + " → " + k.getRuteTujuan());
        lblSisaKursi.setText("Sisa kursi: " + k.getTotalKursiKosong() + " / " + k.getTotalKapasitas() +
                             " | Berangkat: " + k.getJadwalBerangkat());
    }

    private void updateRuleInfo() {
        JenisKelamin   gender = (JenisKelamin)   cmbGender.getSelectedItem();
        StatusKesehatan status = (StatusKesehatan) cmbStatus.getSelectedItem();
        if (gender == null || status == null) return;

        String info;
        if (status.isMembutuhkanPrioritas()) {
            info = "\u2605 Status " + status.getDisplay() + " → Anda akan dialokasikan ke " +
                   "GERBONG PRIORITAS (Gerbong 1). Layanan khusus tersedia.";
        } else if (gender == JenisKelamin.PEREMPUAN) {
            info = "\u2640 Penumpang perempuan normal → Diprioritaskan di GERBONG REGULER WANITA. " +
                   "Jika penuh, akan dialihkan ke Gerbong Campur.";
        } else {
            info = "\u2642 Penumpang laki-laki normal → Dialokasikan ke GERBONG REGULER CAMPUR.";
        }
        taRuleInfo.setText(info);
    }

    // ── Proses Pemesanan ───────────────────────────────────────────────
    private void prosesPemesanan() {
        String nama = txtNama.getText().trim();
        
        // 1. Validasi Nama: Tidak boleh kosong
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama penumpang tidak boleh kosong!",
                                        "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Validasi Nama: Tidak boleh ada angka atau karakter spesial
        // Menggunakan Regex: ^[a-zA-Z\\s]+$ (Hanya huruf A-Z dan spasi)
        if (!nama.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, 
                "Nama hanya boleh berisi huruf dan spasi!\n(Tidak boleh ada angka atau karakter spesial)",
                "Gagal Validasi Nama", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validasi Umur: Memastikan umur dalam rentang logika yang benar
        int umur = (Integer) spnUmur.getValue();
        if (umur <= 0 || umur > 120) {
            JOptionPane.showMessageDialog(this, 
                "Masukkan umur yang valid (antara 1 sampai 120 tahun)!",
                "Gagal Validasi Umur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        AkunPengguna akun = AuthManager.getInstance().getAkunAktif();
        String nik = akun != null ? akun.getNik() : "TAMU";

        // Jika lolos validasi, buat objek Penumpang
        Penumpang p = new Penumpang(
            nik, nama,
            umur,
            (JenisKelamin)   cmbGender.getSelectedItem(),
            (StatusKesehatan) cmbStatus.getSelectedItem()
        );

        Kereta kereta = (Kereta) cmbKereta.getSelectedItem();
        if (kereta == null) {
            JOptionPane.showMessageDialog(this, "Pilih kereta terlebih dahulu!");
            return;
        }

        HasilPemesanan hasil = SistemAlokasi.getInstance().prosesPemesanan(kereta, p);

        if (hasil.isBerhasil()) {
            showTiketDialog(hasil);
            if (onPesanBerhasil != null) onPesanBerhasil.run();
        } else {
            JOptionPane.showMessageDialog(this,
                hasil.getPesan(), "Pemesanan Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTiketDialog(HasilPemesanan hasil) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                  "Tiket Berhasil!", true);
        dlg.setSize(480, 420);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.getContentPane().setBackground(UIConstants.KAI_ABU);

        // Header sukses
        JPanel header = new JPanel();
        header.setBackground(UIConstants.KAI_HIJAU);
        header.setPreferredSize(new Dimension(480, 70));
        header.setLayout(new GridBagLayout());
        JLabel hLbl = new JLabel("\u2713  PEMESANAN BERHASIL!");
        hLbl.setFont(UIConstants.FONT_SUBJUDUL);
        hLbl.setForeground(Color.WHITE);
        header.add(hLbl);
        dlg.add(header, BorderLayout.NORTH);

        // Isi tiket
        JTextArea ta = new JTextArea(SistemAlokasi.getInstance().cetakTiket(hasil));
        ta.setFont(UIConstants.FONT_MONO);
        ta.setEditable(false);
        ta.setBackground(new Color(250, 252, 255));
        ta.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        dlg.add(new JScrollPane(ta), BorderLayout.CENTER);

        // Tombol tutup
        StyledButton btnTutup = new StyledButton("TUTUP", UIConstants.KAI_BIRU);
        btnTutup.setPreferredSize(new Dimension(200, 40));
        JPanel bot = new JPanel();
        bot.setBackground(UIConstants.KAI_ABU);
        bot.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        bot.add(btnTutup);
        dlg.add(bot, BorderLayout.SOUTH);
        btnTutup.addActionListener(e -> dlg.dispose());

        dlg.setVisible(true);
    }

    private void addFieldLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_LABEL);
        lbl.setForeground(new Color(50, 65, 90));
        gbc.gridy = row; gbc.insets = new Insets(8, 20, 2, 20);
        p.add(lbl, gbc);
    }
}
