package com.vacilandia.park.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vacilandia.park.core.ui.theme.SurfaceSubtle

/**
 * Imagen remota con la foto local como placeholder: la pantalla nunca muestra
 * un recuadro gris vacío, aun sin red.
 */
@Composable
fun ParkAsyncImage(
    imageUrl: String,
    @DrawableRes fallbackRes: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderColor: Color? = null
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .clip(shape)
        .background(SurfaceSubtle)
        .let { m ->
            if (borderColor != null) m.border(1.dp, borderColor, shape) else m
        }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        placeholder = painterResource(id = fallbackRes),
        error = painterResource(id = fallbackRes),
        fallback = painterResource(id = fallbackRes),
        contentScale = ContentScale.Crop,
        modifier = baseModifier
    )
}
