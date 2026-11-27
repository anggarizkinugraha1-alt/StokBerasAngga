package com.beras.stokberas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Memetakan tabel "pengguna" yang sudah ada (kolom username & password). */
@Entity
@Table(name = "pengguna")
public class Pengguna {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public Pengguna() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
