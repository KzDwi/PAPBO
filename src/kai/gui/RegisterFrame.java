package kai.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import kai.controller.AuthManager;
import kai.model.JenisKelamin;

/**
 * Frame Registrasi akun baru menggunakan NIK dari KTP.
 */
public class RegisterFrame extends JFrame {

    private StyledTextField  txtNIK, txtNama, txtEmail, txtUmur, txtAlamat;
    private StyledPasswordField txtPass, txtPassKonfirm;
    private JComboBox<JenisKelamin> cmbGender;
    private JLabel lblError, lblSuccess;

    public RegisterFrame() {
        setTitle("KAI - Daftar Akun Baru");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildContent());
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.KAI_ABU);

        // Header
        root.add(buildHeader(), BorderLayout.NORTH);

        // Scrollable form
        JPanel formWrap = new JPanel(new GridBagLayout());
        formWrap.setBackground(UIConstants.KAI_ABU);
        formWrap.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        formWrap.add(buildFormCard());

        JScrollPane scroll = new JScrollPane(formWrap);
        scroll.setBorder(null);
        scroll.setBackground(UIConstants.KAI_ABU);
        root.add(scroll, BorderLayout.CENTER);

        return root;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU_GELAP,
                                                      getWidth(), 0, UIConstants.KAI_BIRU);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(520, 90));
        header.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        JLabel t1 = new JLabel("Buat Akun Baru");
        t1.setFont(UIConstants.FONT_SUBJUDUL);
        t1.setForeground(Color.WHITE);
        t1.setAlignmentX(CENTER_ALIGNMENT);
        JLabel t2 = new JLabel("Daftarkan diri Anda menggunakan data KTP");
        t2.setFont(UIConstants.FONT_KECIL);
        t2.setForeground(new Color(200, 220, 255));
        t2.setAlignmentX(CENTER_ALIGNMENT);
        inner.add(t1); inner.add(t2);
        header.add(inner);
        return header;
    }

    private RoundedPanel buildFormCard() {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(445, 800));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1;

        int row = 0;

        // Section: Data KTP
        addSectionTitle(card, gbc, row++, "Data KTP (Kartu Tanda Penduduk)");

        addLabel(card, gbc, row++, "Nomor Induk Kependudukan (NIK) *");
        txtNIK = new StyledTextField("Masukkan 16 digit NIK");
        addField(card, gbc, row++, txtNIK);

        addLabel(card, gbc, row++, "Nama Lengkap (sesuai KTP) *");
        txtNama = new StyledTextField("Nama sesuai KTP");
        addField(card, gbc, row++, txtNama);

        addLabel(card, gbc, row++, "Jenis Kelamin *");
        cmbGender = new JComboBox<>(JenisKelamin.values());
        cmbGender.setFont(UIConstants.FONT_NORMAL);
        cmbGender.setPreferredSize(new Dimension(200, 38));
        cmbGender.setBackground(Color.WHITE);
        addField(card, gbc, row++, cmbGender);

        addLabel(card, gbc, row++, "Umur *");
        txtUmur = new StyledTextField("Contoh: 30");
        addField(card, gbc, row++, txtUmur);

        addLabel(card, gbc, row++, "Alamat");
        txtAlamat = new StyledTextField("Alamat sesuai KTP");
        addField(card, gbc, row++, txtAlamat);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.KAI_BORDER);
        gbc.gridy = row++; gbc.insets = new Insets(10, 24, 10, 24);
        card.add(sep, gbc);

        // Section: Akun
        addSectionTitle(card, gbc, row++, "Data Akun");

        addLabel(card, gbc, row++, "Email *");
        txtEmail = new StyledTextField("contoh@email.com");
        addField(card, gbc, row++, txtEmail);

        addLabel(card, gbc, row++, "Password * (min. 6 karakter)");
        txtPass = new StyledPasswordField("Buat password");
        addField(card, gbc, row++, txtPass);

        addLabel(card, gbc, row++, "Konfirmasi Password *");
        txtPassKonfirm = new StyledPasswordField("Ulangi password");
        addField(card, gbc, row++, txtPassKonfirm);

        // Error / success label
        lblError = new JLabel(" ");
        lblError.setFont(UIConstants.FONT_KECIL);
        lblError.setForeground(UIConstants.KAI_MERAH);
        gbc.gridy = row++; gbc.insets = new Insets(4, 24, 2, 24);
        card.add(lblError, gbc);

        lblSuccess = new JLabel(" ");
        lblSuccess.setFont(UIConstants.FONT_KECIL);
        lblSuccess.setForeground(UIConstants.KAI_HIJAU);
        gbc.gridy = row++; gbc.insets = new Insets(0, 24, 4, 24);
        card.add(lblSuccess, gbc);

        // Tombol daftar
        StyledButton btnDaftar = new StyledButton("DAFTAR SEKARANG", UIConstants.KAI_BIRU);
        btnDaftar.setPreferredSize(new Dimension(397, 44));
        gbc.gridy = row++; gbc.insets = new Insets(4, 24, 8, 24);
        card.add(btnDaftar, gbc);
        btnDaftar.addActionListener(e -> prosesRegistrasi());

        // Kembali Login
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        backPanel.setOpaque(false);
        JLabel lblSudah = new JLabel("Sudah punya akun?");
        lblSudah.setFont(UIConstants.FONT_KECIL);
        lblSudah.setForeground(UIConstants.KAI_ABU_GELAP);
        JLabel lblLogin = new JLabel("<html><u>Masuk di sini</u></html>");
        lblLogin.setFont(UIConstants.FONT_KECIL);
        lblLogin.setForeground(UIConstants.KAI_BIRU);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { kembaliLogin(); }
        });
        backPanel.add(lblSudah); backPanel.add(lblLogin);
        gbc.gridy = row; gbc.insets = new Insets(0, 24, 20, 24);
        card.add(backPanel, gbc);

        return card;
    }

    private void addSectionTitle(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_BOLD);
        lbl.setForeground(UIConstants.KAI_BIRU);
        gbc.gridy = row; gbc.insets = new Insets(14, 24, 4, 24);
        p.add(lbl, gbc);
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_LABEL);
        lbl.setForeground(new Color(50, 65, 90));
        gbc.gridy = row; gbc.insets = new Insets(6, 24, 2, 24);
        p.add(lbl, gbc);
    }

    private void addField(JPanel p, GridBagConstraints gbc, int row, JComponent comp) {
        gbc.gridy = row; gbc.insets = new Insets(2, 24, 4, 24);
        p.add(comp, gbc);
    }

    private void prosesRegistrasi() {
        lblError.setText(" ");
        lblSuccess.setText(" ");

        String nik     = txtNIK.getText().trim();
        String nama    = txtNama.getText().trim();
        String email   = txtEmail.getText().trim();
        String umurStr = txtUmur.getText().trim();
        String alamat  = txtAlamat.getText().trim();
        String pass    = new String(txtPass.getPassword());
        String pass2   = new String(txtPassKonfirm.getPassword());
        JenisKelamin gender = (JenisKelamin) cmbGender.getSelectedItem();

        // Delegasi validasi kecocokan password & parsing umur ke Controller
        String errorPra = AuthManager.getInstance().validasiPraRegistrasi(pass, pass2, umurStr);
        if (errorPra != null) {
            lblError.setText(errorPra);
            return;
        }

        // Controller yang melakukan parsing — GUI tinggal pakai hasilnya
        int umur = AuthManager.getInstance().parseUmur(umurStr);

        String err = AuthManager.getInstance().registrasi(nik, nama, pass, email, umur, gender, alamat);
        if (err != null) {
            lblError.setText(err);
        } else {
            lblSuccess.setText("Registrasi berhasil! Silakan login.");
            Timer t = new Timer(1800, ev -> kembaliLogin());
            t.setRepeats(false);
            t.start();
        }
    }

    private void kembaliLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
