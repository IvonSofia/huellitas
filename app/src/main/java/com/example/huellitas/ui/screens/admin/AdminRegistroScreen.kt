package com.example.huellitas.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huellitas.ui.theme.GradientEnd
import com.example.huellitas.ui.theme.GradientStart
import com.example.huellitas.ui.theme.HuellitasTheme
import com.example.huellitas.viewmodel.AuthViewModel
import com.example.huellitas.viewmodel.EstadoAuth

/**
 * Pantalla de registro de usuario administrador.
 *
 * @param alRegistroExitoso Callback al registrarse correctamente
 * @param alIrALogin Callback para volver al login
 * @param authViewModel ViewModel compartido de autenticación
 */
@Composable
fun PantallaRegistroUsuario(
    alRegistroExitoso: () -> Unit,
    alIrALogin: () -> Unit,
    authViewModel: AuthViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Desactivar Force Dark del sistema (Xiaomi/MIUI) a nivel de View
    LocalView.current.setForceDarkAllowed(false)
    var confirmarContrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val estadoAuth by authViewModel.estado.collectAsState()

    // Colores forzados para inputs (evita que el modo oscuro los sobreescriba)
    val coloresInput = OutlinedTextFieldDefaults.colors(
        focusedTextColor = GradientStart,
        unfocusedTextColor = GradientStart,
        cursorColor = GradientStart,
        focusedBorderColor = GradientStart,
        unfocusedBorderColor = Color(0xFFCAC4D0),
        focusedContainerColor = Color(0xFFFAF5FF),
        unfocusedContainerColor = Color(0xFFFAF5FF),
        focusedPlaceholderColor = Color(0xFFB39DDB),
        unfocusedPlaceholderColor = Color(0xFFB39DDB),
        focusedLeadingIconColor = GradientStart,
        unfocusedLeadingIconColor = GradientStart
    )

    LaunchedEffect(estadoAuth) {
        when (estadoAuth) {
            is EstadoAuth.Exito -> {
                authViewModel.resetearEstado()
                alRegistroExitoso()
            }
            is EstadoAuth.Error -> {
                mensajeError = (estadoAuth as EstadoAuth.Error).mensaje
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(GradientStart, GradientEnd)
                )
            )
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ── Encabezado ──
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\uD83D\uDCDD Registro de administrador",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Card de formulario ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ── Campo: Nombre ──
                    Text(
                        text = "Nombre",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1A20)
                    )
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            mensajeError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAF5FF), RoundedCornerShape(12.dp)),
                        placeholder = { Text("Ingresa tu nombre", color = Color(0xFF8A7A9E)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = GradientStart),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = coloresInput
                    )

                    // ── Campo: Apellidos ──
                    Text(
                        text = "Apellidos",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1A20)
                    )
                    OutlinedTextField(
                        value = apellidos,
                        onValueChange = {
                            apellidos = it
                            mensajeError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAF5FF), RoundedCornerShape(12.dp)),
                        placeholder = { Text("Ingresa tus apellidos", color = Color(0xFF8A7A9E)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Badge,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = GradientStart),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = coloresInput
                    )

                    // ── Campo: Correo ──
                    Text(
                        text = "Correo",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1A20)
                    )
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            mensajeError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAF5FF), RoundedCornerShape(12.dp)),
                        placeholder = { Text("Ingresa tu correo", color = Color(0xFF8A7A9E)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = GradientStart),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = coloresInput
                    )

                    // ── Campo: Contraseña ──
                    Text(
                        text = "Contraseña",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1A20)
                    )
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            mensajeError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAF5FF), RoundedCornerShape(12.dp)),
                        placeholder = { Text("Crea una contraseña", color = Color(0xFF8A7A9E)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = GradientStart),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = coloresInput
                    )

                    // ── Campo: Confirmar contraseña ──
                    Text(
                        text = "Confirmar contraseña",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1D1A20)
                    )
                    OutlinedTextField(
                        value = confirmarContrasena,
                        onValueChange = {
                            confirmarContrasena = it
                            mensajeError = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAF5FF), RoundedCornerShape(12.dp)),
                        placeholder = { Text("Repite la contraseña", color = Color(0xFF8A7A9E)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        textStyle = LocalTextStyle.current.copy(color = GradientStart),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = coloresInput
                    )

                    // ── Mensaje de error ──
                    if (mensajeError != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            )
                        ) {
                            Text(
                                text = "⚠\uFE0F ${ mensajeError }",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFB71C1C),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            )
                        }
                    }

                    // ── Botón Registrarse ──
                    val camposCompletos = nombre.isNotBlank() && apellidos.isNotBlank()
                            && correo.isNotBlank() && contrasena.isNotBlank()
                            && confirmarContrasena.isNotBlank()

                    Button(
                        onClick = {
                            mensajeError = null
                            if (contrasena != confirmarContrasena) {
                                mensajeError = "Las contraseñas no coinciden"
                                return@Button
                            }
                            if (contrasena.length < 4) {
                                mensajeError = "La contraseña debe tener al menos 4 caracteres"
                                return@Button
                            }
                            authViewModel.registrar(
                                nombre.trim(),
                                apellidos.trim(),
                                correo.trim(),
                                contrasena
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GradientStart,
                            contentColor = Color.White,
                            disabledContainerColor = GradientStart.copy(alpha = 0.6f),
                            disabledContentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        enabled = camposCompletos && estadoAuth !is EstadoAuth.Cargando
                    ) {
                        if (estadoAuth is EstadoAuth.Cargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Registrarse",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    // ── Link a login ──
                    TextButton(
                        onClick = alIrALogin,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "¿Ya tienes cuenta? Inicia sesión",
                            color = GradientStart,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Volver a la aplicación ──
            TextButton(onClick = alIrALogin) {
                Text(
                    text = "← Volver al inicio de sesión",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaRegistroUsuarioPreview() {
    HuellitasTheme {
        PantallaRegistroUsuario(
            alRegistroExitoso = {},
            alIrALogin = {},
            authViewModel = AuthViewModel()
        )
    }
}
