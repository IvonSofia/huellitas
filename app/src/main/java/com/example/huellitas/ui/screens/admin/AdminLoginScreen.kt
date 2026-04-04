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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.huellitas.ui.theme.GradientEnd
import com.example.huellitas.ui.theme.GradientStart
import com.example.huellitas.ui.theme.HuellitasTheme

/** Credenciales de administrador (hardcoded para demo) */
private const val ADMIN_CORREO = "admin@gmail.com"
private const val ADMIN_CONTRASENA = "admin"

/**
 * Pantalla de inicio de sesión del panel de administración.
 *
 * Valida credenciales contra valores fijos de demostración.
 * En producción, esto debería conectarse a un endpoint seguro.
 *
 * @param alIniciarSesion Callback al autenticarse correctamente
 * @param alVolver Callback para regresar al feed principal
 */
@Composable
fun PantallaLoginAdmin(
    alIniciarSesion: () -> Unit,
    alVolver: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

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
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Encabezado ──
            Text(
                text = "Panel de Administración",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\uD83D\uDD12 Acceso solo para personal autorizado",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
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
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ingresa tu correo") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientStart,
                            unfocusedBorderColor = Color(0xFFCAC4D0)
                        )
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
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ingresa tu contraseña") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = GradientStart
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GradientStart,
                            unfocusedBorderColor = Color(0xFFCAC4D0)
                        )
                    )

                    // ── Mensaje de error ──
                    if (mensajeError != null) {
                        Text(
                            text = mensajeError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // ── Botón Iniciar sesión ──
                    Button(
                        onClick = {
                            if (correo.trim() == ADMIN_CORREO && contrasena == ADMIN_CONTRASENA) {
                                alIniciarSesion()
                            } else {
                                mensajeError = "Credenciales incorrectas"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GradientStart,
                            contentColor = Color.White
                        ),
                        enabled = correo.isNotBlank() && contrasena.isNotBlank()
                    ) {
                        Text(
                            text = "Iniciar sesión",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // ── Pista de credenciales ──
                    Text(
                        text = "\uD83D\uDCA1 Correo por defecto: admin@gmail.com\nContraseña: admin",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF49454F),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Volver a la aplicación ──
            TextButton(onClick = alVolver) {
                Text(
                    text = "← Volver a la aplicación",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaLoginAdminPreview() {
    HuellitasTheme {
        PantallaLoginAdmin(alIniciarSesion = {}, alVolver = {})
    }
}
