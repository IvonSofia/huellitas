package com.example.huellitas.ui.screens.camera

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Pantalla de cámara integrada en la aplicación.
 * Muestra la vista previa de la cámara con controles para:
 * - Capturar foto
 * - Abrir galería
 * - Activar/desactivar flash
 * - Cambiar entre cámara frontal/trasera
 *
 * @param alCapturarFoto Callback con el Uri de la foto capturada
 * @param alSeleccionarGaleria Callback para abrir la galería del dispositivo
 * @param alCerrar Callback para cerrar la cámara sin seleccionar imagen
 */
@Composable
fun PantallaCamara(
    alCapturarFoto: (Uri) -> Unit,
    alSeleccionarGaleria: () -> Unit,
    alCerrar: () -> Unit
) {
    val context = LocalContext.current
    @Suppress("DEPRECATION")
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current

    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashActivo by remember { mutableStateOf(false) }
    var capturando by remember { mutableStateOf(false) }
    var cameraRef by remember { mutableStateOf<Camera?>(null) }

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // ── Vincular cámara al lifecycle ──
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.obtenerCameraProvider()
        cameraProvider.unbindAll()

        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            val camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            cameraRef = camera
        } catch (_: Exception) { }
    }

    // ── Actualizar flash/torch ──
    LaunchedEffect(flashActivo, cameraRef) {
        cameraRef?.cameraControl?.enableTorch(flashActivo)
    }

    // ── Desvincular cámara al salir de la composición ──
    DisposableEffect(Unit) {
        onDispose {
            try {
                ProcessCameraProvider.getInstance(context).get().unbindAll()
            } catch (_: Exception) { }
        }
    }

    // Manejar botón atrás del sistema
    BackHandler { alCerrar() }

    // ══════════════════════════════════════════════════════════════════
    //  UI de la cámara
    // ══════════════════════════════════════════════════════════════════
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // ── Vista previa de la cámara ──
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // ── Barra superior: Cerrar y Flash ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = alCerrar,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.4f)
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Cerrar cámara",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            IconButton(
                onClick = { flashActivo = !flashActivo },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.4f)
                )
            ) {
                Icon(
                    imageVector = if (flashActivo) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                    contentDescription = if (flashActivo) "Desactivar flash" else "Activar flash",
                    tint = if (flashActivo) Color.Yellow else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // ── Controles inferiores ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Etiqueta "Foto"
            Text(
                text = "Foto",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Botón galería ──
                IconButton(
                    onClick = alSeleccionarGaleria,
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoLibrary,
                        contentDescription = "Abrir galería",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // ── Botón capturar (círculo grande) ──
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .clickable(
                            enabled = !capturando,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            capturando = true
                            tomarFoto(context, imageCapture) { uri ->
                                capturando = false
                                if (uri != null) alCapturarFoto(uri)
                            }
                        }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(if (capturando) Color.Gray else Color.White)
                    )
                }

                // ── Botón cambiar cámara (frontal / trasera) ──
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
                            CameraSelector.LENS_FACING_FRONT
                        else
                            CameraSelector.LENS_FACING_BACK
                    },
                    modifier = Modifier.size(56.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Cameraswitch,
                        contentDescription = "Cambiar cámara",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────
//  Funciones auxiliares
// ─────────────────────────────────────────────────────────────────────

/**
 * Captura una foto y devuelve el Uri del archivo guardado en caché.
 */
private fun tomarFoto(
    context: Context,
    imageCapture: ImageCapture,
    onResult: (Uri?) -> Unit
) {
    val archivoFoto = File(
        context.cacheDir,
        "huellitas_${System.currentTimeMillis()}.jpg"
    )
    val opcionesSalida = ImageCapture.OutputFileOptions.Builder(archivoFoto).build()

    imageCapture.takePicture(
        opcionesSalida,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onResult(Uri.fromFile(archivoFoto))
            }
            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                onResult(null)
            }
        }
    )
}

/**
 * Obtiene el ProcessCameraProvider de forma suspendida (no-bloqueante).
 */
private suspend fun Context.obtenerCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuacion ->
        ProcessCameraProvider.getInstance(this).also { future ->
            future.addListener(
                { continuacion.resume(future.get()) },
                ContextCompat.getMainExecutor(this)
            )
        }
    }
