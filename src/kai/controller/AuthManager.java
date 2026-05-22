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
}