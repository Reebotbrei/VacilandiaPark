package com.vacilandia.park.core.ui.components

import android.app.Activity
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/** Respeta "Quitar animaciones" del sistema (escala de animación = 0). */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        ) == 0f
    }
}

/** Desaceleración exponencial para llegadas seguras (sin rebote por reflejo). */
val EaseOutExpo = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)

/** Número que rueda hacia arriba al subir y hacia abajo al bajar. */
@Composable
fun RollingCounter(
    value: Int,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = value,
        modifier = modifier,
        transitionSpec = {
            val up = targetState > initialState
            (slideInVertically(tween(220)) { h -> if (up) h else -h } + fadeIn(tween(220)))
                .togetherWith(slideOutVertically(tween(160)) { h -> if (up) -h else h } + fadeOut(tween(160)))
                .using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.Center,
        label = "rollingCounter"
    ) { v ->
        Text(text = v.toString(), style = style, color = color)
    }
}

/** Texto (precio) que se renueva con un desliz vertical corto. */
@Composable
fun RollingText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = text,
        modifier = modifier,
        transitionSpec = {
            (slideInVertically(tween(240)) { it / 2 } + fadeIn(tween(240)))
                .togetherWith(slideOutVertically(tween(160)) { -it / 2 } + fadeOut(tween(160)))
                .using(SizeTransform(clip = false))
        },
        label = "rollingText"
    ) { t ->
        Text(text = t, style = style, color = color, maxLines = 1)
    }
}

/** Corazón de favoritos con "pop" de resorte al tocar. Objetivo táctil de 48dp. */
@Composable
fun FavoriteToggle(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    idleTint: Color,
    activeTint: Color,
    containerColor: Color = Color.Transparent
) {
    val scope = rememberCoroutineScope()
    val pop = remember { Animatable(1f) }
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                role = Role.Switch,
                onClickLabel = if (isFavorite) "Quitar de favoritos" else "Guardar en favoritos"
            ) {
                onToggle()
                scope.launch {
                    pop.snapTo(0.6f)
                    pop.animateTo(1f, spring(dampingRatio = 0.32f, stiffness = 420f))
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Quitar de favoritos" else "Guardar en favoritos",
                tint = if (isFavorite) activeTint else idleTint,
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer {
                        scaleX = pop.value
                        scaleY = pop.value
                    }
            )
        }
    }
}

/**
 * Banda de olas: dos ondas superpuestas que avanzan lento. La de atrás es agua
 * translúcida; la de adelante se funde con la superficie siguiente.
 * Se dibuja en la fase de draw (sin recomposición por frame).
 */
@Composable
fun WaveBand(
    frontColor: Color,
    backColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 30.dp,
    animate: Boolean = true
) {
    val reduced = rememberReducedMotion()
    val transition = rememberInfiniteTransition(label = "waves")
    val phaseAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(7000, easing = LinearEasing), RepeatMode.Restart),
        label = "wavePhase"
    )
    Canvas(modifier = modifier.height(height)) {
        val phase = if (animate && !reduced) phaseAnim.value else 0.6f
        drawPath(wavePath(size, phase + 1.3f, cycles = 1.15f, amplitudeFraction = 0.30f, baseFraction = 0.38f), backColor)
        drawPath(wavePath(size, -phase, cycles = 1.45f, amplitudeFraction = 0.26f, baseFraction = 0.58f), frontColor)
    }
}

private fun wavePath(
    size: Size,
    phase: Float,
    cycles: Float,
    amplitudeFraction: Float,
    baseFraction: Float
): Path {
    val w = size.width
    val h = size.height
    val amp = h * amplitudeFraction
    val base = h * baseFraction
    val steps = 64
    return Path().apply {
        moveTo(0f, h)
        for (i in 0..steps) {
            val x = w * i / steps
            val y = base + amp * sin((2 * PI * cycles * i / steps).toFloat() + phase)
            lineTo(x, y)
        }
        lineTo(w, h)
        close()
    }
}

/**
 * Forma de entrada física de parque: esquinas redondeadas con dos muescas
 * laterales a la altura de la línea de corte.
 */
class TicketStubShape(
    private val cornerRadius: Dp = 22.dp,
    private val notchRadius: Dp = 11.dp,
    private val notchOffset: Dp,
    private val fromBottom: Boolean = true
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val c = with(density) { cornerRadius.toPx() }
        val r = with(density) { notchRadius.toPx() }
        val off = with(density) { notchOffset.toPx() }
        val cy = (if (fromBottom) size.height - off else off).coerceIn(c + r, size.height - c - r)
        val body = Path().apply {
            addRoundRect(RoundRect(0f, 0f, size.width, size.height, CornerRadius(c, c)))
        }
        val notches = Path().apply {
            addOval(Rect(center = Offset(0f, cy), radius = r))
            addOval(Rect(center = Offset(size.width, cy), radius = r))
        }
        return Outline.Generic(Path.combine(PathOperation.Difference, body, notches))
    }
}

/** Línea de corte punteada entre muescas. */
@Composable
fun PerforationLine(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.height(2.dp)) {
        val dash = 7.dp.toPx()
        val gap = 6.dp.toPx()
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = color,
                start = Offset(x, size.height / 2),
                end = Offset((x + dash).coerceAtMost(size.width), size.height / 2),
                strokeWidth = size.height
            )
            x += dash + gap
        }
    }
}

/**
 * Íconos de la barra de estado: claros sobre cabeceras de agua profunda,
 * oscuros sobre superficies claras. Restaura al salir.
 */
@Composable
fun StatusBarIcons(lightIcons: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(lightIcons) {
        val window = (view.context as? Activity)?.window
        val controller = window?.let { WindowCompat.getInsetsController(it, view) }
        val previous = controller?.isAppearanceLightStatusBars
        controller?.isAppearanceLightStatusBars = !lightIcons
        onDispose {
            if (previous != null) controller.isAppearanceLightStatusBars = previous
        }
    }
}
