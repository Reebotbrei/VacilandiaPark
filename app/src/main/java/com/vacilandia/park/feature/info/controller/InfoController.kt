package com.vacilandia.park.feature.info.controller

import com.vacilandia.park.core.data.MockParkRepository
import com.vacilandia.park.feature.info.model.InfoUiState
import com.vacilandia.park.feature.info.model.ParkZone
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InfoController {

    private val _uiState = MutableStateFlow(
        InfoUiState(
            zones = MockParkRepository.getParkZones(),
            selectedZoneId = "z2",
            faqs = MockParkRepository.getFaqs(),
            expandedFaqIds = setOf("faq1"),
            checklist = MockParkRepository.getChecklist(),
            checkedItemIds = setOf("chk1", "chk2"),
            permittedRules = MockParkRepository.getPermittedRules(),
            prohibitedRules = MockParkRepository.getProhibitedRules()
        )
    )
    val uiState: StateFlow<InfoUiState> = _uiState.asStateFlow()

    fun selectZone(zoneId: String) {
        _uiState.update { it.copy(selectedZoneId = zoneId) }
    }

    fun selectedZone(state: InfoUiState): ParkZone? {
        return state.zones.firstOrNull { it.id == state.selectedZoneId } ?: state.zones.firstOrNull()
    }

    fun toggleFaq(faqId: String) {
        _uiState.update { current ->
            val updated = current.expandedFaqIds.toMutableSet()
            if (updated.contains(faqId)) updated.remove(faqId) else updated.add(faqId)
            current.copy(expandedFaqIds = updated)
        }
    }

    fun toggleChecklistItem(itemId: String) {
        _uiState.update { current ->
            val updated = current.checkedItemIds.toMutableSet()
            if (updated.contains(itemId)) updated.remove(itemId) else updated.add(itemId)
            current.copy(checkedItemIds = updated)
        }
    }
}
