package com.example.Start.P2_ClientBonsByDay

import P1_StartupScreen.Main.FloatingActionButtonGroup.FloatingActionButtonGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.Start.P2_ClientBonsByDay.Ui.AddBonDialog

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
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Main screen content
        ClientBonsByDayScreen(
            uiState,
            actions
        )

        // FAB and Dialog container
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
        ) {
            // Main FAB
            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .offset(y = (-16).dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Bon")
            }

            // Animated FAB group
            FloatingActionButtonGroup(
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Add Bon Dialog
        if (showDialog) {
            AddBonDialog(
                daySoldBonsModel = uiState.daySoldBonsModel,
                actions = actions,
                onDismiss = { showDialog = false }
            )
        }
    }
}
