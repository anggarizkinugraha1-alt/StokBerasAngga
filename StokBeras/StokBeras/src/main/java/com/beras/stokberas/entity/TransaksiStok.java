package com.beras.stokberas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Riwayat beras masuk / keluar. */
@Entity
@Table(name = "transaksi_stok")
public class TransaksiStok {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "beras_id")
    private Beras beras;

    /** "MASUK" atau "KELUAR". */
    @Column(nullable = false, length = 10)
    private String jenis;

    @Column(nullable = false)
    private int jumlah;

    @Column(nullable = false)
    private LocalDateTime tanggal;

    public TransaksiStok() {
    }

    public TransaksiStok(Beras beras, String jenis, int jumlah, LocalDateTime tanggal) {
        this.beras = beras;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
    }

    public Long getId() {
        return id;
    }

    public Beras getBeras() {
        return beras;
    }

    public String getJenis() {
        return jenis;
    }

    public int getJumlah() {
        return jumlah;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    // Getter khusus laporan (tidak disimpan ke database)
    public String getNamaBeras() {
        return beras.getNama();
    }

    public String getWaktu() {
        return tanggal.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
}
