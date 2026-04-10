<?php

/**
 * API Endpoint: Iniciar sesión
 * Método: POST
 * URL: /api/auth/login.php
 * Body: { "correo": "...", "password": "..." }
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
if (empty($input['correo']) || empty($input['password'])) {
    Response::error('El correo y la contraseña son obligatorios.');
}

$correo = trim($input['correo']);
$password = $input['password'];

// Validar formato de correo
if (!filter_var($correo, FILTER_VALIDATE_EMAIL)) {
    Response::error('El formato del correo no es válido.');
}

try {
    $usuario = new Usuario();
    $datos = $usuario->obtenerPorCorreo($correo);

    if (!$datos) {
        Response::error('Credenciales incorrectas.', 401);
    }

    if (!password_verify($password, $datos['password'])) {
        Response::error('Credenciales incorrectas.', 401);
    }

    // No devolver el hash de la contraseña
    unset($datos['password']);

    Response::success($datos, 'Inicio de sesión exitoso.');

} catch (Exception $e) {
    Response::error('Error interno del servidor.', 500);
}
