package com.example.huellitas.ui.screens.onboarding

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
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
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
 * Datos de cada paso del tutorial.
 */
private data class PasoTutorial(
    val icono: ImageVector,
    val titulo: String,
    val descripcion: String,
    val consejosTitulo: String = "\uD83D\uDCA1 Consejos útiles:",
    val consejos: List<String>
)

/** Los 4 pasos del tutorial de registro */
private val pasosTutorial = listOf(
    PasoTutorial(
        icono = Icons.Outlined.CameraAlt,
        titulo = "1. Toma una foto clara",
        descripcion = "Fotografía al animal de frente, con buena luz. Esto ayuda a identificarlo mejor.",
        consejos = listOf(
            "\uD83D\uDCF7 Usa la cámara de tu celular",
            "\uD83C\uDF1E Busca buena luz natural",
            "\uD83C\uDFAC Toma la foto de frente"
        )
    ),
    PasoTutorial(
        icono = Icons.Outlined.LocationOn,
        titulo = "2. Indica la ubicación",
        descripcion = "Escribe dónde encontraste al animal. Sé lo más específico posible (calle, esquina, barrio).",
        consejos = listOf(
            "\uD83D\uDCCD Incluye nombre de la calle",
            "\uD83C\uDFD8\uFE0F Menciona el barrio o zona",
            "\uD83D\uDDFA\uFE0F Agrega referencias cercanas"
        )
    ),
    PasoTutorial(
        icono = Icons.Outlined.Description,
        titulo = "3. Describe al animal",
        descripcion = "Cuéntanos cómo es: color, tamaño, si tiene collar, si parece herido o asustado.",
        consejos = listOf(
            "\uD83D\uDC3E Describe su apariencia",
            "\u2764\uFE0F Menciona su comportamiento",
            "\uD83E\uDE79 Indica si está herido"
        )
    ),
    PasoTutorial(
        icono = Icons.Outlined.Phone,
        titulo = "4. Completa tus datos",
        descripcion = "Tu número de WhatsApp es opcional, pero nos ayuda a contactarte si necesitamos más información.",
        consejos = listOf(
            "\uD83D\uDCF1 Tu número es privado",
            "\u2705 Es completamente opcional",
            "\uD83D\uDCAC Usamos WhatsApp para contactar"
        )
    )
)

/**
 * Pantalla de tutorial que explica cómo registrar un animal
 * en 4 pasos con un HorizontalPager y navegación Anterior/Continuar.
 *
 * Al finalizar (último paso), el botón cambia a "Comenzar" y
 * navega al formulario de registro de animales.
 *
 * @param alFinalizar Callback que se invoca al completar o saltar el tutorial
 * @param alRegresar Callback para volver a la pantalla anterior (null = no mostrar)
 */
@Composable
fun PantallaTutorial(
    alFinalizar: () -> Unit,
    alRegresar: (() -> Unit)? = null
) {
    val pagerState = rememberPagerState(pageCount = { pasosTutorial.size })
    val scope = rememberCoroutineScope()
    val esUltimaPagina = pagerState.currentPage == pasosTutorial.size - 1

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
            text = "¡Aprende a Registrar!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D1A20),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Sigue estos pasos sencillos para ayudar a un peludito",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF49454F),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Indicador de página ──
        IndicadorPagina(
            totalPaginas = pasosTutorial.size,
            paginaActual = pagerState.currentPage,
            colorActivo = GradientStart
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Paso ${pagerState.currentPage + 1} de ${pasosTutorial.size}",
            style = MaterialTheme.typography.labelMedium,
            color = GradientStart,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Pager con las tarjetas de cada paso ──
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pagina ->
            TarjetaPasoTutorial(paso = pasosTutorial[pagina])
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Botones de navegación ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botón "Anterior" o "Regresar" (primera página)
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

            // Botón "Continuar" o "Comenzar" (última página)
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
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Comenzar",
                        fontWeight = FontWeight.Bold
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
 * Tarjeta individual de un paso del tutorial.
 * Muestra ícono, título, descripción y consejos útiles.
 */
@Composable
private fun TarjetaPasoTutorial(paso: PasoTutorial) {
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
            border = androidx.compose.foundation.BorderStroke(
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
                // ── Ícono grande con fondo suave ──
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = GradientStart.copy(alpha = 0.1f)
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

                // ── Título del paso ──
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

                // ── Sección de consejos ──
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = GradientStart.copy(alpha = 0.06f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = paso.consejosTitulo,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = GradientStart,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        paso.consejos.forEach { consejo ->
                            Text(
                                text = "  •  $consejo",
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
private fun PantallaTutorialPreview() {
    HuellitasTheme {
        PantallaTutorial(alFinalizar = {})
    }
}
