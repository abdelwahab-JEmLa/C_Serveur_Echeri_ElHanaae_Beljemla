package com.example.Start.P2_ClientBonsByDay

import P1_StartupScreen.Main.FloatingActionButtonGroup.FloatingActionButtonGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.Start.P2_ClientBonsByDay.Ui.DateDefiner
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
    val uiState by viewModel.state.collectAsStateWithLifecycle(DaySoldBonsScreen())
    val actions = rememberClientBonsByDayActions(viewModel)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
        ) {
            DateDefiner(
                Modifier,
                uiState,
                actions)
            ClientBonsByDayScreen(
                uiState,
                actions
            )
        }
        

        // FAB and Dialog container
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
        ) {

            // Animated FAB group
            FloatingActionButtonGroup(
                modifier = Modifier.padding(top = 8.dp)
            )
        }


    }
}
