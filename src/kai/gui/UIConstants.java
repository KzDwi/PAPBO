package kai.gui;

import java.awt.*;

/**
 * Konstanta UI untuk tampilan KAI yang konsisten di seluruh aplikasi.
 */
public class UIConstants {

    // ── Warna Brand KAI ────────────────────────────────────────────────
    public static final Color KAI_BIRU         = new Color(0, 82, 165);
    public static final Color KAI_BIRU_GELAP   = new Color(0, 56, 117);
    public static final Color KAI_BIRU_MUDA    = new Color(220, 234, 255);
    public static final Color KAI_MERAH        = new Color(196, 30, 58);
    public static final Color KAI_MERAH_MUDA   = new Color(255, 230, 235);
    public static final Color KAI_EMAS         = new Color(212, 160, 23);
    public static final Color KAI_PUTIH        = Color.WHITE;
    public static final Color KAI_ABU          = new Color(245, 247, 250);
    public static final Color KAI_ABU_GELAP    = new Color(100, 110, 125);
    public static final Color KAI_HIJAU        = new Color(39, 174, 96);
    public static final Color KAI_UNGU         = new Color(150, 60, 180);
    public static final Color KAI_BORDER       = new Color(200, 210, 225);

    // ── Font ──────────────────────────────────────────────────────────
    public static final Font FONT_JUDUL     = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font FONT_SUBJUDUL  = new Font("Segoe UI", Font.BOLD,  18);
    public static final Font FONT_NORMAL    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_KECIL     = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BOLD      = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_LABEL     = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_MONO      = new Font("Consolas",  Font.PLAIN, 13);

    // ── Dimensi ───────────────────────────────────────────────────────
    public static final int RADIUS_SUDUT = 12;
    public static final Insets PADDING_CARD = new Insets(20, 24, 20, 24);

    private UIConstants() {}
}
