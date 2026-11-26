package com.beras.stokberas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaksi_stok")
public class TransaksiStok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beras_id")
    private Beras beras;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private JenisTransaksi jenis;

    @Column(nullable = false)
    private int jumlah;

    @Column(nullable = false)
    private LocalDateTime tanggal;

    @Column(length = 255)
    private String keterangan;

    public TransaksiStok() {
    }

    public TransaksiStok(Beras beras, JenisTransaksi jenis, int jumlah,
            LocalDateTime tanggal, String keterangan) {
        this.beras = beras;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
    }

    public Long getId() {
        return id;
    }

    public Beras getBeras() {
        return beras;
    }

    public JenisTransaksi getJenis() {
        return jenis;
    }

    public int getJumlah() {
        return jumlah;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }
}
