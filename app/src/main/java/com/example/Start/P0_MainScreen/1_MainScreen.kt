package com.example.Start.P0_MainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.serveurecherielhanaaebeljemla.AppViewModels

@Composable
fun MainScreen(appViewModels: AppViewModels) {
    val clientBonsByDayState by appViewModels.clientBonsByDayViewModel.state.collectAsState()

    when {
        clientBonsByDayState.isLoading -> {
            LoadingScreen()
        }

        clientBonsByDayState.isInitialized -> {
            MainContent(modifier = Modifier)
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}



@Composable
private fun MainContent(modifier: Modifier) {
    val navController = rememberNavController()
    val items = NavigationItems.getItems()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var isFabVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f)) {
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            AnimatedVisibility(
                visible = true,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                NavigationBarWithFab(
                    // Fixed the filtering logic to avoid type comparison issues
                    items = items,
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
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}
