# 🐾 Huellitas

Aplicación móvil Android para el registro y visualización de animales callejeros. Permite a los usuarios reportar avistamientos de animales en situación de calle, incluyendo fotos, ubicación y datos de contacto para facilitar su rescate y adopción.

## Capturas de pantalla

![Interfaz de la app](app/src/main/res/drawable/interfaz.png)

## Características

- **Splash screen animado** con Lottie
- **Onboarding** de bienvenida e introducción (solo en primer inicio)
- **Listado de animales** con filtros y ordenamiento (Recientes, Por fecha, Más antiguos)
- **Registro de animales** con nombre, tipo, raza, descripción, ubicación y contacto
- **Captura de fotos** con CameraX integrada en la app
- **Subida de imágenes** al servidor
- **Zoom de imágenes** en diálogo ampliado
- **Navegación fluida** con animaciones de transición entre pantallas

## Arquitectura

El proyecto sigue el patrón **MVVM** (Model-View-ViewModel):

```
com.example.huellitas/
├── model/              # Modelos de datos (Animal, TipoAnimal, OpcionFiltro)
├── navigation/         # Rutas y NavHost de navegación
├── network/            # Retrofit client, ApiService y DTOs
├── repository/         # Repositorio de datos (AnimalRepository)
├── ui/
│   ├── components/     # Componentes reutilizables (AnimalCard, FilterChipRow, etc.)
│   ├── screens/
│   │   ├── camera/     # Pantalla de cámara (CameraX)
│   │   ├── home/       # Lista principal de animales
│   │   ├── onboarding/ # Bienvenida e introducción
│   │   ├── registration/ # Formulario de registro de animal
│   │   └── splash/     # Pantalla de carga con Lottie
│   └── theme/          # Tema Material 3 de la app
├── viewmodel/          # ViewModels (AnimalListViewModel, AnimalRegistroViewModel)
└── MainActivity.kt     # Actividad principal (single activity)
```

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** | UI declarativa |
| **Material 3** | Diseño y componentes visuales |
| **Navigation Compose** | Navegación entre pantallas |
| **Retrofit 2** | Consumo de API REST |
| **OkHttp** | Cliente HTTP con logging |
| **Gson** | Serialización/deserialización JSON |
| **Coil** | Carga de imágenes asíncronas |
| **Lottie** | Animaciones en splash screen |
| **CameraX** | Captura de fotos |
| **Coroutines** | Programación asíncrona |
| **ViewModel** | Gestión del estado de la UI |

## Backend

La app se conecta a un backend PHP alojado en un servidor web:

- **Debug**: `http://10.0.2.2/huellitas/` (XAMPP local vía emulador)
- **Release**: `https://webculmapp.com/huellitas/` (HostGator)

### Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/animales/listar.php` | Listar animales con paginación, filtros y ordenamiento |
| POST | `/api/animales/crear.php` | Registrar un nuevo animal |
| POST | `/api/animales/subir_imagen.php` | Subir imagen de un animal |

## Requisitos

- **Android Studio** Ladybug o superior
- **JDK 11+**
- **Android SDK 24** (mínimo) — **SDK 36** (target)
- Servidor XAMPP (para desarrollo local) o acceso al backend en producción

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Alfredogc21/huellitas.git
   ```
2. Abre el proyecto en **Android Studio**.
3. Sincroniza Gradle y espera a que descargue las dependencias.
4. **(Opcional)** Para desarrollo local, levanta XAMPP con el backend PHP en `htdocs/huellitas/`.
5. Ejecuta la app en un emulador o dispositivo físico.

## Configuración de la URL base

La URL del backend se configura en [app/build.gradle.kts](app/build.gradle.kts):

- **Debug** — se conecta automáticamente al servidor local (`10.0.2.2`)
- **Release** — apunta al dominio de producción

Para cambiar la URL de producción, edita el `buildConfigField` en el bloque `defaultConfig`.

## Versión

- **v1.0.0** — Primera versión con registro, listado y captura de fotos de animales callejeros.

