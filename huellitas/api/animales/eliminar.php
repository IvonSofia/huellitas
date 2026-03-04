<?php

/**
 * API Endpoint: Eliminar un animal
 * Método: DELETE
 * URL: /api/animales/eliminar.php
 */

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/Animal.php';

if ($_SERVER['REQUEST_METHOD'] !== 'DELETE') {
    Response::error('Método no permitido. Use DELETE.', 405);
}

$input = json_decode(file_get_contents('php://input'), true);

if ($input === null || !isset($input['id']) || !is_numeric($input['id'])) {
    Response::error('El campo "id" es obligatorio y debe ser numérico.');
}

try {
    $animal = new Animal();

    // Verificar que el animal existe
    $animalExistente = $animal->obtenerPorId((int) $input['id']);
    if (!$animalExistente) {
        Response::error('Animal no encontrado.', 404);
    }

    $animal->eliminar((int) $input['id']);

    Response::success(null, 'Animal eliminado correctamente');
} catch (Exception $e) {
    Response::error('Error al eliminar el animal', 500);
}
