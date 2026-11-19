package com.vacilandia.park

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vacilandia.park.core.navigation.AppRoutes
import com.vacilandia.park.core.ui.components.VacilandiaBottomBar
import com.vacilandia.park.core.ui.theme.SurfaceCanvas
import com.vacilandia.park.feature.attractions.controller.AttractionsController
import com.vacilandia.park.feature.attractions.view.AttractionDetailScreen
import com.vacilandia.park.feature.attractions.view.AttractionsScreen
import com.vacilandia.park.feature.home.controller.HomeController
import com.vacilandia.park.feature.home.view.HomeScreen
import com.vacilandia.park.feature.info.controller.InfoController
import com.vacilandia.park.feature.info.view.InfoScreen
import com.vacilandia.park.feature.tickets.controller.TicketsController
import com.vacilandia.park.feature.tickets.view.TicketSummaryScreen
import com.vacilandia.park.feature.tickets.view.TicketsScreen

// Transiciones: pestañas con fade-through; detalle y resumen con eje compartido horizontal.
private val pushEnter = slideInHorizontally(tween(320)) { it / 3 } + fadeIn(tween(240))
private val pushExit = slideOutHorizontally(tween(320)) { -it / 6 } + fadeOut(tween(200))
private val popEnter = slideInHorizontally(tween(300)) { -it / 6 } + fadeIn(tween(220))
private val popExit = slideOutHorizontally(tween(260)) { it / 3 } + fadeOut(tween(180))

class AppControllers : ViewModel() {
    val homeController = HomeController()
    val attractionsController = AttractionsController()
    val ticketsController = TicketsController()
    val infoController = InfoController()
}

@Composable
fun VacilandiaApp(
    controllers: AppControllers = viewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val homeState by controllers.homeController.uiState.collectAsState()
    val attractionsState by controllers.attractionsController.uiState.collectAsState()
    val ticketsState by controllers.ticketsController.uiState.collectAsState()
    val infoState by controllers.infoController.uiState.collectAsState()

    val ticketCount = controllers.ticketsController.totalItemCount(ticketsState)

    val showBottomBar = currentRoute == AppRoutes.HOME ||
        currentRoute == AppRoutes.ATTRACTIONS ||
        currentRoute == AppRoutes.TICKETS ||
        currentRoute?.startsWith(AppRoutes.INFO_BASE) == true

    fun navigateToTopLevel(route: String) {
        val shouldRestore = !route.startsWith(AppRoutes.INFO_BASE)
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = shouldRestore
        }
    }

    Scaffold(
        containerColor = SurfaceCanvas,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                VacilandiaBottomBar(
                    currentRoute = currentRoute,
                    ticketCount = ticketCount,
                    onNavigate = { targetRoute ->
                        val destination = if (targetRoute == AppRoutes.INFO_BASE) {
                            AppRoutes.info("default")
                        } else {
                            targetRoute
                        }
                        navigateToTopLevel(destination)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            enterTransition = { fadeIn(tween(220, delayMillis = 60)) + scaleIn(tween(220, delayMillis = 60), initialScale = 0.98f) },
            exitTransition = { fadeOut(tween(90)) },
            popEnterTransition = { fadeIn(tween(220, delayMillis = 60)) + scaleIn(tween(220, delayMillis = 60), initialScale = 0.98f) },
            popExitTransition = { fadeOut(tween(90)) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(AppRoutes.HOME) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceCanvas)
                ) {
                    HomeScreen(
                        homeState = homeState,
                        allAttractions = attractionsState.attractions,
                        favoriteIds = attractionsState.favoriteIds,
                        onQuickFilterSelected = controllers.homeController::selectQuickFilter,
                        onOpenPromoSheet = controllers.homeController::openPromoSheet,
                        onClosePromoSheet = controllers.homeController::closePromoSheet,
                        onApplyPromoOffer = { ticketId ->
                            controllers.ticketsController.applyOfferFromHome(ticketId)
                            controllers.homeController.closePromoSheet()
                            navigateToTopLevel(AppRoutes.TICKETS)
                        },
                        onGoToTickets = { navigateToTopLevel(AppRoutes.TICKETS) },
                        onGoToAttractions = { navigateToTopLevel(AppRoutes.ATTRACTIONS) },
                        onGoToMap = { navigateToTopLevel(AppRoutes.info("map")) },
                        onGoToRules = { navigateToTopLevel(AppRoutes.info("rules")) },
                        onOpenAttractionDetail = { id ->
                            navController.navigate(AppRoutes.attractionDetail(id))
                        },
                        onToggleFavorite = controllers.attractionsController::toggleFavorite
                    )
                }
            }

            composable(AppRoutes.ATTRACTIONS) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceCanvas)
                        .statusBarsPadding()
                ) {
                    AttractionsScreen(
                        state = attractionsState,
                        filteredAttractions = controllers.attractionsController.filteredAttractions(attractionsState),
                        onQueryChange = controllers.attractionsController::updateSearchQuery,
                        onCategorySelected = controllers.attractionsController::selectCategory,
                        onToggleFavoritesOnly = controllers.attractionsController::toggleFavoritesFilter,
                        onClearFilters = controllers.attractionsController::clearFilters,
                        onToggleFavorite = controllers.attractionsController::toggleFavorite,
                        onOpenDetail = { id ->
                            navController.navigate(AppRoutes.attractionDetail(id))
                        }
                    )
                }
            }

            composable(
                route = AppRoutes.ATTRACTION_DETAIL,
                arguments = listOf(navArgument("attractionId") { type = NavType.StringType }),
                enterTransition = { pushEnter },
                exitTransition = { pushExit },
                popEnterTransition = { popEnter },
                popExitTransition = { popExit }
            ) { entry ->
                val attractionId = entry.arguments?.getString("attractionId").orEmpty()
                val attraction = controllers.attractionsController.getAttractionById(attractionId)

                AttractionDetailScreen(
                    attraction = attraction,
                    isFavorite = attractionsState.favoriteIds.contains(attractionId),
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { controllers.attractionsController.toggleFavorite(attractionId) },
                    onGoToTickets = {
                        navController.popBackStack()
                        navigateToTopLevel(AppRoutes.TICKETS)
                    }
                )
            }

            composable(AppRoutes.TICKETS) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceCanvas)
                        .statusBarsPadding()
                ) {
                    TicketsScreen(
                        state = ticketsState,
                        selectedDate = controllers.ticketsController.selectedDate(ticketsState),
                        isTicketAvailable = { t -> controllers.ticketsController.isAvailable(t, ticketsState) },
                        totalItemCount = ticketCount,
                        totalGuestCount = controllers.ticketsController.totalGuestCount(ticketsState),
                        subtotal = controllers.ticketsController.subtotal(ticketsState),
                        discountAmount = controllers.ticketsController.discountAmount(ticketsState),
                        finalTotal = controllers.ticketsController.finalTotal(ticketsState),
                        onSelectDate = controllers.ticketsController::selectDate,
                        onIncrement = controllers.ticketsController::increment,
                        onDecrement = controllers.ticketsController::decrement,
                        onPromoInputChange = controllers.ticketsController::updatePromoInput,
                        onApplyPromoCode = controllers.ticketsController::applyPromoCode,
                        onRemovePromoCode = controllers.ticketsController::removePromoCode,
                        onContinueToSummary = {
                            navController.navigate(AppRoutes.TICKET_SUMMARY)
                        }
                    )
                }
            }

            composable(
                route = AppRoutes.TICKET_SUMMARY,
                enterTransition = { pushEnter },
                exitTransition = { pushExit },
                popEnterTransition = { popEnter },
                popExitTransition = { popExit }
            ) {
                TicketSummaryScreen(
                    state = ticketsState,
                    selectedDate = controllers.ticketsController.selectedDate(ticketsState),
                    selectedEntries = controllers.ticketsController.selectedEntries(ticketsState),
                    totalGuests = controllers.ticketsController.totalGuestCount(ticketsState),
                    subtotal = controllers.ticketsController.subtotal(ticketsState),
                    discountAmount = controllers.ticketsController.discountAmount(ticketsState),
                    finalTotal = controllers.ticketsController.finalTotal(ticketsState),
                    onBack = { navController.popBackStack() },
                    onResetOrder = {
                        controllers.ticketsController.resetOrder()
                        navController.popBackStack()
                    },
                    onReturnHome = {
                        navigateToTopLevel(AppRoutes.HOME)
                    }
                )
            }

            composable(
                route = AppRoutes.INFO,
                arguments = listOf(
                    navArgument("section") {
                        type = NavType.StringType
                        defaultValue = "default"
                    }
                )
            ) { entry ->
                val section = entry.arguments?.getString("section")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SurfaceCanvas)
                        .statusBarsPadding()
                ) {
                    InfoScreen(
                        state = infoState,
                        selectedZone = controllers.infoController.selectedZone(infoState),
                        initialSection = section,
                        onSelectZone = controllers.infoController::selectZone,
                        onToggleFaq = controllers.infoController::toggleFaq,
                        onToggleChecklistItem = controllers.infoController::toggleChecklistItem,
                        onOpenAttraction = { id ->
                            navController.navigate(AppRoutes.attractionDetail(id))
                        }
                    )
                }
            }
        }
    }
}
