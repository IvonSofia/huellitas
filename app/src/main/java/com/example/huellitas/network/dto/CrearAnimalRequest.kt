package com.example.huellitas.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Cuerpo de la solicitud POST para crear un nuevo animal.
 * Mapea al body JSON esperado por /api/animales/crear.php
 */
data class CrearAnimalRequest(
    @SerializedName("nombre")          val nombre: String?,
    @SerializedName("id_tipo_animal")  val idTipoAnimal: Int,
    @SerializedName("raza")            val raza: String?,
    @SerializedName("descripcion")     val descripcion: String?,
    @SerializedName("ubicacion")       val ubicacion: String,
    @SerializedName("contacto")        val contacto: String,
    @SerializedName("imagen_url")      val imagenUrl: String? = null
)
