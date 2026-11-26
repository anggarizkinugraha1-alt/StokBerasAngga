package com.beras.stokberas;

import com.beras.stokberas.entity.Beras;
import com.beras.stokberas.util.HibernateUtil;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

/** Tampilan setelah login: 3 fitur dalam tab (Data Beras, Barang Masuk, Barang Keluar). */
public class MenuUtama extends JFrame {

    private final JTextField txtNama = new JTextField(12);
    private final JTextField txtHarga = new JTextField(8);
    private final JComboBox<Beras> cmbMasuk = new JComboBox<>();
    private final JComboBox<Beras> cmbKeluar = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nama", "Harga/kg", "Stok (kg)"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable tabel = new JTable(model);

    public MenuUtama(String username) {
        super("Stok Beras - " + username);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Data Beras", panelData());
        tabs.addTab("Barang Masuk", panelStok(cmbMasuk, 1));
        tabs.addTab("Barang Keluar", panelStok(cmbKeluar, -1));
        add(tabs);

        setSize(700, 400);
        setLocationRelativeTo(null);
        muat();
    }

    // Fitur 1: tambah / ubah / hapus data beras
    private JPanel panelData() {
        tabel.getSelectionModel().addListSelectionListener(e -> {
            int r = tabel.getSelectedRow();
            if (r >= 0) {
                txtNama.setText(model.getValueAt(r, 1).toString());
                txtHarga.setText(model.getValueAt(r, 2).toString());
            }
        });

        JButton tambah = new JButton("Tambah");
        JButton ubah = new JButton("Ubah");
        JButton hapus = new JButton("Hapus");
        tambah.addActionListener(e -> jalankan(s -> s.persist(
                new Beras(txtNama.getText().trim(), Long.parseLong(txtHarga.getText().trim()), 0))));
        ubah.addActionListener(e -> jalankan(s -> {
            Beras b = s.get(Beras.class, idDipilih());
            b.setNama(txtNama.getText().trim());
            b.setHarga(Long.parseLong(txtHarga.getText().trim()));
        }));
        hapus.addActionListener(e -> jalankan(s -> s.remove(s.get(Beras.class, idDipilih()))));

        JPanel form = new JPanel();
        form.add(new JLabel("Nama"));
        form.add(txtNama);
        form.add(new JLabel("Harga/kg"));
        form.add(txtHarga);
        form.add(tambah);
        form.add(ubah);
        form.add(hapus);

        JPanel p = new JPanel(new BorderLayout());
        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return p;
    }

    // Fitur 2 & 3: arah = 1 (masuk) atau -1 (keluar)
    private JPanel panelStok(JComboBox<Beras> cmb, int arah) {
        JSpinner jumlah = new JSpinner(new SpinnerNumberModel(1, 1, 1000000, 1));
        JButton simpan = new JButton(arah > 0 ? "Simpan Barang Masuk" : "Simpan Barang Keluar");
        simpan.addActionListener(e -> jalankan(s -> {
            if (cmb.getSelectedItem() == null) {
                throw new IllegalStateException("Belum ada data beras.");
            }
            Beras b = s.get(Beras.class, ((Beras) cmb.getSelectedItem()).getId());
            int baru = b.getStok() + arah * (int) jumlah.getValue();
            if (baru < 0) {
                throw new IllegalStateException("Stok tidak cukup. Stok sekarang " + b.getStok() + " kg.");
            }
            b.setStok(baru);
        }));

        JPanel p = new JPanel();
        p.add(new JLabel("Beras"));
        p.add(cmb);
        p.add(new JLabel("Jumlah (kg)"));
        p.add(jumlah);
        p.add(simpan);
        return p;
    }

    private Long idDipilih() {
        int r = tabel.getSelectedRow();
        if (r < 0) {
            throw new IllegalStateException("Pilih data di tabel dulu.");
        }
        return (Long) model.getValueAt(r, 0);
    }

    /** Menjalankan perubahan dalam satu transaksi, lalu memuat ulang tampilan. */
    private void jalankan(Consumer<Session> kerja) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction t = s.beginTransaction();
            try {
                kerja.accept(s);
                t.commit();
            } catch (RuntimeException e) {
                t.rollback();
                throw e;
            }
            muat();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka.");
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void muat() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            model.setRowCount(0);
            cmbMasuk.removeAllItems();
            cmbKeluar.removeAllItems();
            for (Beras b : s.createQuery("from Beras order by nama", Beras.class).list()) {
                model.addRow(new Object[]{b.getId(), b.getNama(), b.getHarga(), b.getStok()});
                cmbMasuk.addItem(b);
                cmbKeluar.addItem(b);
            }
        }
    }
}
