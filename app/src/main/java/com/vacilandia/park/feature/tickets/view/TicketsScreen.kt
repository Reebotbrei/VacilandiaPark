package com.vacilandia.park.feature.tickets.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.ui.components.ParkAsyncImage
import com.vacilandia.park.core.ui.components.ParkCard
import com.vacilandia.park.core.ui.components.ParkOutlineButton
import com.vacilandia.park.core.ui.components.ParkPrimaryButton
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.components.PerforationLine
import com.vacilandia.park.core.ui.components.RollingCounter
import com.vacilandia.park.core.ui.components.RollingText
import com.vacilandia.park.core.ui.components.TicketStubShape
import com.vacilandia.park.core.ui.components.oceanShadow
import com.vacilandia.park.core.ui.components.pressScale
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.BorderStrong
import com.vacilandia.park.core.ui.theme.CoralInk
import com.vacilandia.park.core.ui.theme.CoralSoft
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanBlueSoft
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.OceanOnDeepMuted
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.StatusDangerText
import com.vacilandia.park.core.ui.theme.StatusOpenBg
import com.vacilandia.park.core.ui.theme.StatusOpenText
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.SurfaceSubtle
import com.vacilandia.park.core.ui.theme.Turquoise
import com.vacilandia.park.feature.tickets.model.TicketAvailability
import com.vacilandia.park.feature.tickets.model.TicketGroup
import com.vacilandia.park.feature.tickets.model.TicketItem
import com.vacilandia.park.feature.tickets.model.TicketsUiState
import com.vacilandia.park.feature.tickets.model.VisitDateOption
import java.util.Locale

private val CounterRowHeight = 68.dp

@Composable
fun TicketsScreen(
    state: TicketsUiState,
    selectedDate: VisitDateOption?,
    isTicketAvailable: (TicketItem) -> Boolean,
    totalItemCount: Int,
    totalGuestCount: Int,
    subtotal: Double,
    discountAmount: Double,
    finalTotal: Double,
    onSelectDate: (Int) -> Unit,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    onPromoInputChange: (String) -> Unit,
    onApplyPromoCode: () -> Unit,
    onRemovePromoCode: () -> Unit,
    onContinueToSummary: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "title") {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Entradas y Paquetes",
                        style = MaterialTheme.typography.displaySmall,
                        color = InkPrimary
                    )
                    Text(
                        text = "Tarifas web · Full Day 9:00 AM – 6:00 PM",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkSecondary
                    )
                }
            }

            // 1. SELECTOR DE FECHAS (cuadrícula 3 × 2, sin deslizadores)
            item(key = "dates") {
                ParkCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(ParkRadius.lg)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Elige tu fecha de visita",
                                style = MaterialTheme.typography.titleLarge,
                                color = InkPrimary
                            )
                            selectedDate?.let { d ->
                                ParkTag(
                                    text = if (d.isWeekend) "Tarifa Sáb/Dom" else "Tarifa Lun–Vie",
                                    textColor = if (d.isWeekend) CoralInk else OceanBlue,
                                    backgroundColor = if (d.isWeekend) CoralSoft else OceanBlueSoft
                                )
                            }
                        }

                        state.availableDates.chunked(3).forEach { rowDates ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowDates.forEach { d ->
                                    DateTile(
                                        date = d,
                                        selected = d.index == state.selectedDateIndex,
                                        onClick = { onSelectDate(d.index) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. TARIFAS AGRUPADAS
            TicketGroup.entries.forEach { group ->
                val groupTickets = state.tickets.filter { it.group == group }
                if (groupTickets.isNotEmpty()) {
                    item(key = "header_${group.name}") {
                        Text(
                            text = group.label,
                            style = MaterialTheme.typography.headlineSmall,
                            color = InkPrimary,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                    items(groupTickets, key = { it.id }) { ticket ->
                        val available = isTicketAvailable(ticket)
                        val qty = if (available) state.quantities[ticket.id] ?: 0 else 0
                        TicketOptionCard(
                            ticket = ticket,
                            quantity = qty,
                            available = available,
                            onIncrement = { onIncrement(ticket.id) },
                            onDecrement = { onDecrement(ticket.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }

            // 3. CÓDIGO PROMOCIONAL SIMULADO
            item(key = "promo") {
                PromoCodeCard(
                    state = state,
                    onPromoInputChange = onPromoInputChange,
                    onApplyPromoCode = onApplyPromoCode,
                    onRemovePromoCode = onRemovePromoCode,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        // BARRA INFERIOR FIJA CON TOTAL EN TIEMPO REAL
        TotalBar(
            totalItemCount = totalItemCount,
            totalGuestCount = totalGuestCount,
            subtotal = subtotal,
            discountAmount = discountAmount,
            finalTotal = finalTotal,
            onContinue = onContinueToSummary
        )
    }
}

@Composable
private fun DateTile(
    date: VisitDateOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interaction = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(ParkRadius.sm)
    val bg by animateColorAsState(if (selected) OceanDeep else SurfaceCanvas, tween(220), label = "dateBg")
    val border by animateColorAsState(if (selected) OceanDeep else BorderDefault, tween(220), label = "dateBorder")
    val lift by animateFloatAsState(
        targetValue = if (selected) 1.04f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "dateLift"
    )
    val primaryTxt = if (selected) Color.White else InkPrimary
    val secondaryTxt = if (selected) OceanOnDeepMuted else InkSecondary

    Column(
        modifier = modifier
            .graphicsLayer {
                scaleX = lift
                scaleY = lift
            }
            .pressScale(interaction, pressedScale = 0.94f)
            .clip(shape)
            .background(bg)
            .border(1.5.dp, border, shape)
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                role = Role.RadioButton,
                onClick = onClick
            )
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Text(
            text = date.shortDay,
            style = MaterialTheme.typography.labelSmall,
            color = secondaryTxt,
            maxLines = 1
        )
        Text(
            text = date.dayNumber,
            style = MaterialTheme.typography.headlineMedium,
            color = primaryTxt,
            maxLines = 1
        )
        Text(
            text = date.monthLabel,
            style = MaterialTheme.typography.labelMedium,
            color = secondaryTxt,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            selected -> SunYellow
                            date.isWeekend -> CoralInk
                            else -> OceanBlue
                        }
                    )
            )
            Text(
                text = if (date.isWeekend) "Sáb/Dom" else "Lun–Vie",
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) SunYellow else if (date.isWeekend) CoralInk else OceanBlue,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TicketOptionCard(
    ticket: TicketItem,
    quantity: Int,
    available: Boolean,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = TicketStubShape(notchOffset = CounterRowHeight)
    val active = available && quantity > 0
    val borderColor by animateColorAsState(
        targetValue = if (active) OceanBlue else Color.Transparent,
        animationSpec = tween(220),
        label = "ticketBorder"
    )
    val weekend = ticket.availability == TicketAvailability.WEEKENDS_ONLY

    Column(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { alpha = if (available) 1f else 0.62f }
            .oceanShadow(if (active) 8.dp else 4.dp, shape)
            .clip(shape)
            .background(if (available) SurfaceCard else SurfaceSubtle)
            .border(2.dp, borderColor, shape)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                ParkAsyncImage(
                    imageUrl = ticket.imageUrl,
                    fallbackRes = ticket.fallbackRes,
                    contentDescription = ticket.title,
                    cornerRadius = ParkRadius.sm,
                    modifier = Modifier.size(76.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ParkTag(
                        text = ticket.badge,
                        textColor = if (weekend) CoralInk else OceanBlue,
                        backgroundColor = if (weekend) CoralSoft else OceanBlueSoft
                    )
                    Text(
                        text = ticket.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = InkPrimary
                    )
                    Text(
                        text = "${ticket.subtitle} (${ticket.unitLabel})",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                ticket.includes.forEach { item ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = Turquoise,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodySmall,
                            color = InkPrimary
                        )
                    }
                }
            }

            Text(
                text = if (available) ticket.conditions else "No disponible para el día elegido",
                style = MaterialTheme.typography.bodySmall,
                color = InkSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        PerforationLine(
            color = BorderStrong,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        )

        // Talón: precio y contador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(CounterRowHeight - 2.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatSoles(ticket.price),
                style = MaterialTheme.typography.headlineMedium,
                color = OceanDeep
            )

            if (available) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CounterButton(
                        icon = Icons.Outlined.Remove,
                        contentDescription = "Quitar una ${ticket.title}",
                        enabled = quantity > 0,
                        isPrimary = false,
                        onClick = onDecrement
                    )
                    RollingCounter(
                        value = quantity,
                        style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
                        color = InkPrimary,
                        modifier = Modifier.width(30.dp)
                    )
                    CounterButton(
                        icon = Icons.Outlined.Add,
                        contentDescription = "Agregar una ${ticket.title}",
                        enabled = quantity < 15,
                        isPrimary = true,
                        onClick = onIncrement
                    )
                }
            } else {
                ParkTag(
                    text = "No aplica hoy",
                    textColor = InkSecondary,
                    backgroundColor = SurfaceCard,
                    borderColor = BorderStrong
                )
            }
        }
    }
}

@Composable
private fun CounterButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean,
    isPrimary: Boolean,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val bg by animateColorAsState(
        targetValue = when {
            !enabled -> SurfaceSubtle
            isPrimary -> SunYellow
            else -> SurfaceCard
        },
        animationSpec = tween(160),
        label = "counterBg"
    )
    val fg = when {
        !enabled -> InkSecondary.copy(alpha = 0.6f)
        isPrimary -> OceanDeep
        else -> InkPrimary
    }

    Box(
        modifier = Modifier
            .pressScale(interaction, enabled = enabled, pressedScale = 0.85f)
            .size(44.dp)
            .clip(CircleShape)
            .background(bg)
            .let { if (!isPrimary && enabled) it.border(1.5.dp, BorderStrong, CircleShape) else it }
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = fg, modifier = Modifier.size(22.dp))
    }
}

@Composable
private fun PromoCodeCard(
    state: TicketsUiState,
    onPromoInputChange: (String) -> Unit,
    onApplyPromoCode: () -> Unit,
    onRemovePromoCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    ParkCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ParkRadius.lg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocalOffer,
                    contentDescription = null,
                    tint = CoralInk,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Código promocional (Simulación)",
                    style = MaterialTheme.typography.titleMedium,
                    color = InkPrimary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.promoInput,
                    onValueChange = onPromoInputChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = CircleShape,
                    isError = state.promoError != null,
                    placeholder = {
                        Text(
                            text = "Ej. VERANO20",
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkSecondary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCanvas,
                        unfocusedContainerColor = SurfaceCanvas,
                        errorContainerColor = SurfaceCanvas,
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = BorderDefault,
                        focusedTextColor = InkPrimary,
                        unfocusedTextColor = InkPrimary,
                        cursorColor = OceanBlue
                    )
                )
                ParkOutlineButton(
                    text = "Aplicar",
                    onClick = onApplyPromoCode
                )
            }

            AnimatedVisibility(
                visible = state.promoError != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = state.promoError.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = StatusDangerText
                )
            }

            AnimatedVisibility(
                visible = state.appliedPromo != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                state.appliedPromo?.let { promo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(CircleShape)
                            .background(StatusOpenBg)
                            .padding(start = 16.dp, end = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = StatusOpenText,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${promo.code}: ${promo.label}",
                                style = MaterialTheme.typography.labelMedium,
                                color = StatusOpenText
                            )
                        }
                        IconButton(onClick = onRemovePromoCode) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Quitar código",
                                tint = StatusOpenText,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "Códigos de prueba disponibles: VERANO20 (-20%) o VACI10 (-10%).",
                style = MaterialTheme.typography.bodySmall,
                color = InkSecondary
            )
        }
    }
}

@Composable
private fun TotalBar(
    totalItemCount: Int,
    totalGuestCount: Int,
    subtotal: Double,
    discountAmount: Double,
    finalTotal: Double,
    onContinue: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = ParkRadius.lg, topEnd = ParkRadius.lg)
    Row(
        modifier = Modifier
            .oceanShadow(14.dp, shape)
            .clip(shape)
            .background(SurfaceCard)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (totalItemCount == 0) {
                    "Selecciona tus entradas"
                } else {
                    "$totalItemCount ítems · $totalGuestCount personas"
                },
                style = MaterialTheme.typography.bodySmall,
                color = InkSecondary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                RollingText(
                    text = formatSoles(finalTotal),
                    style = MaterialTheme.typography.headlineLarge,
                    color = OceanDeep
                )
                AnimatedVisibility(visible = discountAmount > 0.0, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = formatSoles(subtotal),
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = InkSecondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        ParkPrimaryButton(
            text = "Continuar",
            enabled = totalItemCount > 0,
            trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
            elevation = 0.dp,
            onClick = onContinue
        )
    }
}

internal fun formatSoles(value: Double): String {
    return String.format(Locale.US, "S/ %.2f", value)
}
