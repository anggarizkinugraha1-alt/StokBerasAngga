-- Database untuk Manajemen Stok Beras
CREATE DATABASE IF NOT EXISTS beras;
USE beras;

CREATE TABLE IF NOT EXISTS pengguna (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

-- Akun contoh
INSERT INTO pengguna (username, password) VALUES ('admin', 'admin123');
