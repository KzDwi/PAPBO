package kai.controller;

import java.util.HashMap;
import java.util.Map;

import kai.model.AkunPengguna;
import kai.model.JenisKelamin;

/**
 * PILAR OOP: ENKAPSULASI
 * Controller untuk autentikasi pengguna (Login & Registrasi).
 * Menyimpan data pengguna dalam HashMap (in-memory database).
 */
public class AuthManager {

    // ── Singleton Instance ─────────────────────────────────────────────
    private static AuthManager instance;

    // ── Storage (NIK → AkunPengguna) ──────────────────────────────────
    private final Map<String, AkunPengguna> daftarAkun;
    private AkunPengguna akunAktif;

    // ── Constructor Private (Singleton) ────────────────────────────────
    private AuthManager() {
        daftarAkun  = new HashMap<>();
        akunAktif   = null;
        seedDemoAkun(); // tambah akun demo
    }

    public static AuthManager getInstance() {
        if (instance == null) instance = new AuthManager();
        return instance;
    }

    // ── Seed Data Demo ─────────────────────────────────────────────────
    private void seedDemoAkun() {
        AkunPengguna demo = new AkunPengguna(
            "3201012501900001",
            "Budi Santoso",
            "password123",
            "budi@email.com",
            34,
            JenisKelamin.LAKI_LAKI,
            "Jl. Merdeka No. 1, Jakarta"
        );
        daftarAkun.put(demo.getNik(), demo);

        AkunPengguna demo2 = new AkunPengguna(
            "3201015505850002",
            "Siti Rahayu",
            "siti123",
            "siti@email.com",
            39,
            JenisKelamin.PEREMPUAN,
            "Jl. Sudirman No. 5, Bandung"
        );
        daftarAkun.put(demo2.getNik(), demo2);
    }

    // ── Method Login ───────────────────────────────────────────────────
    /**
     * @param nik      NIK 16 digit
     * @param password password akun
     * @return true jika login berhasil
     */
    public boolean login(String nik, String password) {
        if (!AkunPengguna.isNikValid(nik)) return false;
        AkunPengguna akun = daftarAkun.get(nik);
        if (akun != null && akun.getPassword().equals(password)) {
            akunAktif = akun;
            return true;
        }
        return false;
    }

    // ── Method Registrasi ──────────────────────────────────────────────
    /**
     * Mendaftarkan akun baru.
     * @return null jika berhasil, atau pesan error
     */
    public String registrasi(String nik, String namaLengkap, String password,
                              String email, int umur, JenisKelamin gender, String alamat) {
        if (!AkunPengguna.isNikValid(nik))
            return "NIK harus 16 digit angka!";
        if (daftarAkun.containsKey(nik))
            return "NIK sudah terdaftar! Gunakan NIK lain atau login.";
        if (namaLengkap == null || namaLengkap.trim().isEmpty())
            return "Nama lengkap tidak boleh kosong!";
        if (password == null || password.length() < 6)
            return "Password minimal 6 karakter!";
        if (email == null || !email.contains("@"))
            return "Format email tidak valid!";
        if (umur < 0 || umur > 120)
            return "Umur tidak valid!";

        AkunPengguna akun = new AkunPengguna(nik, namaLengkap, password,
                                              email, umur, gender, alamat);
        daftarAkun.put(nik, akun);
        return null; // sukses
    }

    // ── Method Logout ──────────────────────────────────────────────────
    public void logout() { akunAktif = null; }

    // ── Getter ─────────────────────────────────────────────────────────
    public AkunPengguna getAkunAktif()  { return akunAktif; }
    public boolean isLoggedIn()         { return akunAktif != null; }
    public boolean isNikTerdaftar(String nik) { return daftarAkun.containsKey(nik); }
}
