<?php

/**
 * API Endpoint: Actualizar un animal existente
 * Método: PUT
 * URL: /api/animales/actualizar.php
 * 
 */

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: PUT, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/Animal.php';

if ($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    Response::error('Método no permitido. Use PUT.', 405);
}

$input = json_decode(file_get_contents('php://input'), true);

if ($input === null) {
    Response::error('El cuerpo de la solicitud debe ser JSON válido.');
}

// Validar campos obligatorios
$camposObligatorios = ['id', 'id_tipo_animal', 'ubicacion', 'contacto'];
$errores = [];

foreach ($camposObligatorios as $campo) {
    if (!isset($input[$campo]) || (is_string($input[$campo]) && trim($input[$campo]) === '')) {
        $errores[] = "El campo '{$campo}' es obligatorio.";
    }
}

if (!empty($errores)) {
    Response::error(implode(' ', $errores));
}

try {
    $animal = new Animal();

    // Verificar que el animal existe
    $animalExistente = $animal->obtenerPorId((int) $input['id']);
    if (!$animalExistente) {
        Response::error('Animal no encontrado.', 404);
    }

    $animal->id = (int) $input['id'];
    $animal->nombre = isset($input['nombre']) ? trim($input['nombre']) : null;
    $animal->id_tipo_animal = (int) $input['id_tipo_animal'];
    $animal->raza = isset($input['raza']) ? trim($input['raza']) : null;
    $animal->descripcion = isset($input['descripcion']) ? trim($input['descripcion']) : null;
    $animal->ubicacion = trim($input['ubicacion']);
    $animal->contacto = trim($input['contacto']);
    $animal->id_estado = isset($input['id_estado']) ? (int) $input['id_estado'] : (int) $animalExistente['id_estado'];

    $animal->actualizar();
    $animalActualizado = $animal->obtenerPorId($animal->id);

    Response::success($animalActualizado, 'Animal actualizado correctamente');
} catch (Exception $e) {
    Response::error('Error al actualizar el animal', 500);
}
