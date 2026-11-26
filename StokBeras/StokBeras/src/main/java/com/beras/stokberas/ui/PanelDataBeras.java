package com.beras.stokberas.ui;

import com.beras.stokberas.entity.Beras;
import com.beras.stokberas.service.StokService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Fitur 1: kelola data jenis beras (tambah, ubah, hapus). */
public class PanelDataBeras extends JPanel implements Refreshable {

    private final StokService service = new StokService();

    private final JTextField txtNama = new JTextField(18);
    private final JTextField txtHarga = new JTextField(10);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Nama Beras", "Harga/kg (Rp)", "Stok (kg)"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabel = new JTable(model);
    private Long idTerpilih;

    public PanelDataBeras() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        form.add(new JLabel("Nama"));
        form.add(txtNama);
        form.add(new JLabel("Harga/kg"));
        form.add(txtHarga);
        form.add(btnTambah);
        form.add(btnUbah);
        form.add(btnHapus);
        form.add(btnBersih);

        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            int r = tabel.getSelectedRow();
            if (!e.getValueIsAdjusting() && r >= 0) {
                idTerpilih = (Long) model.getValueAt(r, 0);
                txtNama.setText(String.valueOf(model.getValueAt(r, 1)));
                txtHarga.setText(String.valueOf(model.getValueAt(r, 2)));
            }
        });

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(new JLabel("Stok tidak diubah di sini. Gunakan tab Barang Masuk / Barang Keluar."),
                BorderLayout.SOUTH);

        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersihkanForm());
    }

    @Override
    public void muat() {
        try {
            model.setRowCount(0);
            idTerpilih = null;
            for (Beras b : service.semuaBeras()) {
                model.addRow(new Object[]{b.getId(), b.getNama(), b.getHarga(), b.getStok()});
            }
        } catch (RuntimeException ex) {
            error("Gagal memuat data: " + ex.getMessage());
        }
    }

    private void tambah() {
        String nama = txtNama.getText().trim();
        Long harga = bacaHarga();
        if (nama.isEmpty() || harga == null) {
            return;
        }
        try {
            service.tambahBeras(nama, harga);
            bersihkanForm();
            muat();
        } catch (RuntimeException ex) {
            error("Gagal menambah data: " + ex.getMessage());
        }
    }

    private void ubah() {
        if (idTerpilih == null) {
            error("Pilih data di tabel terlebih dahulu.");
            return;
        }
        String nama = txtNama.getText().trim();
        Long harga = bacaHarga();
        if (nama.isEmpty() || harga == null) {
            return;
        }
        try {
            service.ubahBeras(idTerpilih, nama, harga);
            bersihkanForm();
            muat();
        } catch (RuntimeException ex) {
            error("Gagal mengubah data: " + ex.getMessage());
        }
    }

    private void hapus() {
        if (idTerpilih == null) {
            error("Pilih data di tabel terlebih dahulu.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "Hapus data beras ini?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            service.hapusBeras(idTerpilih);
            bersihkanForm();
            muat();
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    /** @return harga, atau null (dan menampilkan pesan) jika input tidak valid. */
    private Long bacaHarga() {
        String teks = txtHarga.getText().trim();
        if (txtNama.getText().trim().isEmpty() || teks.isEmpty()) {
            error("Nama dan harga wajib diisi.");
            return null;
        }
        try {
            long h = Long.parseLong(teks);
            if (h <= 0) {
                error("Harga harus lebih dari 0.");
                return null;
            }
            return h;
        } catch (NumberFormatException ex) {
            error("Harga harus berupa angka (tanpa titik/koma).");
            return null;
        }
    }

    private void bersihkanForm() {
        txtNama.setText("");
        txtHarga.setText("");
        idTerpilih = null;
        tabel.clearSelection();
    }

    private void error(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Info", JOptionPane.WARNING_MESSAGE);
    }
}
