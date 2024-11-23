package com.example.Start.P2_ClientBonsByDay.clientbonsbyday

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel


import androidx.lifecycle.compose.collectAsStateWithLifecycle


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

