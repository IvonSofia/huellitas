package com.example.huellitas.network.dto

/**
 * Envoltorio genérico que coincide con la estructura JSON del backend PHP:
 * { "status": true, "message": "...", "data": ... }
 */
data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)
