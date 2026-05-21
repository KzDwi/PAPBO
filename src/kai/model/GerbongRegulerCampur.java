package kai.model;

import java.awt.Color;

/**
 * PILAR OOP: INHERITANCE + POLIMORFISME
 * Gerbong reguler campur untuk semua gender dengan status normal.
 * Meng-override isCocokUntuk() untuk menerima semua penumpang normal.
 */
public class GerbongRegulerCampur extends Gerbong {

    // ── Constructor ────────────────────────────────────────────────────
    public GerbongRegulerCampur(String kodeGerbong) {
        super(kodeGerbong, "REGULER_CAMPUR",
              "Penumpang Umum Laki-laki & Perempuan (Status Normal)");
    }

    // ── Override isCocokUntuk() (POLIMORFISME) ─────────────────────────
    /**
     * Aturan gerbong campur:
     * - Menerima semua gender
     * - Status kesehatan harus NORMAL (bukan prioritas)
     */
    @Override
    public boolean isCocokUntuk(Penumpang p) {
        return !p.getStatusKesehatan().isMembutuhkanPrioritas();
    }

    @Override
    public Color getWarnaTema() {
        // Biru KAI — gerbong reguler umum
        return new Color(0, 82, 165);
    }

    @Override
    public String toString() {
        return "⊕ " + super.toString();
    }
}
