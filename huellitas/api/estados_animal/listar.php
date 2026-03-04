<?php

/**
 * API Endpoint: Listar todos los estados de animal
 * Método: GET
 * URL: /api/estados_animal/listar.php
 */

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/EstadoAnimal.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    Response::error('Método no permitido. Use GET.', 405);
}

try {
    $estadoAnimal = new EstadoAnimal();
    $estados = $estadoAnimal->obtenerTodos();

    Response::success($estados, 'Estados de animal obtenidos correctamente');
} catch (Exception $e) {
    Response::error('Error al obtener los estados de animal', 500);
}
