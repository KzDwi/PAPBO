package kai.model;

/**
 * PILAR OOP: ENKAPSULASI
 * Enum untuk status kesehatan penumpang yang menentukan alokasi gerbong.
 */
public enum StatusKesehatan {
    PILIH       ("Pilih",       "Pilih status kesehatan Anda",       false),
    NORMAL      ("Normal",      "Penumpang umum sehat",              false),
    LANSIA      ("Lansia",      "Usia 60 tahun ke atas",             true),
    IBU_HAMIL   ("Ibu Hamil",   "Perempuan dalam masa kehamilan",    true),
    DISABILITAS ("Disabilitas", "Penyandang disabilitas fisik/mental",true),
    SAKIT       ("Sakit",       "Penumpang dalam kondisi sakit",     true);

    private final String display;
    private final String keterangan;
    private final boolean butuhPrioritas;

    StatusKesehatan(String display, String keterangan, boolean butuhPrioritas) {
        this.display       = display;
        this.keterangan    = keterangan;
        this.butuhPrioritas = butuhPrioritas;
    }

    public String  getDisplay()        { return display; }
    public String  getKeterangan()     { return keterangan; }
    public boolean isMembutuhkanPrioritas() { return butuhPrioritas; }

    @Override
    public String toString() { return display; }
}
