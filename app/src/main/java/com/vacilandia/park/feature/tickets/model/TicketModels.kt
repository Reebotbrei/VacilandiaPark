package com.vacilandia.park.feature.tickets.model

import androidx.annotation.DrawableRes

enum class TicketAvailability {
    ALL_DAYS,
    WEEKDAYS_ONLY,
    WEEKENDS_ONLY
}

enum class TicketGroup(val label: String) {
    INDIVIDUAL("Adultos y Niños"),
    FAMILIAR("Paquetes Familiares"),
    ESPECIAL("Tarifas Especiales y Libres")
}

data class TicketItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val price: Double,
    val unitLabel: String,
    val group: TicketGroup,
    val availability: TicketAvailability,
    val imageUrl: String,
    @param:DrawableRes val fallbackRes: Int,
    val includes: List<String>,
    val conditions: String
)

data class VisitDateOption(
    val index: Int,
    val shortDay: String,
    val dayNumber: String,
    val monthLabel: String,
    val fullLabel: String,
    val isWeekend: Boolean
)

data class PromoOffer(
    val id: String,
    val ticketId: String,
    val title: String,
    val badge: String,
    val priceLabel: String,
    val summary: String,
    val terms: String,
    val promoCodeHint: String,
    val imageUrl: String,
    @param:DrawableRes val fallbackRes: Int
)

data class AppliedPromoCode(
    val code: String,
    val discountPercent: Int,
    val label: String
)

data class TicketsUiState(
    val availableDates: List<VisitDateOption> = emptyList(),
    val selectedDateIndex: Int = 0,
    val tickets: List<TicketItem> = emptyList(),
    val quantities: Map<String, Int> = emptyMap(),
    val promoInput: String = "",
    val appliedPromo: AppliedPromoCode? = null,
    val promoError: String? = null,
    val reservationCode: String = "VACI-2026-8421"
)
