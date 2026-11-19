package com.vacilandia.park.feature.attractions.model

import androidx.annotation.DrawableRes

enum class AttractionCategory(val label: String) {
    ALL("Todos"),
    ADRENALINA("Adrenalina"),
    FAMILIAR("Familiar"),
    INFANTIL("Infantil"),
    GASTRONOMIA("Gastronomía"),
    DEPORTES_ECO("Deportes y Eco")
}

data class AttractionGalleryPhoto(
    val imageUrl: String,
    @param:DrawableRes val fallbackRes: Int,
    val caption: String
)

data class Attraction(
    val id: String,
    val name: String,
    val category: AttractionCategory,
    val tags: List<String>,
    val imageUrl: String,
    @param:DrawableRes val fallbackRes: Int,
    val gallery: List<AttractionGalleryPhoto>,
    val minHeight: String,
    val depth: String,
    val waitTime: String,
    val schedule: String,
    val shortDescription: String,
    val fullDescription: String,
    val highlights: List<String>,
    val safetyTips: List<String>
)

data class AttractionsUiState(
    val searchQuery: String = "",
    val selectedCategory: AttractionCategory = AttractionCategory.ALL,
    val favoritesOnly: Boolean = false,
    val favoriteIds: Set<String> = emptySet(),
    val attractions: List<Attraction> = emptyList()
)
