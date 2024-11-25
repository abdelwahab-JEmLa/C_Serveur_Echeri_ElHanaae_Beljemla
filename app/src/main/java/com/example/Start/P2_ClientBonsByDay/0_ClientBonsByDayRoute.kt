package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen
import kotlinx.serialization.Serializable

/**
 * Object used for a type safe destination to a Home screen
 */
@Serializable
data class ClientBonsByDayDestination(val route: String = "clientBonsByDay") : java.io.Serializable

@Composable
fun ClientBonsByDayRoute(
    viewModel: ClientBonsByDayViewModel = hiltViewModel()
) {
    // State observing and declarations
    val state by viewModel.state.collectAsStateWithLifecycle(DaySoldBonsScreen())
    val actions = rememberClientBonsByDayActions(viewModel)

    ClientBonsByDayScreen(state,actions)
}
