package kai.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * PILAR OOP: ENKAPSULASI
 * Menyimpan hasil akhir pemesanan tiket (data tiket).
 */
public class HasilPemesanan {

    private final String    kodeBooking;
    private final Penumpang penumpang;
    private final Kereta    kereta;
    private final Gerbong   gerbong;
    private final Kursi     kursi;
    private final LocalDateTime waktuPesan;
    private final boolean   berhasil;
    private final String    pesan;

    // ── Constructor Berhasil ───────────────────────────────────────────
    public HasilPemesanan(Penumpang penumpang, Kereta kereta,
                          Gerbong gerbong, Kursi kursi) {
        this.kodeBooking = "KAI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.penumpang   = penumpang;
        this.kereta      = kereta;
        this.gerbong     = gerbong;
        this.kursi       = kursi;
        this.waktuPesan  = LocalDateTime.now();
        this.berhasil    = true;
        this.pesan       = "Pemesanan berhasil!";
    }

    // ── Constructor Gagal ──────────────────────────────────────────────
    public HasilPemesanan(Penumpang penumpang, String pesanGagal) {
        this.kodeBooking = "-";
        this.penumpang   = penumpang;
        this.kereta      = null;
        this.gerbong     = null;
        this.kursi       = null;
        this.waktuPesan  = LocalDateTime.now();
        this.berhasil    = false;
        this.pesan       = pesanGagal;
    }

    // ── Getter ─────────────────────────────────────────────────────────
    public String        getKodeBooking() { return kodeBooking; }
    public Penumpang     getPenumpang()   { return penumpang; }
    public Kereta        getKereta()      { return kereta; }
    public Gerbong       getGerbong()     { return gerbong; }
    public Kursi         getKursi()       { return kursi; }
    public boolean       isBerhasil()     { return berhasil; }
    public String        getPesan()       { return pesan; }

    public String getWaktuPesanFormatted() {
        return waktuPesan.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss"));
    }

    /** Ringkasan tiket untuk dicetak */
    public String getRingkasan() {
        if (!berhasil) return "GAGAL: " + pesan;
        return String.format(
            "Kode Booking : %s%n" +
            "Penumpang    : %s%n" +
            "Kereta       : %s%n" +
            "Rute         : %s → %s%n" +
            "Jadwal       : %s%n" +
            "Gerbong      : %s (%s)%n" +
            "Nomor Kursi  : %s%n" +
            "Waktu Pesan  : %s",
            kodeBooking,
            penumpang.getNama(),
            kereta.getNamaKereta(),
            kereta.getRuteAsal(), kereta.getRuteTujuan(),
            kereta.getJadwalBerangkat(),
            gerbong.getKodeGerbong(), gerbong.getTipeGerbong(),
            kursi.getNomorKursi(),
            getWaktuPesanFormatted()
        );
    }
}
