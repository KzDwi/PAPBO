package kai.model;

/**
 * PILAR OOP: ENKAPSULASI
 * Enum untuk membungkus nilai jenis kelamin agar tidak ada typo / nilai sembarangan.
 */
public enum JenisKelamin {
    LAKI_LAKI("Laki-laki"),
    PEREMPUAN("Perempuan");

    private final String display;

    JenisKelamin(String display) { this.display = display; }

    public String getDisplay() { return display; }

    @Override
    public String toString() { return display; }
}
