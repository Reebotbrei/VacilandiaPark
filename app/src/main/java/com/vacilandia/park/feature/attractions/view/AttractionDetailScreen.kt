package com.vacilandia.park.feature.attractions.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.ui.components.FavoriteToggle
import com.vacilandia.park.core.ui.components.ParkAsyncImage
import com.vacilandia.park.core.ui.components.ParkPrimaryButton
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.components.StatusBarIcons
import com.vacilandia.park.core.ui.components.oceanShadow
import com.vacilandia.park.core.ui.theme.CoralOrange
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanBlueSoft
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.StatusWarnBg
import com.vacilandia.park.core.ui.theme.StatusWarnText
import com.vacilandia.park.core.ui.theme.SunInk
import com.vacilandia.park.core.ui.theme.SunSoft
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.Turquoise
import com.vacilandia.park.core.ui.theme.TurquoiseSoft
import com.vacilandia.park.feature.attractions.model.Attraction
import kotlinx.coroutines.launch

private val HeaderHeight = 400.dp
private val SheetOverlap = 32.dp

@Composable
fun AttractionDetailScreen(
    attraction: Attraction?,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onGoToTickets: () -> Unit
) {
    if (attraction == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceCanvas)
                .statusBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Atracción no encontrada",
                style = MaterialTheme.typography.headlineSmall,
                color = InkPrimary
            )
            ParkPrimaryButton(text = "Volver", onClick = onBack)
        }
        return
    }

    val photos = attraction.gallery
    val pagerState = rememberPagerState(pageCount = { photos.size.coerceAtLeast(1) })
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val collapsePx = with(density) { (HeaderHeight - 120.dp).roundToPx() }

    val collapsed by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > collapsePx
        }
    }
    StatusBarIcons(lightIcons = !collapsed)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                // Galería deslizable a sangre, con parallax
                item(key = "gallery") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(HeaderHeight)
                            .background(OceanDeep)
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    if (listState.firstVisibleItemIndex == 0) {
                                        translationY = listState.firstVisibleItemScrollOffset * 0.5f
                                    }
                                }
                        ) { page ->
                            val photo = photos.getOrNull(page)
                            ParkAsyncImage(
                                imageUrl = photo?.imageUrl ?: attraction.imageUrl,
                                fallbackRes = photo?.fallbackRes ?: attraction.fallbackRes,
                                contentDescription = photo?.caption ?: attraction.name,
                                cornerRadius = 0.dp,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        0f to OceanDeep.copy(alpha = 0.55f),
                                        0.25f to Color.Transparent,
                                        0.6f to Color.Transparent,
                                        1f to OceanDeep.copy(alpha = 0.75f)
                                    )
                                )
                        )

                        // Miniaturas táctiles sobre la foto
                        if (photos.size > 1) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(start = 20.dp, bottom = SheetOverlap + 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                photos.forEachIndexed { index, photo ->
                                    val selected = pagerState.currentPage == index
                                    val w by animateDpAsState(if (selected) 64.dp else 48.dp, tween(220), label = "thumbW")
                                    Box(
                                        modifier = Modifier
                                            .width(w)
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(ParkRadius.xs))
                                            .border(
                                                width = if (selected) 2.5.dp else 1.dp,
                                                color = if (selected) SunYellow else Color.White.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(ParkRadius.xs)
                                            )
                                            .clickable(role = Role.Tab, onClickLabel = photo.caption) {
                                                scope.launch { pagerState.animateScrollToPage(index) }
                                            }
                                    ) {
                                        ParkAsyncImage(
                                            imageUrl = photo.imageUrl,
                                            fallbackRes = photo.fallbackRes,
                                            contentDescription = null,
                                            cornerRadius = ParkRadius.xs,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Hoja de contenido que se monta sobre la foto
                item(key = "sheet") {
                    Column(
                        modifier = Modifier
                            .offset(y = -SheetOverlap)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = ParkRadius.xl, topEnd = ParkRadius.xl))
                            .background(SurfaceCanvas)
                            .padding(start = 20.dp, end = 20.dp, top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ParkTag(
                                    text = attraction.category.label,
                                    textColor = OceanDeep,
                                    backgroundColor = SunYellow
                                )
                                ParkTag(
                                    text = attraction.schedule,
                                    textColor = Turquoise,
                                    backgroundColor = TurquoiseSoft,
                                    icon = Icons.Outlined.Schedule
                                )
                            }
                            Text(
                                text = attraction.name,
                                style = MaterialTheme.typography.displaySmall,
                                color = InkPrimary
                            )
                            photos.getOrNull(pagerState.currentPage)?.let { photo ->
                                Text(
                                    text = photo.caption,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkSecondary
                                )
                            }
                        }

                        // Ficha técnica en tres baldosas de color
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricTile(
                                icon = Icons.Outlined.Height,
                                label = "Estatura mín.",
                                value = attraction.minHeight,
                                bg = OceanBlueSoft,
                                ink = OceanBlue,
                                modifier = Modifier.weight(1f)
                            )
                            MetricTile(
                                icon = Icons.Outlined.WaterDrop,
                                label = "Profundidad",
                                value = attraction.depth,
                                bg = TurquoiseSoft,
                                ink = Turquoise,
                                modifier = Modifier.weight(1f)
                            )
                            MetricTile(
                                icon = Icons.Outlined.Schedule,
                                label = "Espera est.",
                                value = attraction.waitTime,
                                bg = SunSoft,
                                ink = SunInk,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Text(
                            text = attraction.fullDescription,
                            style = MaterialTheme.typography.bodyLarge,
                            color = InkPrimary
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Qué incluye esta zona",
                                style = MaterialTheme.typography.headlineSmall,
                                color = InkPrimary
                            )
                            attraction.highlights.forEach { highlight ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Turquoise),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                    Text(
                                        text = highlight,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = InkPrimary,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(ParkRadius.md))
                                .background(StatusWarnBg)
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.HealthAndSafety,
                                    contentDescription = null,
                                    tint = StatusWarnText,
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Recomendaciones de seguridad",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = InkPrimary
                                )
                            }
                            attraction.safetyTips.forEach { tip ->
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 7.dp)
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(StatusWarnText)
                                    )
                                    Text(
                                        text = tip,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = InkPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Barra inferior fija CTA
            Row(
                modifier = Modifier
                    .oceanShadow(12.dp, RoundedCornerShape(topStart = ParkRadius.lg, topEnd = ParkRadius.lg))
                    .clip(RoundedCornerShape(topStart = ParkRadius.lg, topEnd = ParkRadius.lg))
                    .background(SurfaceCard)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Incluido en tu entrada",
                        style = MaterialTheme.typography.titleSmall,
                        color = InkPrimary
                    )
                    Text(
                        text = "Full Day 9:00 AM – 6:00 PM",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkSecondary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                ParkPrimaryButton(
                    text = "Comprar",
                    leadingIcon = Icons.Outlined.ConfirmationNumber,
                    elevation = 0.dp,
                    onClick = onGoToTickets
                )
            }
        }

        // Barra superior flotante: botones sobre la foto, barra sólida al bajar
        val barBg by animateColorAsState(
            targetValue = if (collapsed) SurfaceCard else Color.Transparent,
            animationSpec = tween(220),
            label = "barBg"
        )
        val titleAlpha by animateFloatAsState(if (collapsed) 1f else 0f, tween(220), label = "titleAlpha")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(barBg)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                solid = !collapsed,
                onClick = onBack
            )
            Text(
                text = attraction.name,
                style = MaterialTheme.typography.titleMedium,
                color = InkPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .graphicsLayer { alpha = titleAlpha }
            )
            FavoriteToggle(
                isFavorite = isFavorite,
                onToggle = onToggleFavorite,
                idleTint = InkPrimary,
                activeTint = CoralOrange,
                containerColor = if (collapsed) Color.Transparent else Color.White
            )
        }
    }
}

@Composable
private fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String,
    solid: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (solid) Color.White else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = InkPrimary
            )
        }
    }
}

@Composable
private fun MetricTile(
    icon: ImageVector,
    label: String,
    value: String,
    bg: Color,
    ink: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(ParkRadius.md))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ink, modifier = Modifier.size(20.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = InkPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = ink,
            maxLines = 1
        )
    }
}
