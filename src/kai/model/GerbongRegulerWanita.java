package kai.model;

import java.awt.Color;

/**
 * PILAR OOP: INHERITANCE + POLIMORFISME
 * Gerbong khusus perempuan normal.
 * Meng-override isCocokUntuk() agar hanya menerima perempuan sehat.
 */
public class GerbongRegulerWanita extends Gerbong {

    // ── Constructor ────────────────────────────────────────────────────
    public GerbongRegulerWanita(String kodeGerbong) {
        super(kodeGerbong, "REGULER_WANITA",
              "Khusus Penumpang Perempuan (Status Normal)");
    }

    // ── Override isCocokUntuk() (POLIMORFISME) ─────────────────────────
    /**
     * Aturan gerbong wanita:
     * - Penumpang harus PEREMPUAN
     * - Status kesehatan harus NORMAL (bukan prioritas)
     */
    @Override
    public boolean isCocokUntuk(Penumpang p) {
        return p.getGender() == JenisKelamin.PEREMPUAN
            && !p.getStatusKesehatan().isMembutuhkanPrioritas();
    }

    @Override
    public Color getWarnaTema() {
        // Ungu muda — identitas gerbong wanita KAI
        return new Color(150, 60, 180);
    }

    @Override
    public String toString() {
        return "♀ " + super.toString();
    }
}
