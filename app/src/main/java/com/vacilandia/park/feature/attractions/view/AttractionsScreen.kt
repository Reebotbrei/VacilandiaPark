package com.vacilandia.park.feature.attractions.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.ui.components.FavoriteToggle
import com.vacilandia.park.core.ui.components.ParkAsyncImage
import com.vacilandia.park.core.ui.components.ParkCard
import com.vacilandia.park.core.ui.components.ParkFilterChip
import com.vacilandia.park.core.ui.components.ParkOutlineButton
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.components.oceanShadow
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.CoralInk
import com.vacilandia.park.core.ui.theme.CoralOrange
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.SunSoft
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.Turquoise
import com.vacilandia.park.core.ui.theme.TurquoiseSoft
import com.vacilandia.park.feature.attractions.model.Attraction
import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.attractions.model.AttractionsUiState

@Composable
fun AttractionsScreen(
    state: AttractionsUiState,
    filteredAttractions: List<Attraction>,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (AttractionCategory) -> Unit,
    onToggleFavoritesOnly: () -> Unit,
    onClearFilters: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenDetail: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
        contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "title") {
            Text(
                text = "Atracciones y Zonas",
                style = MaterialTheme.typography.displaySmall,
                color = InkPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Buscador en tiempo real
        item(key = "search") {
            val pill = CircleShape
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .oceanShadow(3.dp, pill),
                singleLine = true,
                shape = pill,
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = {
                    Text(
                        text = "Buscar tobogán, piscina, pony, comida...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = OceanBlue
                    )
                },
                trailingIcon = {
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Limpiar búsqueda",
                                tint = InkSecondary
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = OceanBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = InkPrimary,
                    unfocusedTextColor = InkPrimary,
                    cursorColor = OceanBlue
                )
            )
        }

        // Chips de categoría + favoritos (deslizables)
        item(key = "chips") {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AttractionCategory.entries, key = { it.name }) { category ->
                    ParkFilterChip(
                        label = category.label,
                        selected = state.selectedCategory == category && !state.favoritesOnly,
                        onClick = {
                            if (state.favoritesOnly) onToggleFavoritesOnly()
                            onCategorySelected(category)
                        }
                    )
                }
                item(key = "favs") {
                    ParkFilterChip(
                        label = "Favoritos (${state.favoriteIds.size})",
                        selected = state.favoritesOnly,
                        selectedColor = CoralInk,
                        onClick = onToggleFavoritesOnly
                    )
                }
            }
        }

        item(key = "count") {
            AnimatedContent(
                targetState = "Mostrando ${filteredAttractions.size} de ${state.attractions.size} zonas",
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(120)) },
                modifier = Modifier.padding(horizontal = 16.dp),
                label = "count"
            ) { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = InkSecondary
                )
            }
        }

        if (filteredAttractions.isEmpty()) {
            item(key = "empty") {
                EmptyResults(
                    favoritesOnly = state.favoritesOnly,
                    onClearFilters = onClearFilters,
                    modifier = Modifier
                        .animateItem()
                        .padding(horizontal = 16.dp)
                )
            }
        } else {
            items(filteredAttractions, key = { it.id }) { attraction ->
                AttractionCard(
                    attraction = attraction,
                    isFavorite = state.favoriteIds.contains(attraction.id),
                    onOpen = { onOpenDetail(attraction.id) },
                    onToggleFavorite = { onToggleFavorite(attraction.id) },
                    modifier = Modifier
                        .animateItem()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun AttractionCard(
    attraction: Attraction,
    isFavorite: Boolean,
    onOpen: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    ParkCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ParkRadius.lg),
        onClick = onOpen
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(184.dp)
        ) {
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
                            0f to OceanDeep.copy(alpha = 0.35f),
                            0.35f to Color.Transparent,
                            1f to Color.Transparent
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
                    text = attraction.category.label,
                    textColor = OceanDeep,
                    backgroundColor = SunSoft
                )
                FavoriteToggle(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite,
                    idleTint = InkPrimary,
                    activeTint = CoralOrange,
                    containerColor = Color.White
                )
            }
            ParkTag(
                text = attraction.waitTime,
                textColor = Color.White,
                backgroundColor = OceanDeep.copy(alpha = 0.78f),
                icon = Icons.Outlined.Schedule,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = attraction.name,
                style = MaterialTheme.typography.headlineSmall,
                color = InkPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = attraction.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = InkSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                    FactChip(icon = Icons.Outlined.Height, text = attraction.minHeight)
                    FactChip(icon = Icons.Outlined.Schedule, text = attraction.schedule)
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OceanBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver detalle",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FactChip(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(TurquoiseSoft)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Turquoise, modifier = Modifier.size(14.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Turquoise,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyResults(
    favoritesOnly: Boolean,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    ParkCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(ParkRadius.lg)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(TurquoiseSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Pool,
                    contentDescription = null,
                    tint = Turquoise,
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(
                text = if (favoritesOnly) "Aún no guardas favoritos" else "No encontramos coincidencias",
                style = MaterialTheme.typography.headlineSmall,
                color = InkPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (favoritesOnly) {
                    "Toca el corazón de cualquier zona para tenerla a mano el día de tu visita."
                } else {
                    "Prueba otra palabra o restablece los filtros activos."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = InkSecondary,
                textAlign = TextAlign.Center
            )
            ParkOutlineButton(
                text = "Mostrar todas",
                onClick = onClearFilters,
                borderColor = BorderDefault
            )
        }
    }
}
