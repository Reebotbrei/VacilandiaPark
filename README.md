# 🌊 Vacilandia Park - Android Native App

Aplicación móvil nativa moderna desarrollada en **Android Studio** con **Kotlin** y **Jetpack Compose (Material 3)** para el parque acuático **Vacilandia Park** (Lima, Perú).

Rediseño completo enfocado en una experiencia de usuario (UI/UX) limpia, fluida, sobria y de alto rendimiento para dispositivos móviles.

---

## 📱 Capturas y Características Principales

### 1. 🏠 Dashboard Principal (Inicio)
- **Cabecera operativa en tiempo real:** Estado dinámico del parque (*"Abierto ahora · Cierra 6:00 PM"*), clima/temperatura del agua simulada y aforo actual con barra de capacidad.
- **Accesos Rápidos (Grid 2x2):** Botones directos hacia Compra de Entradas, Zonas del Parque, Horarios/Reglas y Promociones.
- **Filtro dinámico en vivo:** Chips rectangulares para alternar entre categorías (*Todos*, *Adrenalina*, *Familiar*, *Infantil*) al instante.
- **Banner Promocional Interactivo:** Acceso directo a promociones con Bottom Sheet detallado de términos y aplicación de descuento.

### 2. 🎢 Catálogo de Atracciones y Detalle
- **Buscador en tiempo real:** Filtrado dinámico por nombre y categoría.
- **Gestión de Favoritos:** Guardado interactivo en memoria con respuesta visual.
- **Pantalla de Detalle de Atracción:** Cabecera fotográfica amplia con galería de miniaturas táctiles intercambiables, requisitos de estatura mínima, profundidad y tiempos de espera estimados.

### 3. 🎟️ Venta de Entradas Interactiva y Resumen
- **Selector de Fechas:** Chips de días próximos.
- **Contadores en tiempo real:** Selector de cantidades (`-` / `+`) para Adultos, Niños y Paquete Familiar con actualización inmediata del total.
- **Cupones Promocionales:** Validación de código promocional (ej. `VERANO20`).
- **Resumen de Reserva:** Vista previa con generación de código QR simulado para acceso al parque.

### 4. ℹ️ Información del Parque y Preguntas Frecuentes
- **Acordeones interactivos (FAQ):** Preguntas frecuentes desplegables.
- **Checklist interactivo:** Guía de *"¿Qué llevar al parque?"* con casillas táctiles.
- **Información de Zonas y Ubicación:** Zonas del parque, servicios y accesos.

---

## 🎨 Lineamientos de Diseño UI/UX

- **Geometría y Estilo:** Bordes rectos y limpios (`4dp` a `6dp`), sin sombras difusas (`elevation = 0dp`), separación por bordes sólidos finos (`1dp`) y contraste de superficies.
- **Paleta de Colores:**
  - Azul Oceánico: `#005A8D`
  - Turquesa Fresco: `#008B9B`
  - Naranja Coral (CTA): `#E85D2A`
  - Superficies: `#FFFFFF` / Fondo: `#F4F7F9`
- **Tipografía:** Familia tipográfica única, firme y legible con jerarquía por tamaño y grosor.
- **Imágenes:** Carga asíncrona optimizada mediante **Coil** con drawables vectoriales locales de respaldo.

---

## 🛠️ Tecnologías y Dependencias

- **Lenguaje:** Kotlin
- **UI Toolkit:** Jetpack Compose (BOM 2024.09.00 / Compose Compiler 2.0.20)
- **Design System:** Material 3 & Material Icons Extended
- **Carga de Imágenes:** [Coil Compose](https://coil-kt.github.io/coil/) (`io.coil-kt:coil-compose:2.7.0`)
- **Arquitectura:** Modular por características (`feature.home`, `feature.attractions`, `feature.tickets`, `feature.info`)
- **Build System:** Gradle (Kotlin DSL `.kts`)
- **Compatibility:** Min SDK 24 / Target SDK 34 / Java 17

---

## 🚀 Cómo Ejecutar el Proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Reebotbrei/VacilandiaPark.git
   ```
2. Abrir la carpeta `VacilandiaPark` en **Android Studio** (versión Ladybug / Hedgehog o superior).
3. Esperar la sincronización de Gradle (`Sync Project with Gradle Files`).
4. Conectar un dispositivo físico Android o iniciar un emulador con Android 7.0+ (API 24+).
5. Compilar y ejecutar:
   - Desde Android Studio: Presionar el botón **Run** (`Shift + F10`).
   - O por terminal:
     ```bash
     .\gradlew.bat assembleDebug
     ```
     El APK resultante se generará en `app/build/outputs/apk/debug/app-debug.apk`.
