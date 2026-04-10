package com.example.huellitas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huellitas.network.dto.UsuarioDto
import com.example.huellitas.repository.AuthRepository
import com.example.huellitas.repository.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado de UI para las pantallas de autenticación.
 */
sealed class EstadoAuth {
    data object Inactivo : EstadoAuth()
    data object Cargando : EstadoAuth()
    data class Exito(val usuario: UsuarioDto) : EstadoAuth()
    data class Error(val mensaje: String) : EstadoAuth()
}

/**
 * ViewModel compartido para login y registro de usuarios.
 */
class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<EstadoAuth>(EstadoAuth.Inactivo)
    val estado: StateFlow<EstadoAuth> = _estado.asStateFlow()

    /**
     * Inicia sesión con correo y contraseña.
     */
    fun login(correo: String, password: String) {
        viewModelScope.launch {
            _estado.value = EstadoAuth.Cargando
            when (val resultado = repository.login(correo, password)) {
                is Resultado.Exito -> _estado.value = EstadoAuth.Exito(resultado.datos)
                is Resultado.Error -> _estado.value = EstadoAuth.Error(resultado.mensaje)
            }
        }
    }

    /**
     * Registra un nuevo usuario.
     */
    fun registrar(nombre: String, apellidos: String, correo: String, password: String) {
        viewModelScope.launch {
            _estado.value = EstadoAuth.Cargando
            when (val resultado = repository.registrar(nombre, apellidos, correo, password)) {
                is Resultado.Exito -> _estado.value = EstadoAuth.Exito(resultado.datos)
                is Resultado.Error -> _estado.value = EstadoAuth.Error(resultado.mensaje)
            }
        }
    }

    /**
     * Resetea el estado a inactivo (para navegación).
     */
    fun resetearEstado() {
        _estado.value = EstadoAuth.Inactivo
    }
}
