package com.beras.stokberas.ui;

import com.beras.stokberas.entity.Beras;
import com.beras.stokberas.entity.JenisTransaksi;
import com.beras.stokberas.entity.TransaksiStok;
import com.beras.stokberas.service.StokService;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

/** Fitur 2 (Barang Masuk) dan Fitur 3 (Barang Keluar); bedanya hanya jenis transaksi. */
public class PanelTransaksi extends JPanel implements Refreshable {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final StokService service = new StokService();
    private final JenisTransaksi jenis;

    private final JComboBox<Beras> cmbBeras = new JComboBox<>();
    private final JSpinner spnJumlah = new JSpinner(new SpinnerNumberModel(1, 1, 1_000_000, 1));
    private final JTextField txtKeterangan = new JTextField(18);
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Waktu", "Beras", "Jumlah (kg)", "Keterangan"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    public PanelTransaksi(JenisTransaksi jenis) {
        this.jenis = jenis;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boolean masuk = jenis == JenisTransaksi.MASUK;
        JButton btnSimpan = new JButton(masuk ? "Simpan Barang Masuk" : "Simpan Barang Keluar");

        cmbBeras.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof Beras) {
                    Beras b = (Beras) value;
                    setText(b.getNama() + "  (stok: " + b.getStok() + " kg)");
                }
                return this;
            }
        });

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        form.add(new JLabel("Beras"));
        form.add(cmbBeras);
        form.add(new JLabel("Jumlah (kg)"));
        form.add(spnJumlah);
        form.add(new JLabel(masuk ? "Asal/Supplier" : "Pembeli/Tujuan"));
        form.add(txtKeterangan);
        form.add(btnSimpan);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);

        btnSimpan.addActionListener(e -> simpan());
    }

    @Override
    public void muat() {
        try {
            Beras terpilih = (Beras) cmbBeras.getSelectedItem();
            Long idTerpilih = terpilih != null ? terpilih.getId() : null;

            cmbBeras.removeAllItems();
            for (Beras b : service.semuaBeras()) {
                cmbBeras.addItem(b);
                if (b.getId().equals(idTerpilih)) {
                    cmbBeras.setSelectedItem(b);
                }
            }

            model.setRowCount(0);
            for (TransaksiStok t : service.riwayat(jenis)) {
                model.addRow(new Object[]{
                    t.getTanggal().format(FORMAT),
                    t.getBeras().getNama(),
                    t.getJumlah(),
                    t.getKeterangan()
                });
            }
        } catch (RuntimeException ex) {
            pesan("Gagal memuat data: " + ex.getMessage());
        }
    }

    private void simpan() {
        Beras b = (Beras) cmbBeras.getSelectedItem();
        if (b == null) {
            pesan("Belum ada data beras. Tambahkan dulu di tab Data Beras.");
            return;
        }
        try {
            spnJumlah.commitEdit();
        } catch (ParseException ex) {
            pesan("Jumlah harus berupa angka.");
            return;
        }
        int jumlah = (Integer) spnJumlah.getValue();
        String ket = txtKeterangan.getText().trim();

        try {
            if (jenis == JenisTransaksi.MASUK) {
                service.catatMasuk(b.getId(), jumlah, ket);
            } else {
                service.catatKeluar(b.getId(), jumlah, ket);
            }
            spnJumlah.setValue(1);
            txtKeterangan.setText("");
            muat();
        } catch (RuntimeException ex) {
            pesan(ex.getMessage());
        }
    }

    private void pesan(String teks) {
        JOptionPane.showMessageDialog(this, teks, "Info", JOptionPane.WARNING_MESSAGE);
    }
}
