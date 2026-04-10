-- ============================================================
-- MIGRACIÓN: Agregar tablas de roles y usuarios + relación con animales
-- Base de datos: huellitas_db
-- Fecha: 2026-04-09
-- ============================================================

USE huellitas_db;

-- ============================================================
-- TABLA: roles
-- ============================================================
CREATE TABLE IF NOT EXISTS roles (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================================
-- TABLA: usuarios
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellidos   VARCHAR(100) NOT NULL,
    correo      VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL COMMENT 'Hash bcrypt de la contraseña',
    rol_id      INT NOT NULL DEFAULT 1,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (rol_id) REFERENCES roles(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================
-- ÍNDICES
-- ============================================================
CREATE INDEX idx_usuarios_correo ON usuarios(correo);
CREATE INDEX idx_usuarios_rol ON usuarios(rol_id);

-- ============================================================
-- Agregar columna id_usuario a animales (FK nullable)
-- NULL = registrado por usuario público
-- valor = registrado por admin autenticado
-- ============================================================
ALTER TABLE animales
    ADD COLUMN id_usuario INT NULL COMMENT 'NULL = registrado por usuario público, valor = admin autenticado'
    AFTER id_estado;

ALTER TABLE animales
    ADD CONSTRAINT fk_animal_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
    ON DELETE SET NULL ON UPDATE CASCADE;

CREATE INDEX idx_animales_usuario ON animales(id_usuario);

-- ============================================================
-- DATOS INICIALES
-- ============================================================

-- Rol de administrador
INSERT INTO roles (nombre, descripcion) VALUES
    ('Admin', 'Administrador con acceso completo al panel de gestión');

-- Usuario administrador por defecto (contraseña: admin)
-- Hash generado con: password_hash('admin', PASSWORD_BCRYPT)
INSERT INTO usuarios (nombre, apellidos, correo, password, rol_id) VALUES
    ('Admin', 'Sistema', 'admin@gmail.com', '$2y$10$XqOFszZtKYgB2Kbv9/1a0OMaacEyfOpM5n1r.f8dn3tU6MffGoO0W', 1);
