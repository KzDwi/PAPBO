package kai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PILAR OOP: ENKAPSULASI
 * Merepresentasikan satu rangkaian kereta dengan beberapa gerbong.
 */
public class Kereta {

    // ── Atribut Private ────────────────────────────────────────────────
    private String         namaKereta;
    private String         ruteAsal;
    private String         ruteTujuan;
    private String         jadwalBerangkat;
    private List<Gerbong>  daftarGerbong;

    // ── Constructor ────────────────────────────────────────────────────
    public Kereta(String namaKereta, String ruteAsal, String ruteTujuan, String jadwalBerangkat) {
        this.namaKereta      = namaKereta;
        this.ruteAsal        = ruteAsal;
        this.ruteTujuan      = ruteTujuan;
        this.jadwalBerangkat = jadwalBerangkat;
        this.daftarGerbong   = new ArrayList<>();
    }

    // ── Methods ────────────────────────────────────────────────────────

    /** Menambahkan gerbong ke rangkaian kereta */
    public void tambahGerbong(Gerbong g) {
        daftarGerbong.add(g);
    }

    /**
     * Mencari gerbong yang cocok untuk penumpang berdasarkan profil.
     * Menggunakan POLIMORFISME — memanggil isCocokUntuk() masing-masing gerbong.
     * Prioritas: PRIORITAS → REGULER_WANITA → REGULER_CAMPUR
     *
     * @param p objek Penumpang
     * @return Gerbong yang cocok dan masih ada kursi kosong, atau null
     */
    public Gerbong cariGerbongTepat(Penumpang p) {
        // Rule 1: Penumpang prioritas → cari gerbong PRIORITAS
        if (p.getStatusKesehatan().isMembutuhkanPrioritas()) {
            for (Gerbong g : daftarGerbong) {
                if (g.isCocokUntuk(p) && g.getKursiKosong() != null) {
                    return g;
                }
            }
        }

        // Rule 2: Perempuan normal → cari REGULER_WANITA dulu, fallback CAMPUR
        if (p.getGender() == JenisKelamin.PEREMPUAN &&
                !p.getStatusKesehatan().isMembutuhkanPrioritas()) {
            for (Gerbong g : daftarGerbong) {
                if ("REGULER_WANITA".equals(g.getTipeGerbong()) &&
                        g.getKursiKosong() != null) {
                    return g;
                }
            }
            // Fallback ke REGULER_CAMPUR jika wanita penuh
            for (Gerbong g : daftarGerbong) {
                if ("REGULER_CAMPUR".equals(g.getTipeGerbong()) &&
                        g.getKursiKosong() != null) {
                    return g;
                }
            }
        }

        // Rule 3: Laki-laki normal → REGULER_CAMPUR langsung
        if (p.getGender() == JenisKelamin.LAKI_LAKI &&
                !p.getStatusKesehatan().isMembutuhkanPrioritas()) {
            for (Gerbong g : daftarGerbong) {
                if ("REGULER_CAMPUR".equals(g.getTipeGerbong()) &&
                        g.getKursiKosong() != null) {
                    return g;
                }
            }
        }

        return null; // Semua penuh atau tidak ada gerbong cocok
    }

    // ── Getter ─────────────────────────────────────────────────────────
    public String        getNamaKereta()      { return namaKereta; }
    public String        getRuteAsal()        { return ruteAsal; }
    public String        getRuteTujuan()      { return ruteTujuan; }
    public String        getJadwalBerangkat() { return jadwalBerangkat; }
    public List<Gerbong> getDaftarGerbong()   { return daftarGerbong; }

    public int getTotalKursiKosong() {
        return daftarGerbong.stream().mapToInt(Gerbong::hitungKursiKosong).sum();
    }

    public int getTotalKapasitas() {
        return daftarGerbong.stream().mapToInt(Gerbong::getKapasitas).sum();
    }

    @Override
    public String toString() {
        return namaKereta + " | " + ruteAsal + " → " + ruteTujuan +
               " | " + jadwalBerangkat + " | Sisa: " +
               getTotalKursiKosong() + "/" + getTotalKapasitas();
    }
}
