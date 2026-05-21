package kai.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PILAR OOP: ABSTRAKSI + INHERITANCE
 * Abstract class Gerbong menjadi "cetak biru" untuk semua jenis gerbong.
 * Method isCocokUntuk() bersifat abstract → wajib diimplementasi subclass.
 * Subclass: GerbongPrioritas, GerbongRegulerWanita, GerbongRegulerCampur.
 */
public abstract class Gerbong {

    // ── Atribut Protected (bisa diakses subclass) ──────────────────────
    protected String     kodeGerbong;
    protected String     tipeGerbong;
    protected List<Kursi> daftarKursi;
    protected String     deskripsi;

    // ── Constructor ────────────────────────────────────────────────────
    public Gerbong(String kodeGerbong, String tipeGerbong, String deskripsi) {
        this.kodeGerbong = kodeGerbong;
        this.tipeGerbong = tipeGerbong;
        this.deskripsi   = deskripsi;
        this.daftarKursi = new ArrayList<>();
    }

    // ── Method Konkret (Warisan ke subclass) ───────────────────────────

    /** Tambahkan objek Kursi ke gerbong ini */
    public void tambahKursi(Kursi k) {
        daftarKursi.add(k);
    }

    /** Generate kursi otomatis sejumlah kapasitas */
    public void generateKursi(int kapasitas) {
        String[] kolom = {"A", "B", "C", "D"};
        int baris = (int) Math.ceil((double) kapasitas / kolom.length);
        int counter = 0;
        for (int b = 1; b <= baris && counter < kapasitas; b++) {
            for (String k : kolom) {
                if (counter >= kapasitas) break;
                daftarKursi.add(new Kursi(b + k));
                counter++;
            }
        }
    }

    /**
     * Mencari satu kursi yang masih kosong.
     * @return objek Kursi kosong pertama, atau null jika penuh
     */
    public Kursi getKursiKosong() {
        for (Kursi k : daftarKursi) {
            if (k.cekKetersediaan()) return k;
        }
        return null;
    }

    /** Hitung jumlah kursi kosong */
    public int hitungKursiKosong() {
        return (int) daftarKursi.stream().filter(Kursi::cekKetersediaan).count();
    }

    /** Hitung jumlah kursi terisi */
    public int hitungKursiTerisi() {
        return (int) daftarKursi.stream().filter(k -> !k.cekKetersediaan()).count();
    }

    // ── Method ABSTRACT (Wajib diimplementasi subclass) ────────────────
    /**
     * PILAR: ABSTRAKSI + POLIMORFISME
     * Setiap jenis gerbong punya aturan penerimaan penumpang berbeda.
     * Implementasinya berbeda-beda di tiap subclass.
     */
    public abstract boolean isCocokUntuk(Penumpang p);

    /** Mendapatkan warna tema gerbong untuk GUI */
    public abstract java.awt.Color getWarnaTema();

    // ── Getter ─────────────────────────────────────────────────────────
    public String       getKodeGerbong() { return kodeGerbong; }
    public String       getTipeGerbong() { return tipeGerbong; }
    public String       getDeskripsi()   { return deskripsi; }
    public List<Kursi>  getDaftarKursi() { return daftarKursi; }
    public int          getKapasitas()   { return daftarKursi.size(); }

    @Override
    public String toString() {
        return kodeGerbong + " [" + tipeGerbong + "] " +
               hitungKursiKosong() + "/" + getKapasitas() + " kosong";
    }
}
