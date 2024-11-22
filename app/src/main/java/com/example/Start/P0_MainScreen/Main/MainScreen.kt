package com.example.Start.P0_MainScreen.Main

import P0_MainScreen.Ui.Main.AppNavHost.AppNavHost
import P0_MainScreen.Ui.Main.AppNavHost.NavigationBarWithFab
import P0_MainScreen.Ui.Main.AppNavHost.NavigationItems
import P0_MainScreen.Ui.Main.AppNavHost.Screen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.serveurecherielhanaaebeljemla.AppViewModels

@Composable
fun MainScreen(
    appViewModels: AppViewModels,
    modifier: Modifier = Modifier
) {
    val headViewModel = appViewModels.headViewModel
    val uiState by headViewModel.uiState.collectAsState()
    val productDisplayController = uiState.productDisplayController



    // Navigation setup
    val navController = rememberNavController()
    val items = NavigationItems.getItems()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // State management
    var isNavBarVisible by remember { mutableStateOf(true) }
    var isFabVisible by remember { mutableStateOf(false) }
    var isDisplayedConnexionWifiVisible by remember { mutableStateOf(false) }
    var showProductDisplay by remember { mutableStateOf(false) }
    var lockHost by remember { mutableStateOf(false) }



    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {

                // Main Content Area
                Box(modifier = Modifier.weight(1f)) {
                    AppNavHost(
                        appViewModels = appViewModels,
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        isFabVisible = isFabVisible
                    )

                    // Disable interactions when not host phone
                    if (!productDisplayController.isHostPhone && productDisplayController.isConnected) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable(enabled = false) { }
                        )
                    }
                }
            }

            // Navigation Bar with FAB
            AnimatedVisibility(
                visible = productDisplayController.isHostPhone || !productDisplayController.isConnected,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                NavigationBarWithFab(
                    items = items.filter { it != Screen.ToggleFab },
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isFabVisible = isFabVisible,
                    onToggleFabVisibility = {
                        isFabVisible = !isFabVisible
                        isDisplayedConnexionWifiVisible = false
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }


            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { uiState.loadingProgress / 100f },
                        modifier = Modifier.size(48.dp),
                        trackColor = ProgressIndicatorDefaults.circularTrackColor,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
