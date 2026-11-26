package com.beras.stokberas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "beras")
public class Beras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nama;

    /** Harga per kg (rupiah). */
    @Column(nullable = false)
    private long harga;

    /** Stok saat ini dalam kg. */
    @Column(nullable = false)
    private int stok;

    public Beras() {
    }

    public Beras(String nama, long harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public Long getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public long getHarga() {
        return harga;
    }

    public void setHarga(long harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return nama;
    }
}
