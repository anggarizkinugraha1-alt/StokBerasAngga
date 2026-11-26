package com.beras.stokberas.ui;

import com.beras.stokberas.Login;
import com.beras.stokberas.entity.JenisTransaksi;
import com.beras.stokberas.util.HibernateUtil;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Tampilan utama setelah login: berisi 3 fitur dalam bentuk tab. */
public class MenuUtama extends JFrame {

    private final JTabbedPane tabs = new JTabbedPane();
    private final List<Refreshable> daftarPanel = new ArrayList<>();

    public MenuUtama(String username) {
        super("Stok Beras");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HibernateUtil.shutdown();
            }
        });

        // Header
        JLabel judul = new JLabel("STOK BERAS");
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 18f));
        JLabel info = new JLabel("Login sebagai: " + username + "   ");
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> logout());

        JPanel kanan = new JPanel();
        kanan.add(info);
        kanan.add(btnLogout);

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        header.add(judul, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);

        // 3 fitur
        tambahTab("Data Beras", new PanelDataBeras());
        tambahTab("Barang Masuk", new PanelTransaksi(JenisTransaksi.MASUK));
        tambahTab("Barang Keluar", new PanelTransaksi(JenisTransaksi.KELUAR));

        // Muat ulang data tiap kali pindah tab (agar stok selalu terbaru)
        tabs.addChangeListener(e -> {
            int i = tabs.getSelectedIndex();
            if (i >= 0) {
                daftarPanel.get(i).muat();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(tabs, BorderLayout.CENTER);

        setSize(780, 520);
        setLocationRelativeTo(null);
        daftarPanel.get(0).muat();
    }

    private <T extends JPanel & Refreshable> void tambahTab(String judul, T panel) {
        tabs.addTab(judul, panel);
        daftarPanel.add(panel);
    }

    private void logout() {
        dispose();
        Login login = new Login();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }
}
