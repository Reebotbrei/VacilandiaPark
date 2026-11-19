package com.vacilandia.park.feature.info.view

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.zIndex
import com.vacilandia.park.core.ui.components.oceanShadow
import com.vacilandia.park.core.ui.theme.Aqua
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.TurquoiseSoft
import com.vacilandia.park.core.ui.components.EaseOutExpo
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.core.ui.components.ParkCard
import com.vacilandia.park.core.ui.components.ParkOutlineButton
import com.vacilandia.park.core.ui.components.ParkPrimaryButton
import com.vacilandia.park.core.ui.components.SectionHeader
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.BorderStrong
import com.vacilandia.park.core.ui.theme.CoralOrange
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanBlueSoft
import com.vacilandia.park.core.ui.theme.StatusDangerBg
import com.vacilandia.park.core.ui.theme.StatusDangerBorder
import com.vacilandia.park.core.ui.theme.StatusDangerText
import com.vacilandia.park.core.ui.theme.StatusOpenBg
import com.vacilandia.park.core.ui.theme.StatusOpenBorder
import com.vacilandia.park.core.ui.theme.StatusOpenText
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.SurfaceSubtle
import com.vacilandia.park.core.ui.theme.Turquoise
import com.vacilandia.park.feature.info.model.InfoUiState
import com.vacilandia.park.feature.info.model.ParkZone

@Composable
fun InfoScreen(
    state: InfoUiState,
    selectedZone: ParkZone?,
    initialSection: String?,
    onSelectZone: (String) -> Unit,
    onToggleFaq: (String) -> Unit,
    onToggleChecklistItem: (String) -> Unit,
    onOpenAttraction: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(initialSection) {
        when (initialSection) {
            "map" -> listState.animateScrollToItem(1)
            "rules" -> listState.animateScrollToItem(4)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 0. TÍTULO
        item {
            Text(
                text = "Parque, Mapa e Información",
                style = MaterialTheme.typography.displaySmall,
                color = InkPrimary
            )
        }

        // 1. MAPA VISUAL ESTÁTICO DEL PARQUE CON ZONAS MARCADAS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(
                    title = "Mapa de zonas",
                    subtitle = "Toca cualquier marcador del plano"
                )

                StaticParkMap(
                    zones = state.zones,
                    selectedZoneId = selectedZone?.id ?: "",
                    onSelectZone = onSelectZone
                )

                // Cuadrícula táctil de zonas (2 columnas)
                state.zones.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEach { zone ->
                            val isSel = zone.id == selectedZone?.id
                            val pillBg by animateColorAsState(if (isSel) OceanDeep else SurfaceCard, tween(200), label = "zoneBg")
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 52.dp)
                                    .clip(RoundedCornerShape(ParkRadius.sm))
                                    .background(pillBg)
                                    .border(1.5.dp, if (isSel) OceanDeep else BorderDefault, RoundedCornerShape(ParkRadius.sm))
                                    .clickable(role = Role.RadioButton) { onSelectZone(zone.id) }
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (isSel) SunYellow else SurfaceSubtle),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = zone.number.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isSel) OceanDeep else InkPrimary
                                    )
                                }
                                Text(
                                    text = zone.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSel) Color.White else InkPrimary,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Ficha de la zona seleccionada
                AnimatedContent(
                    targetState = selectedZone,
                    transitionSpec = {
                        (slideInVertically(tween(260)) { it / 4 } + fadeIn(tween(220)))
                            .togetherWith(fadeOut(tween(120)))
                            .using(SizeTransform(clip = false))
                    },
                    contentKey = { it?.id },
                    label = "zoneCard"
                ) { animatedZone ->
                  animatedZone?.let { zone ->
                    ParkCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(ParkRadius.lg),
                        borderColor = OceanBlue
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ParkTag(
                                        text = "Zona ${zone.number}",
                                        textColor = OceanDeep,
                                        backgroundColor = SunYellow
                                    )
                                    ParkTag(
                                        text = zone.category,
                                        textColor = OceanBlue,
                                        backgroundColor = OceanBlueSoft
                                    )
                                }
                                Text(
                                    text = zone.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = InkPrimary
                                )
                                Text(
                                    text = zone.summary,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            ParkOutlineButton(
                                text = "Ver zona",
                                borderColor = OceanBlue,
                                contentColor = OceanBlue,
                                onClick = { onOpenAttraction(zone.attractionId) }
                            )
                        }
                    }
                  }
                }
            }
        }

        // 2. UBICACIÓN, HORARIOS Y BOTÓN GOOGLE MAPS
        item {
            ParkCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(ParkRadius.lg)) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ParkTag(
                            text = "Abierto 365 días",
                            textColor = StatusOpenText,
                            backgroundColor = StatusOpenBg,
                            borderColor = StatusOpenBorder
                        )
                        Text(
                            text = "9:00 AM – 6:00 PM",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = OceanBlue
                        )
                    }

                    Text(
                        text = "Ubicación y contacto",
                        style = MaterialTheme.typography.titleLarge,
                        color = InkPrimary
                    )

                    Text(
                        text = MockParkRepository.PARK_ADDRESS,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = InkPrimary
                    )
                    Text(
                        text = "Referencia: ${MockParkRepository.PARK_REFERENCE}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkSecondary
                    )

                    HorizontalDivider(thickness = 1.dp, color = BorderDefault)

                    Text(
                        text = "Teléfonos: ${MockParkRepository.PARK_PHONES}",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkPrimary
                    )
                    Text(
                        text = "Eventos y Cumpleaños (mín. 35 pax): ${MockParkRepository.PARK_WHATSAPP_EVENTS}",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkSecondary
                    )
                    Text(
                        text = MockParkRepository.PARK_EMAILS,
                        style = MaterialTheme.typography.bodySmall,
                        color = InkSecondary
                    )

                    ParkPrimaryButton(
                        text = "Abrir ubicación en Google Maps",
                        leadingIcon = Icons.Outlined.LocationOn,
                        elevation = 0.dp,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            val uri = Uri.parse(
                                "https://maps.google.com/?q=" +
                                    Uri.encode("Vacilandia Park, Ate, Lima, Peru")
                            )
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                // Sin app de mapas instalada en emulador mínimo
                            }
                        }
                    )
                }
            }
        }

        // 3. CHECKLIST INTERACTIVO: ¿QUÉ LLEVAR AL PARQUE?
        item {
            val doneCount = state.checkedItemIds.size
            val totalCount = state.checklist.size
            val progressTarget = if (totalCount == 0) 0f else doneCount.toFloat() / totalCount.toFloat()
            val progress by animateFloatAsState(progressTarget, tween(450, easing = EaseOutExpo), label = "checklist")

            ParkCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(ParkRadius.lg)) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Qué llevar al parque?",
                            style = MaterialTheme.typography.titleLarge,
                            color = InkPrimary
                        )
                        ParkTag(
                            text = "$doneCount / $totalCount listos",
                            textColor = if (doneCount == totalCount && totalCount > 0) OceanDeep else OceanBlue,
                            backgroundColor = if (doneCount == totalCount && totalCount > 0) SunYellow else OceanBlueSoft
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(CircleShape)
                            .background(SurfaceSubtle)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress.coerceIn(0.001f, 1f))
                                .height(12.dp)
                                .clip(CircleShape)
                                .background(Brush.horizontalGradient(listOf(Aqua, Turquoise)))
                        )
                    }

                    state.checklist.forEach { item ->
                        val checked = state.checkedItemIds.contains(item.id)
                        val rowBg by animateColorAsState(if (checked) TurquoiseSoft else SurfaceCard, tween(200), label = "checkRow")
                        val boxBg by animateColorAsState(if (checked) Turquoise else SurfaceCard, tween(200), label = "checkBox")
                        val tick by animateFloatAsState(
                            targetValue = if (checked) 1f else 0f,
                            animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f),
                            label = "tick"
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(ParkRadius.sm))
                                .background(rowBg)
                                .border(1.5.dp, if (checked) Turquoise.copy(alpha = 0.35f) else BorderDefault, RoundedCornerShape(ParkRadius.sm))
                                .toggleable(value = checked, role = Role.Checkbox) { onToggleChecklistItem(item.id) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(top = 1.dp)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(boxBg)
                                    .border(1.5.dp, if (checked) Turquoise else BorderStrong, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .graphicsLayer {
                                            scaleX = tick
                                            scaleY = tick
                                            alpha = tick
                                        }
                                )
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = InkPrimary
                                )
                                Text(
                                    text = item.note,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. NORMAS DE INGRESO (PERMITIDO VS NO PERMITIDO)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionHeader(
                    title = "Horarios y Reglas de ingreso",
                    subtitle = "Disposiciones vigentes del parque"
                )

                ParkCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(ParkRadius.lg),
                    backgroundColor = StatusOpenBg,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ParkTag(
                            text = "Sí está permitido",
                            textColor = Color.White,
                            backgroundColor = StatusOpenText,
                            icon = Icons.Outlined.Check
                        )
                        state.permittedRules.forEach { rule ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = StatusOpenText,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Text(
                                    text = rule,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = InkPrimary
                                )
                            }
                        }
                    }
                }

                ParkCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(ParkRadius.lg),
                    backgroundColor = StatusDangerBg,
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ParkTag(
                            text = "No está permitido",
                            textColor = Color.White,
                            backgroundColor = StatusDangerText,
                            icon = Icons.Outlined.Close
                        )
                        state.prohibitedRules.forEach { rule ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = null,
                                    tint = StatusDangerText,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Text(
                                    text = rule,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = InkPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. ACORDEONES FAQ INTERACTIVOS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader(
                    title = "Preguntas frecuentes",
                    subtitle = "Toca para desplegar cada respuesta"
                )

                state.faqs.forEach { faq ->
                    val expanded = state.expandedFaqIds.contains(faq.id)
                    val chevron by animateFloatAsState(if (expanded) 180f else 0f, tween(260, easing = EaseOutExpo), label = "chevron")
                    ParkCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(ParkRadius.md),
                        borderColor = if (expanded) OceanBlue else null,
                        elevation = if (expanded) 6.dp else 2.dp,
                        onClick = { onToggleFaq(faq.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = faq.question,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (expanded) OceanBlue else OceanBlueSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.KeyboardArrowDown,
                                        contentDescription = if (expanded) "Contraer" else "Expandir",
                                        tint = if (expanded) Color.White else OceanBlue,
                                        modifier = Modifier.graphicsLayer { rotationZ = chevron }
                                    )
                                }
                            }

                            AnimatedVisibility(visible = expanded) {
                                Column {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    HorizontalDivider(thickness = 1.dp, color = BorderDefault)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = faq.answer,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = InkSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StaticParkMap(
    zones: List<ParkZone>,
    selectedZoneId: String,
    onSelectZone: (String) -> Unit
) {
    val shape = RoundedCornerShape(ParkRadius.lg)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .oceanShadow(4.dp, shape)
            .clip(shape)
            .background(Color(0xFFE1F0E6))
    ) {
        val mapW = maxWidth
        val mapH = maxHeight

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Áreas verdes y cuadrícula sutil del parque
            drawRect(color = Color(0xFFE3EFE6), size = size)

            // Senderos principales
            val mainPath = Path().apply {
                moveTo(w * 0.14f, h * 0.78f)
                quadraticTo(w * 0.34f, h * 0.68f, w * 0.48f, h * 0.42f)
                quadraticTo(w * 0.64f, h * 0.28f, w * 0.82f, h * 0.40f)
            }
            drawPath(
                path = mainPath,
                color = Color(0xFFF6EFE0),
                style = Stroke(width = 24f, cap = StrokeCap.Square)
            )
            drawLine(
                color = Color(0xFFF6EFE0),
                start = Offset(w * 0.46f, h * 0.68f),
                end = Offset(w * 0.84f, h * 0.74f),
                strokeWidth = 18f
            )

            // Zona 2: Toboganes
            drawRoundRect(
                color = Color(0xFF8CCAE8),
                topLeft = Offset(w * 0.12f, h * 0.11f),
                size = Size(w * 0.24f, h * 0.25f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Zona 3: Piscina Familiar Central
            drawRoundRect(
                color = Color(0xFF008B9B),
                topLeft = Offset(w * 0.37f, h * 0.24f),
                size = Size(w * 0.23f, h * 0.27f),
                cornerRadius = CornerRadius(10f, 10f)
            )

            // Zona 4: Piscina Infantil
            drawRoundRect(
                color = Color(0xFF4CAEC8),
                topLeft = Offset(w * 0.33f, h * 0.56f),
                size = Size(w * 0.24f, h * 0.24f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Zona 5: Plaza Espuma
            drawRoundRect(
                color = Color(0xFFB9DCEB),
                topLeft = Offset(w * 0.62f, h * 0.12f),
                size = Size(w * 0.16f, h * 0.24f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Zona 6: Feria Gastronómica
            drawRoundRect(
                color = Color(0xFFF5D8BA),
                topLeft = Offset(w * 0.59f, h * 0.44f),
                size = Size(w * 0.18f, h * 0.23f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Zona 7: Canchas sintéticas
            drawRoundRect(
                color = Color(0xFF74B87E),
                topLeft = Offset(w * 0.78f, h * 0.22f),
                size = Size(w * 0.16f, h * 0.30f),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Zona 8: EcoGranja
            drawRoundRect(
                color = Color(0xFFAAD2B0),
                topLeft = Offset(w * 0.74f, h * 0.60f),
                size = Size(w * 0.20f, h * 0.28f),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Zona 1: Ingreso
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(w * 0.06f, h * 0.66f),
                size = Size(w * 0.16f, h * 0.24f),
                cornerRadius = CornerRadius(6f, 6f)
            )
        }

        // Pines rectangulares táctiles sobre el mapa
        zones.forEach { zone ->
            val isSelected = zone.id == selectedZoneId
            val markerSize = 32.dp
            val xDp = (mapW * zone.xFraction) - (markerSize / 2)
            val yDp = (mapH * zone.yFraction) - (markerSize / 2)
            val pinScale by animateFloatAsState(
                targetValue = if (isSelected) 1.3f else 1f,
                animationSpec = spring(dampingRatio = 0.4f, stiffness = 450f),
                label = "pin"
            )

            Box(
                modifier = Modifier
                    .offset(x = xDp, y = yDp)
                    .zIndex(if (isSelected) 1f else 0f)
                    .graphicsLayer {
                        scaleX = pinScale
                        scaleY = pinScale
                    }
                    .size(markerSize)
                    .oceanShadow(if (isSelected) 6.dp else 2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(if (isSelected) SunYellow else OceanDeep)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable(role = Role.RadioButton, onClickLabel = zone.name) { onSelectZone(zone.id) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = zone.number.toString(),
                    color = if (isSelected) OceanDeep else Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black)
                )
            }
        }
    }
}
