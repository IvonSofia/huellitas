<?php

//  API Endpoint: Listar todos los animales
//  Método: GET
//  URL: /api/animales/listar.php

declare(strict_types=1);

header('Content-Type: application/json; charset=UTF-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET');

require_once __DIR__ . '/../../helpers/Response.php';
require_once __DIR__ . '/../../models/Animal.php';

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    Response::error('Método no permitido. Use GET.', 405);
}

try {
    $animal = new Animal();

    $limite = isset($_GET['limite']) ? max(1, (int) $_GET['limite']) : 10;
    $pagina = isset($_GET['pagina']) ? max(1, (int) $_GET['pagina']) : 1;
    $desplazamiento = ($pagina - 1) * $limite;

    $idTipo = isset($_GET['tipo']) ? (int) $_GET['tipo'] : null;

    if ($idTipo !== null && $idTipo > 0) {
        $animales = $animal->obtenerPorTipo($idTipo, $limite, $desplazamiento);
    } else {
        $orden = $_GET['orden'] ?? 'fecha_registro';
        $direccion = $_GET['direccion'] ?? 'DESC';
        $animales = $animal->obtenerTodos($orden, $direccion, $limite, $desplazamiento);
    }

    Response::success($animales, 'Animales obtenidos correctamente');
} catch (Exception $e) {
    Response::error('Error al obtener los animales', 500);
}