package kai.model;

/**
 * PILAR OOP: ENKAPSULASI
 * Merepresentasikan satu kursi tunggal di dalam gerbong kereta.
 */
public class Kursi {

    // ── Atribut Private ────────────────────────────────────────────────
    private String    nomorKursi;
    private boolean   isTerisi;
    private Penumpang penumpang;   // null jika kosong

    // ── Constructor ────────────────────────────────────────────────────
    public Kursi(String nomorKursi) {
        this.nomorKursi = nomorKursi;
        this.isTerisi   = false;
        this.penumpang  = null;
    }

    // ── Methods Utama ──────────────────────────────────────────────────

    /** @return true jika kursi masih kosong */
    public boolean cekKetersediaan() {
        return !isTerisi;
    }

    /**
     * Mengisi kursi dengan data penumpang.
     * @param p objek Penumpang
     * @throws IllegalStateException jika kursi sudah terisi
     */
    public void tempatiKursi(Penumpang p) {
        if (isTerisi) {
            throw new IllegalStateException("Kursi " + nomorKursi + " sudah terisi!");
        }
        this.penumpang = p;
        this.isTerisi  = true;
    }

    /** Kosongkan kursi (batalkan pemesanan) */
    public void kosongkanKursi() {
        this.penumpang = null;
        this.isTerisi  = false;
    }

    // ── Getter ─────────────────────────────────────────────────────────
    public String    getNomorKursi() { return nomorKursi; }
    public boolean   isTerisi()      { return isTerisi; }
    public Penumpang getPenumpang()  { return penumpang; }

    @Override
    public String toString() {
        return "Kursi[" + nomorKursi + "] - " + (isTerisi ? "Terisi" : "Kosong");
    }
}
