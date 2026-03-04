<?php

/**
 * API Endpoint: Listar todos los tipos de animal
 * Método: GET
 * URL: /api/tipos_animal/listar.php
 */

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/TipoAnimal.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    Response::error('Método no permitido. Use GET.', 405);
}

try {
    $tipoAnimal = new TipoAnimal();
    $tipos = $tipoAnimal->obtenerTodos();

    Response::success($tipos, 'Tipos de animal obtenidos correctamente');
} catch (Exception $e) {
    Response::error('Error al obtener los tipos de animal', 500);
}
