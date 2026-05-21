package kai.model;

/**
 * PILAR OOP: ENKAPSULASI
 * Menyimpan informasi penumpang yang akan memesan tiket.
 * Semua atribut private, akses hanya lewat getter/setter.
 */
public class Penumpang {

    // ── Atribut Private ────────────────────────────────────────────────
    private String          idKtp;
    private String          nama;
    private int             umur;
    private JenisKelamin    gender;
    private StatusKesehatan statusKesehatan;

    // ── Constructor ────────────────────────────────────────────────────
    public Penumpang(String idKtp, String nama, int umur,
                     JenisKelamin gender, StatusKesehatan statusKesehatan) {
        this.idKtp           = idKtp;
        this.nama            = nama;
        this.umur            = umur;
        this.gender          = gender;
        this.statusKesehatan = statusKesehatan;
    }

    // ── Getter & Setter ────────────────────────────────────────────────
    public String          getIdKtp()                        { return idKtp; }
    public void            setIdKtp(String idKtp)           { this.idKtp = idKtp; }

    public String          getNama()                         { return nama; }
    public void            setNama(String nama)              { this.nama = nama; }

    public int             getUmur()                         { return umur; }
    public void            setUmur(int umur)                 { this.umur = umur; }

    public JenisKelamin    getGender()                       { return gender; }
    public void            setGender(JenisKelamin gender)    { this.gender = gender; }

    public StatusKesehatan getStatusKesehatan()                          { return statusKesehatan; }
    public void            setStatusKesehatan(StatusKesehatan status)   { this.statusKesehatan = status; }

    @Override
    public String toString() {
        return nama + " | " + gender.getDisplay() +
               " | Usia: " + umur + " th | " + statusKesehatan.getDisplay();
    }
}
