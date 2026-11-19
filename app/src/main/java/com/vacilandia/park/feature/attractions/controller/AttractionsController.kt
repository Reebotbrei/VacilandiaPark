package com.vacilandia.park.feature.attractions.controller

import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.feature.attractions.model.Attraction
import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.attractions.model.AttractionsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AttractionsController {

    private val _uiState = MutableStateFlow(
        AttractionsUiState(
            attractions = MockParkRepository.getAttractions()
        )
    )
    val uiState: StateFlow<AttractionsUiState> = _uiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun selectCategory(category: AttractionCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleFavoritesFilter() {
        _uiState.update { it.copy(favoritesOnly = !it.favoritesOnly) }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                searchQuery = "",
                selectedCategory = AttractionCategory.ALL,
                favoritesOnly = false
            )
        }
    }

    fun toggleFavorite(attractionId: String) {
        _uiState.update { current ->
            val updated = current.favoriteIds.toMutableSet()
            if (updated.contains(attractionId)) updated.remove(attractionId)
            else updated.add(attractionId)
            current.copy(favoriteIds = updated)
        }
    }

    fun getAttractionById(id: String): Attraction? {
        return _uiState.value.attractions.firstOrNull { it.id == id }
    }

    fun filteredAttractions(state: AttractionsUiState): List<Attraction> {
        val q = state.searchQuery.trim().lowercase()
        return state.attractions.filter { item ->
            val matchesCat =
                state.selectedCategory == AttractionCategory.ALL ||
                    item.category == state.selectedCategory ||
                    item.tags.any { it.equals(state.selectedCategory.label, ignoreCase = true) }

            val matchesFav = !state.favoritesOnly || state.favoriteIds.contains(item.id)

            val matchesQuery = q.isEmpty() ||
                item.name.lowercase().contains(q) ||
                item.shortDescription.lowercase().contains(q) ||
                item.tags.any { it.lowercase().contains(q) }

            matchesCat && matchesFav && matchesQuery
        }
    }
}
