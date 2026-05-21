package kai.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Splash screen animasi loading saat aplikasi pertama kali dibuka.
 */
public class SplashScreen extends JWindow {

    private JProgressBar progressBar;
    private JLabel       lblStatus;

    public SplashScreen() {
        setSize(460, 280);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIConstants.KAI_BIRU_GELAP,
                                                      getWidth(), getHeight(),
                                                      new Color(0, 110, 200));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));

        // Icon
        JLabel icon = new JLabel("\uD83D\uDE82", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setForeground(Color.WHITE);
        panel.add(icon, BorderLayout.NORTH);

        // Teks
        JPanel mid = new JPanel(new GridLayout(3, 1, 0, 4));
        mid.setOpaque(false);

        JLabel t1 = new JLabel("PT KERETA API INDONESIA", SwingConstants.CENTER);
        t1.setFont(UIConstants.FONT_JUDUL);
        t1.setForeground(Color.WHITE);
        JLabel t2 = new JLabel("Sistem Tiket & Alokasi Kursi Otomatis", SwingConstants.CENTER);
        t2.setFont(UIConstants.FONT_NORMAL);
        t2.setForeground(new Color(200, 220, 255));
        lblStatus = new JLabel("Memulai sistem...", SwingConstants.CENTER);
        lblStatus.setFont(UIConstants.FONT_KECIL);
        lblStatus.setForeground(new Color(160, 200, 255));

        mid.add(t1); mid.add(t2); mid.add(lblStatus);
        panel.add(mid, BorderLayout.CENTER);

        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(0, 6));
        progressBar.setBorderPainted(false);
        progressBar.setForeground(UIConstants.KAI_EMAS);
        progressBar.setBackground(new Color(255, 255, 255, 40));
        panel.add(progressBar, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    public void show(Runnable onComplete) {
        setVisible(true);

        String[] steps = {
            "Menginisialisasi kereta...",
            "Memuat data gerbong...",
            "Menyiapkan sistem alokasi...",
            "Memuat antarmuka...",
            "Siap!"
        };

        Timer timer = new Timer(300, null);
        int[] idx = {0};

        timer.addActionListener(e -> {
            if (idx[0] < steps.length) {
                lblStatus.setText(steps[idx[0]]);
                progressBar.setValue((idx[0] + 1) * (100 / steps.length));
                idx[0]++;
            } else {
                timer.stop();
                dispose();
                onComplete.run();
            }
        });
        timer.start();
    }
}
