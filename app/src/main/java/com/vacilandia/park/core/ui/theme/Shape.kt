package com.vacilandia.park.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Geometría fluida: el agua no tiene esquinas
object ParkRadius {
    val xs = 10.dp
    val sm = 14.dp
    val md = 20.dp
    val lg = 28.dp
    val xl = 36.dp
}

val VacilandiaShapes = Shapes(
    extraSmall = RoundedCornerShape(ParkRadius.xs),
    small = RoundedCornerShape(ParkRadius.sm),
    medium = RoundedCornerShape(ParkRadius.md),
    large = RoundedCornerShape(ParkRadius.lg),
    extraLarge = RoundedCornerShape(ParkRadius.xl)
)
