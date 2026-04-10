<?php

/**
 * API Endpoint: Registrar nuevo usuario
 * Método: POST
 * URL: /api/auth/registrar.php
 * Body: { "nombre": "...", "apellidos": "...", "correo": "...", "password": "..." }
 */

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/Usuario.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    Response::error('Método no permitido. Use POST.', 405);
}

$input = json_decode(file_get_contents('php://input'), true);

if ($input === null) {
    Response::error('El cuerpo de la solicitud debe ser JSON válido.');
}

// Validar campos obligatorios
$camposObligatorios = ['nombre', 'apellidos', 'correo', 'password'];
$errores = [];

foreach ($camposObligatorios as $campo) {
    if (!isset($input[$campo]) || trim((string) $input[$campo]) === '') {
        $errores[] = "El campo '{$campo}' es obligatorio.";
    }
}

if (!empty($errores)) {
    Response::error(implode(' ', $errores));
}

$nombre = trim($input['nombre']);
$apellidos = trim($input['apellidos']);
$correo = trim($input['correo']);
$password = $input['password'];

// Validaciones
if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
    Response::error('El formato del correo no es válido.');
}

if (strlen($password) < 4) {
    Response::error('La contraseña debe tener al menos 4 caracteres.');
}

if (strlen($nombre) > 100 || strlen($apellidos) > 100) {
    Response::error('El nombre y apellidos no pueden exceder 100 caracteres.');
}

try {
    $usuario = new Usuario();

    // Verificar si el correo ya existe
    if ($usuario->existeCorreo($correo)) {
        Response::error('El correo ya está registrado.', 409);
    }

    // Crear usuario con password hasheado
    $usuario->nombre = $nombre;
    $usuario->apellidos = $apellidos;
    $usuario->correo = $correo;
    $usuario->password = password_hash($password, PASSWORD_BCRYPT);
    $usuario->rol_id = 1; // Admin por defecto

    $id = $usuario->crear();

    // Devolver datos del usuario creado (sin password)
    $datosUsuario = $usuario->obtenerPorCorreo($correo);
    unset($datosUsuario['password']);

    Response::success($datosUsuario, 'Usuario registrado exitosamente.', 201);

} catch (Exception $e) {
    Response::error('Error interno del servidor.', 500);
}
