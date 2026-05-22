package kai.controller;

import kai.model.AkunPengguna;
import kai.model.JenisKelamin;

public interface AuthManager {
    static AuthManager getInstance() {
        return AuthManagerImpl.getInstance();
    }

    boolean login(String nik, String password);
    String registrasi(String nik, String namaLengkap, String password, String email, int umur, JenisKelamin gender, String alamat);
    void logout();
    AkunPengguna getAkunAktif();
    boolean isLoggedIn();
    boolean isNikTerdaftar(String nik);

    /**
     * Memvalidasi data pra-registrasi: kecocokan password dan parsing umur.
     * Mengembalikan pesan error, atau {@code null} jika semua valid.
     * Jika valid, umur yang sudah di-parse dapat diperoleh via {@link #parseUmur(String)}.
     *
     * @param pass     password yang dimasukkan
     * @param pass2    konfirmasi password
     * @param umurStr  umur dalam bentuk String dari field input
     * @return pesan error, atau null jika lolos validasi
     */
    String validasiPraRegistrasi(String pass, String pass2, String umurStr);

    /**
     * Melakukan parsing String umur menjadi int.
     * Dipanggil setelah {@link #validasiPraRegistrasi} lolos.
     *
     * @param umurStr string umur
     * @return nilai int umur
     */
    int parseUmur(String umurStr);
}