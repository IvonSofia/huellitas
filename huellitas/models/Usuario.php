<?php

declare(strict_types=1);

require_once __DIR__ . '/../config/Database.php';

/**
 * Clase Usuario - Modelo para la tabla usuarios.
 * Encapsula operaciones de autenticación y registro.
 */
class Usuario
{
    private PDO $db;
    private string $table = 'usuarios';

    public ?int $id = null;
    public string $nombre;
    public string $apellidos;
    public string $correo;
    public string $password;
    public int $rol_id = 1;

    public function __construct()
    {
        $this->db = Database::getInstance()->getConnection();
    }

    /**
     * Buscar usuario por correo electrónico.
     *
     * @param string $correo Correo del usuario.
     * @return array|false Datos del usuario o false si no existe.
     */
    public function obtenerPorCorreo(string $correo): array|false
    {
        $query = "SELECT u.id, u.nombre, u.apellidos, u.correo, u.password,
                         u.rol_id, r.nombre AS rol, u.created_at, u.updated_at
                  FROM {$this->table} u
                  INNER JOIN roles r ON u.rol_id = r.id
                  WHERE u.correo = :correo
                  LIMIT 1";

        $stmt = $this->db->prepare($query);
        $stmt->bindValue(':correo', $correo, PDO::PARAM_STR);
        $stmt->execute();

        return $stmt->fetch();
    }

    /**
     * Registrar un nuevo usuario.
     *
     * @return int ID del usuario creado.
     * @throws PDOException Si hay error de base de datos.
     */
    public function crear(): int
    {
        $query = "INSERT INTO {$this->table} (nombre, apellidos, correo, password, rol_id)
                  VALUES (:nombre, :apellidos, :correo, :password, :rol_id)";

        $stmt = $this->db->prepare($query);
        $stmt->bindValue(':nombre', $this->nombre, PDO::PARAM_STR);
        $stmt->bindValue(':apellidos', $this->apellidos, PDO::PARAM_STR);
        $stmt->bindValue(':correo', $this->correo, PDO::PARAM_STR);
        $stmt->bindValue(':password', $this->password, PDO::PARAM_STR);
        $stmt->bindValue(':rol_id', $this->rol_id, PDO::PARAM_INT);
        $stmt->execute();

        return (int) $this->db->lastInsertId();
    }

    /**
     * Verificar si un correo ya está registrado.
     *
     * @param string $correo Correo a verificar.
     * @return bool True si el correo ya existe.
     */
    public function existeCorreo(string $correo): bool
    {
        $query = "SELECT COUNT(*) FROM {$this->table} WHERE correo = :correo";
        $stmt = $this->db->prepare($query);
        $stmt->bindValue(':correo', $correo, PDO::PARAM_STR);
        $stmt->execute();

        return (int) $stmt->fetchColumn() > 0;
    }
}
