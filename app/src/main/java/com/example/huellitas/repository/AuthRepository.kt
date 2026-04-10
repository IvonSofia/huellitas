package com.example.huellitas.repository

import com.example.huellitas.network.RetrofitClient
import com.example.huellitas.network.dto.LoginRequest
import com.example.huellitas.network.dto.RegistroUsuarioRequest
import com.example.huellitas.network.dto.UsuarioDto

/**
 * Repositorio de autenticación. Fuente única de datos para login y registro.
 */
class AuthRepository {

    private val api = RetrofitClient.apiService

    /**
     * Inicia sesión con correo y contraseña.
     */
    suspend fun login(correo: String, password: String): Resultado<UsuarioDto> {
        return try {
            val response = api.login(LoginRequest(correo, password))
            if (response.isSuccessful && response.body()?.status == true) {
                val usuario = response.body()!!.data!!
                Resultado.Exito(usuario)
            } else {
                val mensaje = response.body()?.message ?: "Credenciales incorrectas"
                Resultado.Error(mensaje)
            }
        } catch (e: Exception) {
            Resultado.Error("Sin conexión: ${e.localizedMessage}")
        }
    }

    /**
     * Registra un nuevo usuario.
     */
    suspend fun registrar(
        nombre: String,
        apellidos: String,
        correo: String,
        password: String
    ): Resultado<UsuarioDto> {
        return try {
            val response = api.registrarUsuario(
                RegistroUsuarioRequest(nombre, apellidos, correo, password)
            )
            if (response.isSuccessful && response.body()?.status == true) {
                val usuario = response.body()!!.data!!
                Resultado.Exito(usuario)
            } else {
                val mensaje = response.body()?.message ?: "Error al registrar usuario"
                Resultado.Error(mensaje)
            }
        } catch (e: Exception) {
            Resultado.Error("Sin conexión: ${e.localizedMessage}")
        }
    }
}
