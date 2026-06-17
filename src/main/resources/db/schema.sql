-- =====================================================
-- DDL para crear el esquema completo de UNIR Supplies Users
-- =====================================================

-- Crear el schema users
CREATE SCHEMA IF NOT EXISTS supplies_users;

-- Usar el schema users
USE supplies_users;

-- Crear la tabla usuarios con nombres en inglés
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       phone VARCHAR(15),
                       address VARCHAR(255),
                       cif VARCHAR(20) UNIQUE,
                       sector VARCHAR(50),
                       employees INT,
                       founded_year INT,
                       password VARCHAR(32) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_cif ON users(cif);


-- Sentencias INSERT para la tabla users
INSERT INTO users (id, name, email, phone, address, cif, sector, employees, founded_year, password) VALUES
                                                                                                        (1, 'TechStore Madrid', 'contacto@techstore.com', '+34 91 123 4567', 'Calle Gran Vía, 28, 28013 Madrid', 'B12345678', 'Tecnología', 45, 2018, MD5('123456')),
                                                                                                        (2, 'Digital Office Solutions', 'info@digitaloffice.es', '+34 93 987 6543', 'Passeig de Gràcia, 101, 08008 Barcelona', 'B87654321', 'Consultoría IT', 78, 2015, MD5('password123')),
                                                                                                        (3, 'InnovaCorp Sistemas', 'admin@innovacorp.com', '+34 94 456 7890', 'Alameda de Recalde, 27, 48009 Bilbao', 'B11223344', 'Software Development', 32, 2020, MD5('admin2025'));