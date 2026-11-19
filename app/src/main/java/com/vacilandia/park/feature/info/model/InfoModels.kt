package com.vacilandia.park.feature.info.model

data class ParkZone(
    val id: String,
    val number: Int,
    val name: String,
    val category: String,
    val summary: String,
    val xFraction: Float,
    val yFraction: Float,
    val attractionId: String
)

data class FaqItem(
    val id: String,
    val question: String,
    val answer: String
)

data class ChecklistItem(
    val id: String,
    val label: String,
    val note: String
)

data class InfoUiState(
    val zones: List<ParkZone> = emptyList(),
    val selectedZoneId: String = "",
    val faqs: List<FaqItem> = emptyList(),
    val expandedFaqIds: Set<String> = emptySet(),
    val checklist: List<ChecklistItem> = emptyList(),
    val checkedItemIds: Set<String> = emptySet(),
    val permittedRules: List<String> = emptyList(),
    val prohibitedRules: List<String> = emptyList()
)
