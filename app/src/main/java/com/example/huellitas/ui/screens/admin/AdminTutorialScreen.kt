package com.example.huellitas.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.huellitas.ui.components.IndicadorPagina
import com.example.huellitas.ui.theme.GradientStart
import com.example.huellitas.ui.theme.HuellitasTheme
import kotlinx.coroutines.launch

/**
 * Datos de cada paso del tutorial de administración.
 */
private data class PasoAdmin(
    val icono: ImageVector,
    val colorFondo: Color,
    val titulo: String,
    val descripcion: String,
    val puntos: List<String>
)

/** Los 6 pasos del tutorial de administración */
private val pasosAdmin = listOf(
    PasoAdmin(
        icono = Icons.Outlined.Email,
        colorFondo = Color(0xFFE8DEF8),
        titulo = "1. Bandeja de Entrada",
        descripcion = "Aquí verás todos los reportes de animales. Puedes filtrarlos por estado: Nuevos, En Progreso o Resueltos.",
        puntos = listOf(
            "\uD83D\uDCC4 Los reportes aparecen ordenados por fecha",
            "\u2699\uFE0F Usa los filtros para organizar mejor",
            "\uD83D\uDD34 Los casos urgentes tienen un punto rojo",
            "\uD83D\uDCCA Las estadísticas te muestran el resumen"
        )
    ),
    PasoAdmin(
        icono = Icons.Outlined.RemoveRedEye,
        colorFondo = Color(0xFFD6E4FF),
        titulo = "2. Ver Detalles",
        descripcion = "Haz clic en cualquier reporte para ver toda la información: foto, ubicación, descripción y datos de contacto.",
        puntos = listOf(
            "\uD83D\uDCCB Cada reporte tiene información completa",
            "\uD83D\uDDBC\uFE0F Puedes ver las fotos en grande",
            "\uD83D\uDCCD La ubicación exacta donde fue visto",
            "\uD83D\uDCF1 Los datos de quien reportó"
        )
    ),
    PasoAdmin(
        icono = Icons.Outlined.Forum,
        colorFondo = Color(0xFFD5F5E3),
        titulo = "3. Contactar por WhatsApp",
        descripcion = "Desde cada reporte puedes contactar a quien lo registró. También puedes generar un PDF para compartir.",
        puntos = listOf(
            "\uD83D\uDCAC Botón de WhatsApp directo",
            "\uD83D\uDCC4 Genera PDF con la información",
            "\u2705 El mensaje incluye datos del caso",
            "\uD83D\uDE80 Responde rápido a emergencias"
        )
    ),
    PasoAdmin(
        icono = Icons.Outlined.Sync,
        colorFondo = Color(0xFFFFF9C4),
        titulo = "4. Gestionar Estado",
        descripcion = "Actualiza el estado del reporte según el progreso: Nuevo, En Progreso o Resuelto. Marca casos urgentes.",
        puntos = listOf(
            "\uD83C\uDD95 Nuevo: recién reportado",
            "\u23F3 En Progreso: ya estás trabajando",
            "\u2705 Resuelto: caso cerrado exitosamente",
            "\u26A0\uFE0F Marca urgentes para priorizar"
        )
    ),
    PasoAdmin(
        icono = Icons.Outlined.Send,
        colorFondo = Color(0xFFD6E4FF),
        titulo = "5. Derivar a Instituciones",
        descripcion = "Envía casos a veterinarias, refugios o autoridades directamente por WhatsApp con toda la información.",
        puntos = listOf(
            "\uD83C\uDFE5 Envía a veterinarias cercanas",
            "\uD83E\uDDD1\u200D\uD83E\uDD1D\u200D\uD83E\uDDD1 Deriva a refugios o rescatistas",
            "\uD83D\uDC6E Contacta autoridades si es necesario",
            "\uD83D\uDCC4 El mensaje incluye todos los datos"
        )
    ),
    PasoAdmin(
        icono = Icons.Outlined.Settings,
        colorFondo = Color(0xFFFFE0E0),
        titulo = "6. Configurar Contactos",
        descripcion = "Gestiona la lista de instituciones aliadas: veterinarias, refugios y autoridades con sus WhatsApp.",
        puntos = listOf(
            "\u2795 Agrega nuevas instituciones",
            "\u270F\uFE0F Edita información de contactos",
            "\uD83D\uDDD1\uFE0F Elimina instituciones inactivas",
            "\uD83D\uDCDE Mantén actualizados los números"
        )
    )
)

/**
 * Pantalla de tutorial de administración con 6 pasos.
 *
 * Sigue el mismo patrón de navegación que el tutorial de registro:
 * Regresar/Anterior + Continuar/Comenzar.
 *
 * @param alFinalizar Callback al completar o saltar el tutorial
 * @param alRegresar Callback para volver atrás (null = usa alFinalizar)
 */
@Composable
fun PantallaTutorialAdmin(
    alFinalizar: () -> Unit,
    alRegresar: (() -> Unit)? = null
) {
    val pagerState = rememberPagerState(pageCount = { pasosAdmin.size })
    val scope = rememberCoroutineScope()
    val esUltimaPagina = pagerState.currentPage == pasosAdmin.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // ── Encabezado fijo ──
        Text(
            text = "\uD83C\uDF93 ¡Aprende a Administrar!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1A20),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Te enseñaremos a usar todas las herramientas",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF49454F),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Indicador de página ──
        IndicadorPagina(
            totalPaginas = pasosAdmin.size,
            paginaActual = pagerState.currentPage,
            colorActivo = GradientStart
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Paso ${pagerState.currentPage + 1} de ${pasosAdmin.size}",
            style = MaterialTheme.typography.labelMedium,
            color = GradientStart,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Pager con las tarjetas ──
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pagina ->
            TarjetaPasoAdmin(paso = pasosAdmin[pagina])
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Botones de navegación ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón izquierdo: "Regresar" en la primera, "Anterior" en las demás
            if (pagerState.currentPage > 0) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "← Anterior",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { alRegresar?.invoke() ?: alFinalizar() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "← Regresar",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Botón derecho: "Continuar" o "Comenzar a Administrar"
            Button(
                onClick = {
                    if (esUltimaPagina) {
                        alFinalizar()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GradientStart,
                    contentColor = Color.White
                )
            ) {
                if (esUltimaPagina) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Comenzar a Administrar",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                } else {
                    Text(
                        text = "Continuar →",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── Saltar tutorial ──
        TextButton(onClick = alFinalizar) {
            Text(
                text = "Saltar Tutorial",
                color = GradientStart,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Tarjeta individual de un paso del tutorial admin.
 * Cada paso tiene su propio color de fondo para el ícono.
 */
@Composable
private fun TarjetaPasoAdmin(paso: PasoAdmin) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(
                width = 2.dp,
                color = GradientStart.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ── Ícono con fondo de color propio ──
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = paso.colorFondo
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = paso.icono,
                            contentDescription = paso.titulo,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF49454F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ── Título ──
                Text(
                    text = paso.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1A20),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Descripción ──
                Text(
                    text = paso.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF49454F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // ── Puntos clave ──
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = paso.colorFondo.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "\uD83D\uDCA1 Puntos clave:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = GradientStart,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        paso.puntos.forEach { punto ->
                            Text(
                                text = "  •  $punto",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF49454F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PantallaTutorialAdminPreview() {
    HuellitasTheme {
        PantallaTutorialAdmin(alFinalizar = {})
    }
}
