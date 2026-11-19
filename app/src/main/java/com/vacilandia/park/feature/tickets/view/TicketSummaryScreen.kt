package com.vacilandia.park.feature.tickets.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.ui.components.ParkCard
import com.vacilandia.park.core.ui.components.ParkOutlineButton
import com.vacilandia.park.core.ui.components.ParkPrimaryButton
import com.vacilandia.park.core.ui.components.ParkTag
import com.vacilandia.park.core.ui.components.PerforationLine
import com.vacilandia.park.core.ui.components.QrCodeView
import com.vacilandia.park.core.ui.components.TicketStubShape
import com.vacilandia.park.core.ui.components.oceanShadow
import com.vacilandia.park.core.ui.components.rememberReducedMotion
import com.vacilandia.park.core.ui.theme.Aqua
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.BorderStrong
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.OceanOnDeepMuted
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.StatusOpenText
import com.vacilandia.park.core.ui.theme.StatusWarnBg
import com.vacilandia.park.core.ui.theme.StatusWarnText
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.feature.tickets.model.TicketItem
import com.vacilandia.park.feature.tickets.model.TicketsUiState
import com.vacilandia.park.feature.tickets.model.VisitDateOption

private val PassStubHeight = 252.dp

@Composable
fun TicketSummaryScreen(
    state: TicketsUiState,
    selectedDate: VisitDateOption?,
    selectedEntries: List<Pair<TicketItem, Int>>,
    totalGuests: Int,
    subtotal: Double,
    discountAmount: Double,
    finalTotal: Double,
    onBack: () -> Unit,
    onResetOrder: () -> Unit,
    onReturnHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceCanvas)
    ) {
        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver a Entradas",
                    tint = InkPrimary
                )
            }
            Text(
                text = "Resumen de Reserva",
                style = MaterialTheme.typography.headlineSmall,
                color = InkPrimary
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pase QR simulado
            item(key = "pass") {
                DigitalPass(
                    state = state,
                    selectedDate = selectedDate,
                    totalGuests = totalGuests
                )
            }

            // Desglose de entradas
            item(key = "breakdown") {
                ParkCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(ParkRadius.lg)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Detalle de tarifas",
                            style = MaterialTheme.typography.titleLarge,
                            color = InkPrimary
                        )
                        selectedEntries.forEach { (ticket, qty) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(SunYellow),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$qty",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = OceanDeep
                                        )
                                    }
                                    Text(
                                        text = ticket.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = InkPrimary
                                    )
                                }
                                Text(
                                    text = formatSoles(ticket.price * qty),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = InkPrimary
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp, color = BorderDefault)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Subtotal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = InkSecondary
                            )
                            Text(
                                text = formatSoles(subtotal),
                                style = MaterialTheme.typography.bodyMedium,
                                color = InkPrimary
                            )
                        }

                        state.appliedPromo?.let { promo ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Cupón ${promo.code} (-${promo.discountPercent}%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = StatusOpenText
                                )
                                Text(
                                    text = "-${formatSoles(discountAmount)}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = StatusOpenText
                                )
                            }
                        }

                        HorizontalDivider(thickness = 1.dp, color = BorderDefault)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total referencial",
                                style = MaterialTheme.typography.titleLarge,
                                color = InkPrimary
                            )
                            Text(
                                text = formatSoles(finalTotal),
                                style = MaterialTheme.typography.headlineLarge,
                                color = OceanBlue
                            )
                        }
                    }
                }
            }

            // Aviso oficial de condiciones
            item(key = "notice") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(ParkRadius.md))
                        .background(StatusWarnBg)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = StatusWarnText,
                        modifier = Modifier.size(22.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Condiciones oficiales Vacilandia Park",
                            style = MaterialTheme.typography.titleSmall,
                            color = StatusWarnText
                        )
                        Text(
                            text = "Este resumen y código QR corresponden a una vista de demostración UI sin procesamiento de pagos reales. En la compra real, las entradas aplican solo para la fecha elegida sin cambios ni devoluciones.",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkPrimary
                        )
                    }
                }
            }
        }

        // Acciones finales
        val barShape = RoundedCornerShape(topStart = ParkRadius.lg, topEnd = ParkRadius.lg)
        Row(
            modifier = Modifier
                .oceanShadow(14.dp, barShape)
                .clip(barShape)
                .background(SurfaceCard)
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ParkOutlineButton(
                text = "Nueva reserva",
                onClick = onResetOrder,
                borderColor = BorderStrong,
                contentColor = InkPrimary,
                modifier = Modifier.weight(1f)
            )
            ParkPrimaryButton(
                text = "Volver al Inicio",
                onClick = onReturnHome,
                elevation = 0.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Pase digital con forma de entrada: cuerpo de agua profunda con la fecha,
 * talón blanco con el QR. Llega una sola vez con un resorte corto.
 */
@Composable
private fun DigitalPass(
    state: TicketsUiState,
    selectedDate: VisitDateOption?,
    totalGuests: Int
) {
    val reduced = rememberReducedMotion()
    val arrive = remember { Animatable(if (reduced) 1f else 0f) }
    val stamp = remember { Animatable(if (reduced) 1f else 0f) }
    LaunchedEffect(Unit) {
        if (!reduced) {
            arrive.animateTo(1f, spring(dampingRatio = 0.72f, stiffness = 260f))
            stamp.animateTo(1f, spring(dampingRatio = 0.4f, stiffness = 380f))
        }
    }
    val shape = TicketStubShape(cornerRadius = ParkRadius.lg, notchRadius = 14.dp, notchOffset = PassStubHeight)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = (arrive.value * 1.4f).coerceAtMost(1f)
                translationY = (1f - arrive.value) * 80.dp.toPx()
                val s = 0.92f + 0.08f * arrive.value
                scaleX = s
                scaleY = s
            }
            .oceanShadow(10.dp, shape)
            .clip(shape)
            .background(SurfaceCard)
    ) {
        // Cuerpo del pase
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OceanDeep)
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ParkTag(
                    text = "PASE DIGITAL SIMULADO (MOCKUP)",
                    textColor = OceanDeep,
                    backgroundColor = Aqua
                )
                Text(
                    text = selectedDate?.fullLabel ?: "Fecha seleccionada",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    PassFact(icon = Icons.Outlined.Schedule, text = "Full Day 9:00 AM – 6:00 PM")
                    PassFact(icon = Icons.Outlined.Groups, text = "$totalGuests personas")
                }
            }
            // Sello de confirmación
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .graphicsLayer {
                        scaleX = stamp.value
                        scaleY = stamp.value
                        rotationZ = (1f - stamp.value) * -25f
                    }
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SunYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Reserva lista",
                    tint = OceanDeep,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Talón con QR
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(PassStubHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(0.dp))
            PerforationLine(
                color = BorderStrong,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            QrCodeView(payload = "${state.reservationCode}-${selectedDate?.fullLabel}", size = 150.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = state.reservationCode,
                style = MaterialTheme.typography.titleLarge,
                color = OceanBlue
            )
        }
    }
}

@Composable
private fun PassFact(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = SunYellow, modifier = Modifier.size(16.dp))
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = OceanOnDeepMuted)
    }
}
