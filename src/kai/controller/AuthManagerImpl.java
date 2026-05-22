package kai.controller;

import java.util.HashMap;
import java.util.Map;
import kai.model.AkunPengguna;
import kai.model.JenisKelamin;

public class AuthManagerImpl implements AuthManager {

    // Tipe data instance diganti menjadi AuthManagerImpl
    private static AuthManagerImpl instance;

    private final Map<String, AkunPengguna> daftarAkun;
    private AkunPengguna akunAktif;

    private AuthManagerImpl() {
        daftarAkun = new HashMap<>();
        akunAktif = null;
        seedDemoAkun();
    }

    // Tipe kembalian diganti menjadi AuthManagerImpl agar sesuai dengan interface static call
    public static AuthManagerImpl getInstance() {
        if (instance == null) {
            instance = new AuthManagerImpl();
        }
        return instance;
    }

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

    @Override
    public boolean login(String nik, String password) {
        if (!AkunPengguna.isNikValid(nik)) return false;
        AkunPengguna akun = daftarAkun.get(nik);
        if (akun != null && akun.getPassword().equals(password)) {
            akunAktif = akun;
            return true;
        }
        return false;
    }

    @Override
    public String registrasi(String nik, String namaLengkap, String password,
                              String email, int umur, JenisKelamin gender, String alamat) {
        if (!AkunPengguna.isNikValid(nik)) return "NIK harus 16 digit angka!";
        if (daftarAkun.containsKey(nik)) return "NIK sudah terdaftar! Gunakan NIK lain atau login.";
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) return "Nama lengkap tidak boleh kosong!";
        if (password == null || password.length() < 6) return "Password minimal 6 karakter!";
        if (email == null || !email.contains("@")) return "Format email tidak valid!";
        if (umur < 0 || umur > 120) return "Umur tidak valid!";

        AkunPengguna akun = new AkunPengguna(nik, namaLengkap, password, email, umur, gender, alamat);
        daftarAkun.put(nik, akun);
        return null;
    }

    @Override
    public void logout() {
        akunAktif = null;
    }

    @Override
    public AkunPengguna getAkunAktif() {
        return akunAktif;
    }

    @Override
    public boolean isLoggedIn() {
        return akunAktif != null;
    }

    @Override
    public boolean isNikTerdaftar(String nik) {
        return daftarAkun.containsKey(nik);
    }

    /**
     * Memvalidasi data pra-registrasi yang dikirim GUI sebagai String mentah.
     * Logika ini dipindah dari GUI ke sini agar Controller yang bertanggung jawab
     * atas aturan bisnis, bukan lapisan tampilan.
     *
     * @param pass    password yang dimasukkan
     * @param pass2   konfirmasi password
     * @param umurStr umur dalam String dari field input
     * @return pesan error jika tidak valid, atau {@code null} jika semua lolos
     */
    @Override
    public String validasiPraRegistrasi(String pass, String pass2, String umurStr) {
        // Aturan bisnis: password dan konfirmasi harus cocok
        if (!pass.equals(pass2)) {
            return "Password dan konfirmasi password tidak cocok!";
        }
        // Aturan bisnis: umur harus berupa angka yang bisa di-parse
        try {
            Integer.parseInt(umurStr);
        } catch (NumberFormatException ex) {
            return "Umur harus berupa angka!";
        }
        return null; // semua valid
    }

    /**
     * Melakukan parsing String umur menjadi int.
     * Harus dipanggil setelah {@link #validasiPraRegistrasi} mengembalikan null.
     */
    @Override
    public int parseUmur(String umurStr) {
        return Integer.parseInt(umurStr);
    }
}