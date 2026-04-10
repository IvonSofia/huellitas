package com.example.huellitas.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Cuerpo de la solicitud POST para registrar un usuario.
 * Mapea al body JSON esperado por /api/auth/registrar.php
 */
data class RegistroUsuarioRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)
