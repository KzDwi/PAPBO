package kai.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * =====================================================
 * PROGRAM SISTEM TIKET KAI — ENTRY POINT
 * =====================================================
 *
 * PENERAPAN 4 PILAR OOP:
 *
 * 1. ENKAPSULASI (Encapsulation)
 *    → Semua atribut di class model (Penumpang, Kursi, Gerbong,
 *      Kereta, AkunPengguna) dideklarasikan private.
 *    → Akses hanya melalui getter/setter yang terdefinisi.
 *    → Enum (JenisKelamin, StatusKesehatan) membungkus nilai tetap.
 *
 * 2. ABSTRAKSI (Abstraction)
 *    → Class Gerbong dideklarasikan abstract — tidak bisa diinstansiasi langsung.
 *    → Method isCocokUntuk(Penumpang) bersifat abstract → subclass wajib
 *      mengimplementasikan logika filter mereka sendiri.
 *    → Method getWarnaTema() juga abstract untuk fleksibilitas UI.
 *
 * 3. PEWARISAN (Inheritance)
 *    → GerbongPrioritas     extends Gerbong
 *    → GerbongRegulerWanita extends Gerbong
 *    → GerbongRegulerCampur extends Gerbong
 *    → Semua mewarisi atribut & method dari Gerbong (kode, kursi, dll).
 *
 * 4. POLIMORFISME (Polymorphism)
 *    → Method isCocokUntuk() dioverride di masing-masing subclass gerbong
 *      dengan logika yang berbeda-beda (runtime polymorphism).
 *    → Di SistemAlokasi & Kereta, pemanggilan isCocokUntuk() pada List<Gerbong>
 *      menghasilkan perilaku berbeda sesuai tipe gerbong aktual (dynamic dispatch).
 *    → getWarnaTema() juga polimorfis → tiap gerbong punya warna tema sendiri.
 *
 * Akun Demo untuk Login:
 *   NIK: 3201012501900001  Password: password123
 *   NIK: 3201015505850002  Password: siti123
 * =====================================================
 */
public class MainApp {

    public static void main(String[] args) {
        // Aktifkan rendering halus di semua platform
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Gunakan Look and Feel default OS / Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
        }

        // Override beberapa default Nimbus agar sesuai brand KAI
        UIManager.put("control",              new java.awt.Color(245, 247, 250));
        UIManager.put("nimbusBase",           new java.awt.Color(0, 82, 165));
        UIManager.put("nimbusBlueGrey",       new java.awt.Color(100, 130, 170));
        UIManager.put("nimbusFocus",          new java.awt.Color(0, 82, 165));
        UIManager.put("OptionPane.messageForeground", new java.awt.Color(30, 40, 60));

        // Jalankan GUI di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            SplashScreen splash = new SplashScreen();
            splash.show(() -> {
                new LoginFrame().setVisible(true);
            });
        });
    }
}
