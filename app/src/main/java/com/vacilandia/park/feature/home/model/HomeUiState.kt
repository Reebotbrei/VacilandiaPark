package com.vacilandia.park.feature.home.model

import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.tickets.model.PromoOffer

data class HomeUiState(
    val greeting: String = "Bienvenido a Vacilandia",
    val parkStatus: String = "Abierto ahora · Cierra 6:00 PM",
    val openHoursDetail: String = "Atención los 365 días · 9:00 AM – 6:00 PM",
    val weatherText: String = "25°C Soleado",
    val waterTempText: String = "Agua 26°C",
    val occupancyProgress: Float = 0.58f,
    val occupancyLabel: String = "Aforo actual: 58% (Moderado)",
    val selectedFilter: AttractionCategory = AttractionCategory.ALL,
    val promoOffer: PromoOffer? = null,
    val showPromoSheet: Boolean = false
)
