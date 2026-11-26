package com.beras.manajemenstokberas;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Form login Manajemen Stok Beras.
 * Logika login sama dengan versi Manajemen Stok Mebel, hanya tampilan yang diganti.
 */
public class Login extends JFrame {

    // ===== Warna tema =====
    private static final Color HIJAU_TUA  = new Color(0x1B4332);
    private static final Color HIJAU      = new Color(0x2D6A4F);
    private static final Color HIJAU_MUDA = new Color(0x52B788);
    private static final Color EMAS       = new Color(0xE9B44C);
    private static final Color KRIM       = new Color(0xFFFBF2);
    private static final Color ABU_TEKS   = new Color(0x6B705C);
    private static final Color GARIS      = new Color(0xD9D2C0);

    // ===== Komponen =====
    JTextField UsernameForm;
    JPasswordField PasswordForm;
    JButton jButton1;
    private JCheckBox tampilPassword;

    public Login() {
        initComponents();
    }

    private static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Manajemen Stok Beras");
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setPreferredSize(new Dimension(780, 470));

        root.add(buatPanelKiri());
        root.add(buatPanelKanan());

        setContentPane(root);
        getRootPane().setDefaultButton(jButton1);
        pack();
    }

    // ===================== PANEL KIRI (branding) =====================
    private JPanel buatPanelKiri() {
        JPanel kiri = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                g2.setPaint(new GradientPaint(0, 0, HIJAU, w, h, HIJAU_TUA));
                g2.fillRect(0, 0, w, h);

                // butir beras dekoratif di latar
                g2.setColor(new Color(255, 255, 255, 18));
                int[][] butir = {
                    {30, 40, 40}, {250, 70, -30}, {320, 190, 60}, {20, 250, 20},
                    {290, 330, -50}, {70, 410, 35}, {200, 440, -20}, {330, 40, 15}
                };
                for (int[] b : butir) gambarButir(g2, b[0], b[1], 46, 18, b[2]);

                // lingkaran emas + ikon butir beras
                int cx = w / 2, cy = 150;
                g2.setColor(new Color(233, 180, 76, 60));
                g2.fill(new Ellipse2D.Double(cx - 62, cy - 62, 124, 124));
                g2.setColor(EMAS);
                g2.fill(new Ellipse2D.Double(cx - 48, cy - 48, 96, 96));
                g2.setColor(KRIM);
                gambarButir(g2, cx - 22, cy - 6, 38, 15, -35);
                gambarButir(g2, cx + 4,  cy - 14, 38, 15, 35);
                gambarButir(g2, cx - 9,  cy + 8,  38, 15, 0);
                g2.dispose();
            }
        };

        JLabel judul = new JLabel("Stok Beras", SwingConstants.CENTER);
        judul.setFont(font(Font.BOLD, 30));
        judul.setForeground(Color.WHITE);
        judul.setBounds(0, 240, 390, 40);

        JLabel sub = new JLabel("Manajemen persediaan beras", SwingConstants.CENTER);
        sub.setFont(font(Font.PLAIN, 14));
        sub.setForeground(new Color(0xD8F3DC));
        sub.setBounds(0, 282, 390, 22);

        JPanel garis = new JPanel();
        garis.setBackground(EMAS);
        garis.setBounds(165, 318, 60, 4);

        JLabel fitur = new JLabel(
                "<html><center>Pantau stok masuk, stok keluar<br>dan ketersediaan beras dalam satu aplikasi.</center></html>",
                SwingConstants.CENTER);
        fitur.setFont(font(Font.PLAIN, 12));
        fitur.setForeground(new Color(0xB7E4C7));
        fitur.setBounds(20, 336, 350, 40);

        kiri.add(judul);
        kiri.add(sub);
        kiri.add(garis);
        kiri.add(fitur);
        return kiri;
    }

    private static void gambarButir(Graphics2D g2, int x, int y, int p, int l, int derajat) {
        Graphics2D g = (Graphics2D) g2.create();
        g.rotate(Math.toRadians(derajat), x + p / 2.0, y + l / 2.0);
        g.fill(new Ellipse2D.Double(x, y, p, l));
        g.dispose();
    }

    // ===================== PANEL KANAN (form) =====================
    private JPanel buatPanelKanan() {
        JPanel kanan = new JPanel(null);
        kanan.setBackground(KRIM);

        int x = 48, w = 294;

        JLabel jLabel1 = new JLabel("Selamat Datang");
        jLabel1.setFont(font(Font.BOLD, 26));
        jLabel1.setForeground(HIJAU_TUA);
        jLabel1.setBounds(x, 56, w, 36);

        JLabel info = new JLabel("Masuk untuk mengelola stok beras");
        info.setFont(font(Font.PLAIN, 13));
        info.setForeground(ABU_TEKS);
        info.setBounds(x, 94, w, 20);

        JLabel jLabel2 = new JLabel("USERNAME");
        jLabel2.setFont(font(Font.BOLD, 11));
        jLabel2.setForeground(HIJAU);
        jLabel2.setBounds(x, 140, w, 16);

        UsernameForm = new JTextField();
        gayaInput(UsernameForm);
        UsernameForm.setBounds(x, 160, w, 40);

        JLabel jLabel3 = new JLabel("PASSWORD");
        jLabel3.setFont(font(Font.BOLD, 11));
        jLabel3.setForeground(HIJAU);
        jLabel3.setBounds(x, 214, w, 16);

        PasswordForm = new JPasswordField();
        gayaInput(PasswordForm);
        PasswordForm.setBounds(x, 234, w, 40);
        final char echoAsli = PasswordForm.getEchoChar();

        tampilPassword = new JCheckBox("Tampilkan password");
        tampilPassword.setFont(font(Font.PLAIN, 12));
        tampilPassword.setForeground(ABU_TEKS);
        tampilPassword.setOpaque(false);
        tampilPassword.setFocusPainted(false);
        tampilPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tampilPassword.setBounds(x - 3, 282, w, 22);
        tampilPassword.addActionListener(e ->
                PasswordForm.setEchoChar(tampilPassword.isSelected() ? (char) 0 : echoAsli));

        jButton1 = new JButton("MASUK") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color dasar = getModel().isPressed() ? HIJAU_TUA
                        : getModel().isRollover() ? HIJAU_MUDA : HIJAU;
                g2.setColor(dasar);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(EMAS);
                g2.fill(new RoundRectangle2D.Double(0, getHeight() - 4, getWidth(), 4, 4, 4));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        jButton1.setFont(font(Font.BOLD, 14));
        jButton1.setForeground(Color.WHITE);
        jButton1.setContentAreaFilled(false);
        jButton1.setBorderPainted(false);
        jButton1.setFocusPainted(false);
        jButton1.setOpaque(false);
        jButton1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jButton1.setBounds(x, 328, w, 44);
        jButton1.addActionListener(this::jButton1ActionPerformed);

        JLabel footer = new JLabel("© Manajemen Stok Beras", SwingConstants.CENTER);
        footer.setFont(font(Font.PLAIN, 11));
        footer.setForeground(new Color(0xA5A58D));
        footer.setBounds(x, 410, w, 16);

        kanan.add(jLabel1);
        kanan.add(info);
        kanan.add(jLabel2);
        kanan.add(UsernameForm);
        kanan.add(jLabel3);
        kanan.add(PasswordForm);
        kanan.add(tampilPassword);
        kanan.add(jButton1);
        kanan.add(footer);
        return kanan;
    }

    private void gayaInput(JTextField f) {
        f.setFont(font(Font.PLAIN, 14));
        f.setForeground(HIJAU_TUA);
        f.setBackground(Color.WHITE);
        f.setCaretColor(HIJAU);
        Border biasa = new CompoundBorder(new LineBorder(GARIS, 1, true), new EmptyBorder(6, 12, 6, 12));
        Border fokus = new CompoundBorder(new LineBorder(HIJAU_MUDA, 2, true), new EmptyBorder(5, 11, 5, 11));
        f.setBorder(biasa);
        f.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { f.setBorder(fokus); }
            @Override public void focusLost(FocusEvent e)   { f.setBorder(biasa); }
        });
    }

    // ===================== LOGIKA LOGIN (sama seperti versi mebel) =====================
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String username = UsernameForm.getText().trim();
        String password = new String(PasswordForm.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username dan password wajib diisi!");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/beras";
        String dbUser = "root";
        String dbPassword = "";

        String sql = "SELECT * FROM pengguna WHERE username = ? AND password = ?";

        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                    url, dbUser, dbPassword);

            java.sql.PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, username);
            pst.setString(2, password);

            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this,
                        "Login berhasil! Selamat Datang, " + username);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Username atau password salah!");
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Koneksi database gagal: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> new Login().setVisible(true));
    }
}
