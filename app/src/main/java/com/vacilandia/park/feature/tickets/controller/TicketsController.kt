package com.vacilandia.park.feature.tickets.controller

import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.feature.tickets.model.AppliedPromoCode
import com.vacilandia.park.feature.tickets.model.TicketAvailability
import com.vacilandia.park.feature.tickets.model.TicketItem
import com.vacilandia.park.feature.tickets.model.TicketsUiState
import com.vacilandia.park.feature.tickets.model.VisitDateOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class TicketsController {

    private val _uiState = MutableStateFlow(
        TicketsUiState(
            availableDates = MockParkRepository.getVisitDates(),
            tickets = MockParkRepository.getTickets()
        )
    )
    val uiState: StateFlow<TicketsUiState> = _uiState.asStateFlow()

    fun selectDate(index: Int) {
        _uiState.update { current ->
            val targetDate = current.availableDates.getOrNull(index) ?: return@update current
            val cleanedQuantities = current.quantities.filterKeys { ticketId ->
                val ticket = current.tickets.firstOrNull { it.id == ticketId }
                ticket != null && isTicketAvailableOnDate(ticket, targetDate)
            }
            current.copy(
                selectedDateIndex = index,
                quantities = cleanedQuantities
            )
        }
    }

    fun increment(ticketId: String) {
        _uiState.update { current ->
            val ticket = current.tickets.firstOrNull { it.id == ticketId } ?: return@update current
            if (!isAvailable(ticket, current)) return@update current

            val currentQty = current.quantities[ticketId] ?: 0
            if (currentQty >= 15) return@update current

            val updated = current.quantities.toMutableMap()
            updated[ticketId] = currentQty + 1
            current.copy(quantities = updated)
        }
    }

    fun decrement(ticketId: String) {
        _uiState.update { current ->
            val currentQty = current.quantities[ticketId] ?: 0
            if (currentQty <= 0) return@update current

            val updated = current.quantities.toMutableMap()
            if (currentQty == 1) updated.remove(ticketId) else updated[ticketId] = currentQty - 1
            current.copy(quantities = updated)
        }
    }

    fun applyOfferFromHome(ticketId: String) {
        _uiState.update { current ->
            val ticket = current.tickets.firstOrNull { it.id == ticketId } ?: return@update current
            val selectedDate = current.availableDates.getOrNull(current.selectedDateIndex)
            val newDateIndex = if (selectedDate != null && !isTicketAvailableOnDate(ticket, selectedDate)) {
                current.availableDates.firstOrNull { isTicketAvailableOnDate(ticket, it) }?.index
                    ?: current.selectedDateIndex
            } else {
                current.selectedDateIndex
            }

            val updated = current.quantities.toMutableMap()
            val qty = updated[ticketId] ?: 0
            if (qty == 0) updated[ticketId] = 1

            current.copy(
                selectedDateIndex = newDateIndex,
                quantities = updated
            )
        }
    }

    fun updatePromoInput(value: String) {
        _uiState.update {
            it.copy(
                promoInput = value.uppercase(Locale.getDefault()),
                promoError = null
            )
        }
    }

    fun applyPromoCode() {
        _uiState.update { current ->
            val normalized = current.promoInput.trim().uppercase(Locale.getDefault())
            when (normalized) {
                "VERANO20" -> current.copy(
                    appliedPromo = AppliedPromoCode("VERANO20", 20, "20% dto. simulado aplicado"),
                    promoError = null
                )
                "VACI10" -> current.copy(
                    appliedPromo = AppliedPromoCode("VACI10", 10, "10% dto. web aplicado"),
                    promoError = null
                )
                "" -> current.copy(
                    promoError = "Ingresa un código (prueba VERANO20 o VACI10)."
                )
                else -> current.copy(
                    promoError = "Código no válido. Prueba VERANO20 o VACI10."
                )
            }
        }
    }

    fun removePromoCode() {
        _uiState.update {
            it.copy(appliedPromo = null, promoInput = "", promoError = null)
        }
    }

    fun resetOrder() {
        _uiState.update {
            it.copy(
                quantities = emptyMap(),
                promoInput = "",
                appliedPromo = null,
                promoError = null
            )
        }
    }

    fun selectedDate(state: TicketsUiState): VisitDateOption? {
        return state.availableDates.getOrNull(state.selectedDateIndex)
    }

    fun isAvailable(ticket: TicketItem, state: TicketsUiState): Boolean {
        val date = selectedDate(state) ?: return true
        return isTicketAvailableOnDate(ticket, date)
    }

    private fun isTicketAvailableOnDate(ticket: TicketItem, date: VisitDateOption): Boolean {
        return when (ticket.availability) {
            TicketAvailability.ALL_DAYS -> true
            TicketAvailability.WEEKDAYS_ONLY -> !date.isWeekend
            TicketAvailability.WEEKENDS_ONLY -> date.isWeekend
        }
    }

    fun selectedEntries(state: TicketsUiState): List<Pair<TicketItem, Int>> {
        return state.tickets.mapNotNull { ticket ->
            val qty = state.quantities[ticket.id] ?: 0
            if (qty > 0 && isAvailable(ticket, state)) ticket to qty else null
        }
    }

    fun totalItemCount(state: TicketsUiState): Int {
        return selectedEntries(state).sumOf { it.second }
    }

    fun totalGuestCount(state: TicketsUiState): Int {
        return selectedEntries(state).sumOf { (ticket, qty) ->
            val multiplier = if (ticket.id == "familiar-5") 5 else 1
            qty * multiplier
        }
    }

    fun subtotal(state: TicketsUiState): Double {
        return selectedEntries(state).sumOf { (ticket, qty) -> ticket.price * qty }
    }

    fun discountAmount(state: TicketsUiState): Double {
        val promo = state.appliedPromo ?: return 0.0
        return subtotal(state) * (promo.discountPercent / 100.0)
    }

    fun finalTotal(state: TicketsUiState): Double {
        return (subtotal(state) - discountAmount(state)).coerceAtLeast(0.0)
    }
}
