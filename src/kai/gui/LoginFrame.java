package kai.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kai.controller.AuthManager;

/**
 * Frame halaman Login dengan NIK dan Password.
 * Navigasi ke RegisterFrame atau DashboardFrame.
 */
public class LoginFrame extends JFrame {

    private StyledTextField  txtNIK;
    private StyledPasswordField txtPassword;
    private JLabel           lblError;

    public LoginFrame() {
        setTitle("KAI - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(buildContent());
    }

    // ── Build UI ───────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.KAI_ABU);

        // Header biru
        root.add(buildHeader(), BorderLayout.NORTH);

        // Card form di tengah
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setBackground(UIConstants.KAI_ABU);
        centerWrap.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        centerWrap.add(buildFormCard());
        root.add(centerWrap, BorderLayout.CENTER);

        return root;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU_GELAP,
                                                      getWidth(), 0, UIConstants.KAI_BIRU);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(480, 160));
        header.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // Logo placeholder (lingkaran + teks)
        JLabel logo = new JLabel("\uD83D\uDE82") {{
            setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
            setForeground(Color.WHITE);
            setAlignmentX(CENTER_ALIGNMENT);
        }};
        JLabel title = new JLabel("PT KERETA API INDONESIA") {{
            setFont(UIConstants.FONT_JUDUL);
            setForeground(Color.WHITE);
            setAlignmentX(CENTER_ALIGNMENT);
        }};
        JLabel sub = new JLabel("Sistem Tiket & Alokasi Kursi") {{
            setFont(UIConstants.FONT_KECIL);
            setForeground(new Color(200, 220, 255));
            setAlignmentX(CENTER_ALIGNMENT);
        }};

        inner.add(logo);
        inner.add(Box.createVerticalStrut(4));
        inner.add(title);
        inner.add(sub);
        header.add(inner);
        return header;
    }

    private RoundedPanel buildFormCard() {
        RoundedPanel card = new RoundedPanel(16, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 370));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0; gbc.weightx = 1;

        // Judul card
        JLabel lblJudul = new JLabel("Masuk ke Akun Anda");
        lblJudul.setFont(UIConstants.FONT_SUBJUDUL);
        lblJudul.setForeground(UIConstants.KAI_BIRU_GELAP);
        gbc.gridy = 0; gbc.insets = new Insets(20, 24, 4, 24);
        card.add(lblJudul, gbc);

        JLabel lblSub = new JLabel("Gunakan NIK (KTP) dan password Anda");
        lblSub.setFont(UIConstants.FONT_KECIL);
        lblSub.setForeground(UIConstants.KAI_ABU_GELAP);
        gbc.gridy = 1; gbc.insets = new Insets(0, 24, 16, 24);
        card.add(lblSub, gbc);

        // Field NIK
        addLabel(card, gbc, 2, "Nomor Induk Kependudukan (NIK)");
        txtNIK = new StyledTextField("Masukkan 16 digit NIK");
        gbc.gridy = 3; gbc.insets = new Insets(4, 24, 10, 24);
        card.add(txtNIK, gbc);

        // Field Password
        addLabel(card, gbc, 4, "Password");
        txtPassword = new StyledPasswordField("Masukkan password");
        gbc.gridy = 5; gbc.insets = new Insets(4, 24, 6, 24);
        card.add(txtPassword, gbc);

        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(UIConstants.FONT_KECIL);
        lblError.setForeground(UIConstants.KAI_MERAH);
        gbc.gridy = 6; gbc.insets = new Insets(0, 24, 6, 24);
        card.add(lblError, gbc);

        // Tombol Login
        StyledButton btnLogin = new StyledButton("MASUK", UIConstants.KAI_BIRU);
        btnLogin.setPreferredSize(new Dimension(350, 44));
        gbc.gridy = 7; gbc.insets = new Insets(4, 24, 10, 24);
        card.add(btnLogin, gbc);
        btnLogin.addActionListener(e -> proseLogin());

        // Daftar akun baru
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regPanel.setOpaque(false);
        JLabel lblBelum = new JLabel("Belum punya akun?");
        lblBelum.setFont(UIConstants.FONT_KECIL);
        lblBelum.setForeground(UIConstants.KAI_ABU_GELAP);
        JLabel lblDaftar = new JLabel("<html><u>Daftar Sekarang</u></html>");
        lblDaftar.setFont(UIConstants.FONT_KECIL);
        lblDaftar.setForeground(UIConstants.KAI_BIRU);
        lblDaftar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblDaftar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { bukaRegistrasi(); }
        });
        regPanel.add(lblBelum);
        regPanel.add(lblDaftar);
        gbc.gridy = 8; gbc.insets = new Insets(0, 24, 20, 24);
        card.add(regPanel, gbc);

        // Enter key listener
        txtPassword.addActionListener(e -> proseLogin());
        txtNIK.addActionListener(e -> txtPassword.requestFocus());

        return card;
    }

    private void addLabel(JPanel p, GridBagConstraints gbc, int row, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.FONT_LABEL);
        lbl.setForeground(new Color(50, 65, 90));
        gbc.gridy = row; gbc.insets = new Insets(8, 24, 2, 24);
        p.add(lbl, gbc);
    }

    // ── Logic ──────────────────────────────────────────────────────────
    private void proseLogin() {
        // String nik  = txtNIK.getText().trim();
        // String pass = new String(txtPassword.getPassword());
        String nik = "3201012501900001";
        String pass = "password123";

        if (nik.isEmpty() || pass.isEmpty()) {
            lblError.setText("NIK dan password tidak boleh kosong!");
            return;
        }

        if (AuthManager.getInstance().login(nik, pass)) {
            lblError.setText(" ");
            dispose();
            new DashboardFrame().setVisible(true);
        } else {
            lblError.setText("NIK atau password salah. Coba lagi.");
            txtPassword.setText("");
        }
    }

    private void bukaRegistrasi() {
        dispose();
        new RegisterFrame().setVisible(true);
    }
}
