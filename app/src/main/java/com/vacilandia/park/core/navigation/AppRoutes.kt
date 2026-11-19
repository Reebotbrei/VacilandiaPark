package com.vacilandia.park.core.navigation

object AppRoutes {
    const val HOME = "home"
    const val ATTRACTIONS = "attractions"
    const val ATTRACTION_DETAIL = "attraction_detail/{attractionId}"
    const val TICKETS = "tickets"
    const val TICKET_SUMMARY = "ticket_summary"
    const val INFO_BASE = "info"
    const val INFO = "info?section={section}"

    fun attractionDetail(attractionId: String): String = "attraction_detail/$attractionId"

    fun info(section: String = "default"): String = "info?section=$section"
}
