package kai.controller;

import kai.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PILAR OOP: ENKAPSULASI + POLIMORFISME
 * Controller utama sistem alokasi kursi kereta.
 * Menggunakan Singleton Pattern agar hanya ada satu instance sistem.
 * Polimorfisme: memanggil isCocokUntuk() yang berbeda-beda di tiap gerbong.
 */
public class SistemAlokasi {

    // ── Singleton ──────────────────────────────────────────────────────
    private static SistemAlokasi instance;

    // ── Data Kereta ────────────────────────────────────────────────────
    private final List<Kereta>          daftarKereta;
    private final List<HasilPemesanan>  riwayatPemesanan;

    // ── Constructor ────────────────────────────────────────────────────
    private SistemAlokasi() {
        daftarKereta     = new ArrayList<>();
        riwayatPemesanan = new ArrayList<>();
        inisialisasiKereta();
    }

    public static SistemAlokasi getInstance() {
        if (instance == null) instance = new SistemAlokasi();
        return instance;
    }

    // ── Inisialisasi Data Kereta ───────────────────────────────────────
    private void inisialisasiKereta() {
        // ── Kereta 1: Argo Bromo Anggrek ──────────────────────────────
        Kereta k1 = new Kereta("Argo Bromo Anggrek", "Jakarta Gambir", "Surabaya Pasar Turi", "08:00 WIB");
        k1.tambahGerbong(buatGerbong("G1-ABA", "PRIORITAS", 20));
        k1.tambahGerbong(buatGerbong("G2-ABA", "REGULER_WANITA", 40));
        k1.tambahGerbong(buatGerbong("G3-ABA", "REGULER_CAMPUR", 50));
        k1.tambahGerbong(buatGerbong("G4-ABA", "REGULER_CAMPUR", 50));
        daftarKereta.add(k1);

        // ── Kereta 2: Gajayana ─────────────────────────────────────────
        Kereta k2 = new Kereta("Gajayana", "Jakarta Gambir", "Malang", "16:30 WIB");
        k2.tambahGerbong(buatGerbong("G1-GJY", "PRIORITAS", 20));
        k2.tambahGerbong(buatGerbong("G2-GJY", "REGULER_WANITA", 40));
        k2.tambahGerbong(buatGerbong("G3-GJY", "REGULER_CAMPUR", 50));
        k2.tambahGerbong(buatGerbong("G4-GJY", "REGULER_CAMPUR", 50));
        daftarKereta.add(k2);

        // ── Kereta 3: Argo Lawu ────────────────────────────────────────
        Kereta k3 = new Kereta("Argo Lawu", "Jakarta Gambir", "Solo Balapan", "08:15 WIB");
        k3.tambahGerbong(buatGerbong("G1-ALW", "PRIORITAS", 20));
        k3.tambahGerbong(buatGerbong("G2-ALW", "REGULER_WANITA", 40));
        k3.tambahGerbong(buatGerbong("G3-ALW", "REGULER_CAMPUR", 50));
        daftarKereta.add(k3);

        // ── Kereta 4: Taksaka ──────────────────────────────────────────
        Kereta k4 = new Kereta("Taksaka", "Jakarta Gambir", "Yogyakarta", "10:00 WIB");
        k4.tambahGerbong(buatGerbong("G1-TKS", "PRIORITAS", 20));
        k4.tambahGerbong(buatGerbong("G2-TKS", "REGULER_WANITA", 40));
        k4.tambahGerbong(buatGerbong("G3-TKS", "REGULER_CAMPUR", 50));
        k4.tambahGerbong(buatGerbong("G4-TKS", "REGULER_CAMPUR", 50));
        daftarKereta.add(k4);
    }

    /** Factory method untuk membuat gerbong berdasarkan tipe */
    private Gerbong buatGerbong(String kode, String tipe, int kapasitas) {
        Gerbong g;
        switch (tipe) {
            case "PRIORITAS":
                g = new GerbongPrioritas(kode); break;
            case "REGULER_WANITA":
                g = new GerbongRegulerWanita(kode); break;
            default:
                g = new GerbongRegulerCampur(kode); break;
        }
        g.generateKursi(kapasitas);
        return g;
    }

    // ── Method Utama: Proses Pemesanan ─────────────────────────────────
    /**
     * POLIMORFISME: Memanggil cariGerbongTepat yang di dalamnya
     * memanggil isCocokUntuk() — berbeda-beda di tiap gerbong.
     *
     * @param kereta   objek Kereta yang dipilih pengguna
     * @param penumpang objek Penumpang yang memesan
     * @return HasilPemesanan (berhasil/gagal)
     */
    public HasilPemesanan prosesPemesanan(Kereta kereta, Penumpang penumpang) {
        // Cari gerbong yang cocok
        Gerbong gerbongCocok = kereta.cariGerbongTepat(penumpang);

        if (gerbongCocok == null) {
            return new HasilPemesanan(penumpang,
                "Tidak ada gerbong tersedia untuk profil penumpang ini.");
        }

        // Cari kursi kosong di gerbong tersebut
        Kursi kursiKosong = gerbongCocok.getKursiKosong();
        if (kursiKosong == null) {
            return new HasilPemesanan(penumpang,
                "Semua kursi di gerbong " + gerbongCocok.getKodeGerbong() + " sudah penuh.");
        }

        // Tempati kursi
        kursiKosong.tempatiKursi(penumpang);

        // Buat & simpan hasil pemesanan
        HasilPemesanan hasil = new HasilPemesanan(penumpang, kereta, gerbongCocok, kursiKosong);
        riwayatPemesanan.add(hasil);

        return hasil;
    }

    /**
     * Format ringkasan tiket untuk ditampilkan / dicetak.
     */
    public String cetakTiket(HasilPemesanan hasil) {
        if (!hasil.isBerhasil()) {
            return "=== PEMESANAN GAGAL ===\n" + hasil.getPesan();
        }
        return "========================================\n" +
               "      PT KERETA API INDONESIA (KAI)    \n" +
               "========================================\n" +
               hasil.getRingkasan() + "\n" +
               "========================================\n" +
               "   Tunjukkan tiket ini kepada petugas  \n" +
               "========================================";
    }

    // ── Getter ─────────────────────────────────────────────────────────
    public List<Kereta>         getDaftarKereta()      { return daftarKereta; }
    public List<HasilPemesanan> getRiwayatPemesanan()  { return riwayatPemesanan; }
}
