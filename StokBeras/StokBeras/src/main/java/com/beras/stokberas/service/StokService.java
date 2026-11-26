package com.beras.stokberas.service;

import com.beras.stokberas.entity.Beras;
import com.beras.stokberas.entity.JenisTransaksi;
import com.beras.stokberas.entity.Pengguna;
import com.beras.stokberas.entity.TransaksiStok;
import com.beras.stokberas.util.HibernateUtil;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StokService {

    /** Menjalankan pekerjaan dalam satu transaksi; rollback otomatis jika gagal. */
    private <T> T tx(Function<Session, T> work) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction t = session.beginTransaction();
            try {
                T hasil = work.apply(session);
                t.commit();
                return hasil;
            } catch (RuntimeException e) {
                if (t.isActive()) {
                    t.rollback();
                }
                throw e;
            }
        }
    }

    // ---------- Login ----------

    /** @return pengguna jika cocok, null jika username/password salah. */
    public Pengguna login(String username, String password) {
        return tx(s -> s.createQuery(
                "from Pengguna where username = :u and password = :p", Pengguna.class)
                .setParameter("u", username)
                .setParameter("p", password)
                .uniqueResult());
    }

    // ---------- Fitur 1: Data Beras ----------

    public List<Beras> semuaBeras() {
        return tx(s -> s.createQuery("from Beras order by nama", Beras.class).list());
    }

    public void tambahBeras(String nama, long harga) {
        tx(s -> {
            s.persist(new Beras(nama, harga, 0));
            return null;
        });
    }

    public void ubahBeras(Long id, String nama, long harga) {
        tx(s -> {
            Beras b = s.find(Beras.class, id);
            if (b == null) {
                throw new IllegalStateException("Data beras tidak ditemukan.");
            }
            b.setNama(nama);
            b.setHarga(harga);
            return null;
        });
    }

    public void hapusBeras(Long id) {
        tx(s -> {
            Beras b = s.find(Beras.class, id);
            if (b == null) {
                throw new IllegalStateException("Data beras tidak ditemukan.");
            }
            Long jumlahTransaksi = s.createQuery(
                    "select count(t) from TransaksiStok t where t.beras.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (jumlahTransaksi != null && jumlahTransaksi > 0) {
                throw new IllegalStateException(
                        "Beras ini sudah punya riwayat transaksi, tidak bisa dihapus.");
            }
            s.remove(b);
            return null;
        });
    }

    // ---------- Fitur 2 & 3: Barang Masuk / Barang Keluar ----------

    public void catatMasuk(Long berasId, int jumlah, String keterangan) {
        catat(berasId, jumlah, keterangan, JenisTransaksi.MASUK);
    }

    public void catatKeluar(Long berasId, int jumlah, String keterangan) {
        catat(berasId, jumlah, keterangan, JenisTransaksi.KELUAR);
    }

    private void catat(Long berasId, int jumlah, String keterangan, JenisTransaksi jenis) {
        if (jumlah <= 0) {
            throw new IllegalStateException("Jumlah harus lebih dari 0.");
        }
        tx(s -> {
            // Kunci baris agar stok tidak bentrok jika diubah bersamaan
            Beras b = s.find(Beras.class, berasId, LockModeType.PESSIMISTIC_WRITE);
            if (b == null) {
                throw new IllegalStateException("Data beras tidak ditemukan.");
            }
            if (jenis == JenisTransaksi.KELUAR) {
                if (b.getStok() < jumlah) {
                    throw new IllegalStateException("Stok tidak cukup. Stok " + b.getNama()
                            + " saat ini " + b.getStok() + " kg.");
                }
                b.setStok(b.getStok() - jumlah);
            } else {
                b.setStok(b.getStok() + jumlah);
            }
            s.persist(new TransaksiStok(b, jenis, jumlah, LocalDateTime.now(), keterangan));
            return null;
        });
    }

    public List<TransaksiStok> riwayat(JenisTransaksi jenis) {
        return tx(s -> s.createQuery(
                "select t from TransaksiStok t join fetch t.beras "
                + "where t.jenis = :j order by t.tanggal desc", TransaksiStok.class)
                .setParameter("j", jenis)
                .list());
    }
}
