package kai.model;

import java.awt.Color;

/**
 * PILAR OOP: INHERITANCE + POLIMORFISME
 * Gerbong khusus untuk penumpang yang membutuhkan layanan prioritas:
 * Lansia, Ibu Hamil, Disabilitas, dan Sakit.
 * Meng-override isCocokUntuk() sesuai aturan gerbong prioritas.
 */
public class GerbongPrioritas extends Gerbong {

    // ── Constructor ────────────────────────────────────────────────────
    public GerbongPrioritas(String kodeGerbong) {
        super(kodeGerbong, "PRIORITAS",
              "Khusus Lansia, Ibu Hamil, Disabilitas & Penumpang Sakit");
    }

    // ── Override isCocokUntuk() (POLIMORFISME) ─────────────────────────
    /**
     * Gerbong ini HANYA untuk penumpang yang butuh prioritas.
     * Gender tidak diperhatikan — semua gender boleh masuk.
     */
    @Override
    public boolean isCocokUntuk(Penumpang p) {
        return p.getStatusKesehatan().isMembutuhkanPrioritas();
    }

    @Override
    public Color getWarnaTema() {
        // Merah — simbol urgensi / prioritas
        return new Color(196, 30, 58);
    }

    @Override
    public String toString() {
        return "★ " + super.toString();
    }
}
