package com.example.huellitas.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Cuerpo de la solicitud POST para iniciar sesión.
 * Mapea al body JSON esperado por /api/auth/login.php
 */
data class LoginRequest(
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)
