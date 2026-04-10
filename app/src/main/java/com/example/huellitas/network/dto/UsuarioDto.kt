package com.example.huellitas.network.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa la respuesta JSON de la API PHP para un usuario.
 */
data class UsuarioDto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("rol_id") val rolId: Int,
    @SerializedName("rol") val rol: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
