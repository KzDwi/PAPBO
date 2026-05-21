package kai.model;

/**
 * PILAR OOP: ENKAPSULASI
 * Menyimpan data akun pengguna yang digunakan untuk login & registrasi.
 * NIK = Nomor Induk Kependudukan (16 digit), KTP = scan/id KTP.
 */
public class AkunPengguna {

    // ── Atribut Private (Enkapsulasi) ──────────────────────────────────
    private String nik;          // 16-digit
    private String namaLengkap;
    private String password;
    private String email;
    private int    umur;
    private JenisKelamin gender;
    private String alamat;

    // ── Constructor ────────────────────────────────────────────────────
    public AkunPengguna(String nik, String namaLengkap, String password,
                        String email, int umur, JenisKelamin gender, String alamat) {
        this.nik         = nik;
        this.namaLengkap = namaLengkap;
        this.password    = password;
        this.email       = email;
        this.umur        = umur;
        this.gender      = gender;
        this.alamat      = alamat;
    }

    // ── Getter & Setter (Enkapsulasi) ──────────────────────────────────
    public String      getNik()          { return nik; }
    public void        setNik(String n)  { this.nik = n; }

    public String      getNamaLengkap()          { return namaLengkap; }
    public void        setNamaLengkap(String n)  { this.namaLengkap = n; }

    public String      getPassword()          { return password; }
    public void        setPassword(String p)  { this.password = p; }

    public String      getEmail()             { return email; }
    public void        setEmail(String e)     { this.email = e; }

    public int         getUmur()              { return umur; }
    public void        setUmur(int u)         { this.umur = u; }

    public JenisKelamin getGender()              { return gender; }
    public void         setGender(JenisKelamin g){ this.gender = g; }

    public String      getAlamat()            { return alamat; }
    public void        setAlamat(String a)    { this.alamat = a; }

    // Validasi NIK: harus 16 digit angka
    public static boolean isNikValid(String nik) {
        return nik != null && nik.matches("\\d{16}");
    }

    @Override
    public String toString() {
        return "AkunPengguna{nik='" + nik + "', nama='" + namaLengkap + "'}";
    }
}
