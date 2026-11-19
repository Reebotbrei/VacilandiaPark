package com.vacilandia.park.feature.home.controller

import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.home.model.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

class HomeController {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            greeting = resolveGreeting(),
            parkStatus = resolveParkStatus(),
            promoOffer = MockParkRepository.getFeaturedPromo()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectQuickFilter(category: AttractionCategory) {
        _uiState.update { it.copy(selectedFilter = category) }
    }

    fun openPromoSheet() {
        _uiState.update { it.copy(showPromoSheet = true) }
    }

    fun closePromoSheet() {
        _uiState.update { it.copy(showPromoSheet = false) }
    }

    private fun resolveGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Buenos días · Full Day"
            hour < 19 -> "Buenas tardes · Full Day"
            else -> "Buenas noches · Planifica tu visita"
        }
    }

    private fun resolveParkStatus(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 9..17 -> "Abierto ahora · Cierra 6:00 PM"
            hour < 9 -> "Abre hoy · 9:00 AM – 6:00 PM"
            else -> "Cerrado · Abre mañana 9:00 AM"
        }
    }
}
