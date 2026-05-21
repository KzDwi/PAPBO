package kai.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import kai.controller.SistemAlokasi;
import kai.model.HasilPemesanan;

/**
 * Panel untuk menampilkan riwayat semua pemesanan tiket dalam sesi ini.
 */
public class RiwayatPanel extends JPanel implements RefreshablePanel {

    private JTable         tabel;
    private DefaultTableModel tableModel;

    public RiwayatPanel() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
        build();
    }

    private void build() {
        // Header
        RoundedPanel headerCard = new RoundedPanel(14, Color.WHITE, true);
        headerCard.setLayout(new BorderLayout());
        headerCard.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel judul = new JLabel("Riwayat Pemesanan Sesi Ini");
        judul.setFont(UIConstants.FONT_SUBJUDUL);
        judul.setForeground(UIConstants.KAI_BIRU_GELAP);
        headerCard.add(judul, BorderLayout.WEST);

        StyledButton btnRefresh = new StyledButton("Refresh", UIConstants.KAI_BIRU);
        btnRefresh.setPreferredSize(new Dimension(100, 34));
        btnRefresh.addActionListener(e -> refresh());
        headerCard.add(btnRefresh, BorderLayout.EAST);
        add(headerCard, BorderLayout.NORTH);

        // Tabel
        String[] headers = {"Kode Booking", "Penumpang", "Kereta", "Gerbong", "Kursi",
                            "Status", "Waktu Pesan"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabel = new JTable(tableModel);
        tabel.setFont(UIConstants.FONT_NORMAL);
        tabel.setRowHeight(36);
        tabel.setShowHorizontalLines(true);
        tabel.setGridColor(UIConstants.KAI_BORDER);
        tabel.setSelectionBackground(UIConstants.KAI_BIRU_MUDA);
        tabel.setSelectionForeground(UIConstants.KAI_BIRU_GELAP);
        tabel.setFillsViewportHeight(true);

        // Header tabel styling
        JTableHeader th = tabel.getTableHeader();
        th.setFont(UIConstants.FONT_BOLD);
        th.setBackground(UIConstants.KAI_BIRU);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(0, 38));
        th.setReorderingAllowed(false);

        // Custom renderer untuk warna baris & status
        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel,
                    boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 250, 255));
                    setForeground(new Color(30, 40, 60));
                }
                if (col == 5) { // Status kolom
                    if (val != null && val.toString().equals("Berhasil")) {
                        setForeground(UIConstants.KAI_HIJAU);
                        setFont(UIConstants.FONT_BOLD);
                    } else {
                        setForeground(UIConstants.KAI_MERAH);
                    }
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        // Lebar kolom
        int[] colWidths = {130, 160, 160, 100, 70, 80, 160};
        for (int i = 0; i < colWidths.length; i++) {
            tabel.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }

        RoundedPanel tableCard = new RoundedPanel(14, Color.WHITE, true);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        tableCard.add(new JScrollPane(tabel));
        add(tableCard, BorderLayout.CENTER);

        refresh();
    }

    /** Memuat ulang data dari SistemAlokasi */
    public void refresh() {
        tableModel.setRowCount(0);
        List<HasilPemesanan> riwayat = SistemAlokasi.getInstance().getRiwayatPemesanan();

        if (riwayat.isEmpty()) {
            tableModel.addRow(new Object[]{
                "-", "Belum ada pemesanan", "-", "-", "-", "-", "-"
            });
            return;
        }

        for (HasilPemesanan h : riwayat) {
            tableModel.addRow(new Object[]{
                h.getKodeBooking(),
                h.getPenumpang().getNama(),
                h.isBerhasil() ? h.getKereta().getNamaKereta() : "-",
                h.isBerhasil() ? h.getGerbong().getKodeGerbong() : "-",
                h.isBerhasil() ? h.getKursi().getNomorKursi() : "-",
                h.isBerhasil() ? "Berhasil" : "Gagal",
                h.getWaktuPesanFormatted()
            });
        }
    }
    @Override
    public void refreshData() {
        this.refresh(); // Memanggil method refresh() yang sudah kamu punya
    }
}
