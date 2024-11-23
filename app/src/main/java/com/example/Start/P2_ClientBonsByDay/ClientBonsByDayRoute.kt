package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable

/**
 * Object used for a type safe destination to a Home screen
 */
@Serializable
data class ClientBonsByDayDestination(val route: String = "clientBonsByDay") : java.io.Serializable

// Then update the Route to use this directly:
@Composable
fun ClientBonsByDayRoute(
    viewModel: ClientBonsByDayViewModel = hiltViewModel()
) {
    // State observing and declarations
    val uiState by viewModel.state.collectAsStateWithLifecycle(ClientBonsByDayState())

    // UI Actions now using the updated remember function
    val actions = rememberClientBonsByDayActions(viewModel)

    // UI Rendering
    ClientBonsByDayScreen(uiState, actions)
}

