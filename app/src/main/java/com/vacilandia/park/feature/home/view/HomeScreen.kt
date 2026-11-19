package com.vacilandia.park.feature.home.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vacilandia.park.R
import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.core.ui.components.EaseOutExpo
import com.vacilandia.park.core.ui.components.FavoriteToggle
import com.vacilandia.park.core.ui.components.ParkAsyncImage
import com.vacilandia.park.core.ui.components.ParkCard
import com.vacilandia.park.core.ui.components.ParkFilterChip
import com.vacilandia.park.core.ui.components.ParkOutlineButton
import com.vacilandia.park.core.ui.components.ParkPrimaryButton
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.components.PerforationLine
import com.vacilandia.park.core.ui.components.SectionHeader
import com.vacilandia.park.core.ui.components.StatusBarIcons
import com.vacilandia.park.core.ui.components.TicketStubShape
import com.vacilandia.park.core.ui.components.WaveBand
import com.vacilandia.park.core.ui.components.rememberReducedMotion
import com.vacilandia.park.core.ui.theme.Aqua
import com.vacilandia.park.core.ui.theme.CoralOrange
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.OceanOnDeepMuted
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.SunSoft
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.SurfaceSubtle
import com.vacilandia.park.core.ui.theme.Turquoise
import com.vacilandia.park.feature.attractions.model.Attraction
import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.home.model.HomeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeState: HomeUiState,
    allAttractions: List<Attraction>,
    favoriteIds: Set<String>,
    onQuickFilterSelected: (AttractionCategory) -> Unit,
    onOpenPromoSheet: () -> Unit,
    onClosePromoSheet: () -> Unit,
    onApplyPromoOffer: (String) -> Unit,
    onGoToTickets: () -> Unit,
    onGoToAttractions: () -> Unit,
    onGoToMap: () -> Unit,
    onGoToRules: () -> Unit,
    onOpenAttractionDetail: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val quickCategories = listOf(
        AttractionCategory.ALL,
        AttractionCategory.ADRENALINA,
        AttractionCategory.FAMILIAR,
        AttractionCategory.INFANTIL
    )

    val filteredAttractions = allAttractions.filter { item ->
        homeState.selectedFilter == AttractionCategory.ALL ||
            item.category == homeState.selectedFilter ||
            item.tags.any { it.equals(homeState.selectedFilter.label, ignoreCase = true) }
    }

    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val heroThresholdPx = with(density) { 400.dp.roundToPx() }
    val overHero by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                listState.firstVisibleItemScrollOffset < heroThresholdPx
        }
    }
    StatusBarIcons(lightIcons = overHero)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 28.dp)
        ) {
            item(key = "hero") {
                HomeHero(
                    homeState = homeState,
                    scrollOffset = {
                        if (listState.firstVisibleItemIndex == 0) listState.firstVisibleItemScrollOffset else 0
                    },
                    onGoToTickets = onGoToTickets
                )
            }

            item(key = "pulse") {
                ParkPulseCard(
                    homeState = homeState,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp)
                )
            }

            item(key = "quick") {
                QuickAccessBlock(
                    onGoToTickets = onGoToTickets,
                    onGoToMap = onGoToMap,
                    onGoToRules = onGoToRules,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 32.dp)
                )
            }

            homeState.promoOffer?.let { promo ->
                item(key = "promo") {
                    PromoTicketCard(
                        badge = promo.badge,
                        title = promo.title,
                        summary = promo.summary,
                        priceLabel = promo.priceLabel,
                        imageUrl = promo.imageUrl,
                        fallbackRes = promo.fallbackRes,
                        onClick = onOpenPromoSheet,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 28.dp)
                    )
                }
            }

            item(key = "explore_header") {
                Column(
                    modifier = Modifier.padding(top = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SectionHeader(
                        title = "Explora en vivo",
                        subtitle = "Filtra sin salir del inicio",
                        actionLabel = "Ver todas",
                        onAction = onGoToAttractions,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(quickCategories, key = { it.name }) { cat ->
                            ParkFilterChip(
                                label = cat.label,
                                selected = homeState.selectedFilter == cat,
                                onClick = { onQuickFilterSelected(cat) }
                            )
                        }
                    }
                }
            }

            item(key = "carousel") {
                AttractionCarousel(
                    filter = homeState.selectedFilter,
                    attractions = filteredAttractions,
                    favoriteIds = favoriteIds,
                    onOpen = onOpenAttractionDetail,
                    onToggleFavorite = onToggleFavorite,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

        // Velo de la barra de estado cuando el contenido pasa por debajo
        val scrimAlpha by animateFloatAsState(
            targetValue = if (overHero) 0f else 1f,
            animationSpec = tween(200),
            label = "statusScrim"
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .graphicsLayer { alpha = scrimAlpha }
                .background(SurfaceCanvas)
        )
    }

    // BOTTOM SHEET DE OFERTA DEL DÍA
    if (homeState.showPromoSheet && homeState.promoOffer != null) {
        val promo = homeState.promoOffer
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        ModalBottomSheet(
            onDismissRequest = onClosePromoSheet,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = ParkRadius.xl, topEnd = ParkRadius.xl),
            containerColor = SurfaceCard,
            tonalElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    ParkAsyncImage(
                        imageUrl = promo.imageUrl,
                        fallbackRes = promo.fallbackRes,
                        contentDescription = promo.title,
                        cornerRadius = ParkRadius.md,
                        modifier = Modifier.fillMaxSize()
                    )
                    ParkTag(
                        text = promo.badge,
                        textColor = OceanDeep,
                        backgroundColor = SunYellow,
                        icon = Icons.Outlined.LocalOffer,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = promo.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = InkPrimary
                    )
                    Text(
                        text = promo.priceLabel,
                        style = MaterialTheme.typography.displaySmall,
                        color = OceanBlue
                    )
                }

                Text(
                    text = promo.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkPrimary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(ParkRadius.sm))
                        .background(SurfaceSubtle)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Términos de la promoción",
                        style = MaterialTheme.typography.titleSmall,
                        color = InkPrimary
                    )
                    Text(
                        text = promo.terms,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkSecondary
                    )
                    Text(
                        text = promo.promoCodeHint,
                        style = MaterialTheme.typography.labelMedium,
                        color = Turquoise
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ParkOutlineButton(
                        text = "Cerrar",
                        onClick = onClosePromoSheet,
                        modifier = Modifier.weight(1f)
                    )
                    ParkPrimaryButton(
                        text = "Aplicar esta tarifa",
                        onClick = { onApplyPromoOffer(promo.ticketId) },
                        elevation = 0.dp,
                        modifier = Modifier.weight(1.5f)
                    )
                }
            }
        }
    }
}

/**
 * Cabecera inmersiva: foto del parque a sangre, agua profunda encima,
 * título de cartel que entra una sola vez y ola animada como borde.
 */
@Composable
private fun HomeHero(
    homeState: HomeUiState,
    scrollOffset: () -> Int,
    onGoToTickets: () -> Unit
) {
    val reduced = rememberReducedMotion()
    val entrance = remember { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!reduced) entrance.animateTo(1f, tween(850, easing = EaseOutExpo))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
            .clipToBounds()
            .background(OceanDeep)
    ) {
        // Foto con parallax suave y leve zoom de llegada
        ParkAsyncImage(
            imageUrl = MockParkRepository.HERO_IMAGE_URL,
            fallbackRes = R.drawable.fallback_hero,
            contentDescription = "Vista panorámica de Vacilandia Park",
            cornerRadius = 0.dp,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = scrollOffset() * 0.45f
                    val zoom = 1.08f - (entrance.value * 0.08f)
                    scaleX = zoom
                    scaleY = zoom
                }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to OceanDeep.copy(alpha = 0.72f),
                        0.25f to OceanDeep.copy(alpha = 0.15f),
                        0.5f to OceanDeep.copy(alpha = 0.40f),
                        1f to OceanDeep.copy(alpha = 0.97f)
                    )
                )
        )

        // Fila superior: saludo y ubicación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = homeState.greeting,
                style = MaterialTheme.typography.labelMedium,
                color = Color.White,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            ParkTag(
                text = "Ate · Lima",
                textColor = Color.White,
                backgroundColor = OceanDeep.copy(alpha = 0.55f),
                borderColor = Color.White.copy(alpha = 0.35f),
                icon = Icons.Outlined.LocationOn
            )
        }

        // Bloque principal: entra una vez, desde el agua hacia arriba
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 48.dp)
                .graphicsLayer {
                    alpha = entrance.value
                    translationY = (1f - entrance.value) * 56.dp.toPx()
                },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ParkStatusPill(text = homeState.parkStatus)
            Text(
                text = "Vacilandia\nPark",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White
            )
            Text(
                text = "Piscinas, toboganes extremos, EcoGranja y gastronomía en un solo lugar",
                style = MaterialTheme.typography.bodyLarge,
                color = OceanOnDeepMuted
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HeroMetricPill(icon = Icons.Outlined.Thermostat, label = homeState.weatherText, tint = SunYellow)
                HeroMetricPill(icon = Icons.Outlined.WaterDrop, label = homeState.waterTempText, tint = Aqua)
            }
            Spacer(modifier = Modifier.height(2.dp))
            ParkPrimaryButton(
                text = "Comprar Entradas",
                onClick = onGoToTickets,
                leadingIcon = Icons.Outlined.ConfirmationNumber,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                modifier = Modifier.fillMaxWidth()
            )
        }

        WaveBand(
            frontColor = SurfaceCanvas,
            backColor = Aqua.copy(alpha = 0.55f),
            height = 34.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .offset(y = 1.dp)
        )
    }
}

@Composable
private fun ParkStatusPill(text: String) {
    val isOpen = text.startsWith("Abierto")
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(OceanDeep.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isOpen) Color(0xFF5BE38F) else SunYellow)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Composable
private fun HeroMetricPill(icon: ImageVector, label: String, tint: Color) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(OceanDeep.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.White, maxLines = 1)
    }
}

/** Aforo como nivel de agua: se llena al aparecer. */
@Composable
private fun ParkPulseCard(homeState: HomeUiState, modifier: Modifier = Modifier) {
    val reduced = rememberReducedMotion()
    val fill = remember { Animatable(if (reduced) homeState.occupancyProgress else 0f) }
    LaunchedEffect(homeState.occupancyProgress) {
        if (!reduced) {
            fill.animateTo(homeState.occupancyProgress, tween(1100, delayMillis = 350, easing = EaseOutExpo))
        }
    }

    ParkCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = homeState.occupancyLabel,
                style = MaterialTheme.typography.titleMedium,
                color = InkPrimary
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(CircleShape)
                    .background(SurfaceSubtle)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fill.value.coerceIn(0.001f, 1f))
                        .clip(CircleShape)
                        .background(Brush.horizontalGradient(listOf(Aqua, Turquoise)))
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = InkSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = homeState.openHoursDetail,
                    style = MaterialTheme.typography.bodySmall,
                    color = InkSecondary
                )
            }
        }
    }
}

/** Accesos rápidos asimétricos: entradas manda, mapa y horarios acompañan. */
@Composable
private fun QuickAccessBlock(
    onGoToTickets: () -> Unit,
    onGoToMap: () -> Unit,
    onGoToRules: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Accesos rápidos",
            style = MaterialTheme.typography.headlineSmall,
            color = InkPrimary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 236.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ParkCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                backgroundColor = SunYellow,
                shape = RoundedCornerShape(ParkRadius.lg),
                elevation = 6.dp,
                onClick = onGoToTickets
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickIconBubble(
                        icon = Icons.Outlined.ConfirmationNumber,
                        tint = OceanDeep,
                        bg = Color.White.copy(alpha = 0.55f)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "Tarifas desde", style = MaterialTheme.typography.labelMedium, color = OceanDeep)
                        Text(text = "S/ 29.90", style = MaterialTheme.typography.displaySmall, color = OceanDeep)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Comprar Entradas", style = MaterialTheme.typography.titleMedium, color = OceanDeep)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickTile(
                    title = "Ver Mapa / Zonas",
                    subtitle = "8 áreas ubicadas",
                    icon = Icons.Outlined.Map,
                    bg = OceanBlue,
                    onClick = onGoToMap,
                    modifier = Modifier.weight(1f)
                )
                QuickTile(
                    title = "Horarios y Reglas",
                    subtitle = "365 días · 9am – 6pm",
                    icon = Icons.Outlined.Schedule,
                    bg = Turquoise,
                    onClick = onGoToRules,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ParkCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = bg,
        shape = RoundedCornerShape(ParkRadius.lg),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            QuickIconBubble(icon = icon, tint = Color.White, bg = Color.White.copy(alpha = 0.18f))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun QuickIconBubble(icon: ImageVector, tint: Color, bg: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

/** Oferta del día con forma de entrada física: muescas y línea de corte. */
@Composable
private fun PromoTicketCard(
    badge: String,
    title: String,
    summary: String,
    priceLabel: String,
    imageUrl: String,
    fallbackRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val footerHeight = 52.dp
    ParkCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = OceanDeep,
        shape = TicketStubShape(notchOffset = footerHeight),
        elevation = 8.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ParkAsyncImage(
                imageUrl = imageUrl,
                fallbackRes = fallbackRes,
                contentDescription = title,
                cornerRadius = ParkRadius.sm,
                modifier = Modifier.size(92.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ParkTag(
                    text = badge,
                    textColor = OceanDeep,
                    backgroundColor = SunYellow,
                    icon = Icons.Outlined.LocalOffer
                )
                Text(
                    text = priceLabel,
                    style = MaterialTheme.typography.headlineLarge,
                    color = SunYellow
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodySmall,
                    color = OceanOnDeepMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        PerforationLine(
            color = Color.White.copy(alpha = 0.28f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(footerHeight - 2.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ver términos y aplicar tarifa",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = SunYellow,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/** Carrusel con imán: cada tarjeta encaja al soltar. */
@Composable
private fun AttractionCarousel(
    filter: AttractionCategory,
    attractions: List<Attraction>,
    favoriteIds: Set<String>,
    onOpen: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = filter,
        transitionSpec = { fadeIn(tween(260)) togetherWith fadeOut(tween(140)) },
        modifier = modifier,
        label = "carouselFilter"
    ) { _ ->
        if (attractions.isEmpty()) {
            Text(
                text = "No hay zonas en esta categoría por ahora.",
                style = MaterialTheme.typography.bodyMedium,
                color = InkSecondary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )
        } else {
            val rowState = rememberLazyListState()
            LazyRow(
                state = rowState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = rowState),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(attractions, key = { it.id }) { attraction ->
                    AttractionPosterCard(
                        attraction = attraction,
                        isFavorite = favoriteIds.contains(attraction.id),
                        onClick = { onOpen(attraction.id) },
                        onToggleFavorite = { onToggleFavorite(attraction.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AttractionPosterCard(
    attraction: Attraction,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    ParkCard(
        modifier = Modifier
            .width(262.dp)
            .height(330.dp),
        backgroundColor = OceanDeep,
        shape = RoundedCornerShape(ParkRadius.lg),
        elevation = 6.dp,
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ParkAsyncImage(
                imageUrl = attraction.imageUrl,
                fallbackRes = attraction.fallbackRes,
                contentDescription = attraction.name,
                cornerRadius = 0.dp,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.42f to OceanDeep.copy(alpha = 0.10f),
                            1f to OceanDeep.copy(alpha = 0.95f)
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 4.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ParkTag(
                    text = attraction.waitTime,
                    textColor = OceanDeep,
                    backgroundColor = Color.White,
                    icon = Icons.Outlined.Schedule
                )
                FavoriteToggle(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite,
                    idleTint = InkPrimary,
                    activeTint = CoralOrange,
                    containerColor = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ParkTag(
                    text = attraction.category.label,
                    textColor = OceanDeep,
                    backgroundColor = SunSoft
                )
                Text(
                    text = attraction.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = attraction.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = OceanOnDeepMuted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
