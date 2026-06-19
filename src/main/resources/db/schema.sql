-- =====================================================
-- DDL para crear el esquema completo de UNIR Supplies Users
-- =====================================================

-- Crear el schema users
CREATE SCHEMA IF NOT EXISTS user_db;

-- Usar el schema users
USE user_db;

-- Crear la tabla usuarios con nombres en inglés
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       phone VARCHAR(15),
                       address VARCHAR(255),
                       cif VARCHAR(20) UNIQUE,                       
                       password VARCHAR(32) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_cif ON users(cif);


-- Sentencias INSERT para la tabla users
INSERT INTO users (id, name, email, phone, address, cif, password) VALUES
(1, 'Sofia', 'sofia@gmail.com', '+34 91 123 4567', 'Calle Gran Vía, 28, 28013 Madrid', '1235521', MD5('123456'));
                                                                                                        