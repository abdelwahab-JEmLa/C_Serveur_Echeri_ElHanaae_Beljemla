package com.example.Start.P2_ClientBonsByDay.clientbonsbyday

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * Object used for a type safe destination to a Home screen
 */
@Serializable
data class ClientBonsByDayDestination(val route: String = "clientBonsByDay") : java.io.Serializable

/**
 * UI State that represents ClientBonsByDayScreen
 **/
data class ClientBonsByDayState(
    val clientBonsByDay: List<ClientBonsByDay> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isInitialized: Boolean = false
)

@Entity(tableName = "client_bons_by_day")
data class ClientBonsByDay(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val idClient: Long = 0,
    val nameClient: String = "",
    val total: Boolean = false,
    val payed: Long = 0,
    val date: Date = Date()
) {
    // No-argument constructor for Firebase
    constructor() : this(0)
}

/**
 * ClientBonsByDay Actions emitted from the UI Layer
 * passed to the coordinator to handle
 **/
data class ClientBonsByDayActions(
    val onClick: () -> Unit = {},
    val onAddBon: (ClientBonsByDay) -> Unit = {},
    val onUpdateBon: (ClientBonsByDay) -> Unit = {},
    val onDeleteBon: (ClientBonsByDay) -> Unit = {}
)

// In ClientBonsByDayRoute.kt
@Composable
fun rememberClientBonsByDayActions(viewModel: ClientBonsByDayViewModel): ClientBonsByDayActions {
    return remember(viewModel) {
        ClientBonsByDayActions(
            onClick = {},
            onAddBon = { bon -> viewModel.addBon(bon) },
            onUpdateBon = { bon -> viewModel.updateBon(bon) },
            onDeleteBon = { bon -> viewModel.deleteBon(bon) }
        )
    }
}

/**
 * PreviewParameter Provider for ClientBonsByDayScreen Preview
 * Add values to the sequence to see the preview in different states
 **/
class ClientBonsByDayStatePreviewParameterProvider :
    PreviewParameterProvider<ClientBonsByDayState> {
    override val values: Sequence<ClientBonsByDayState>
        get() = sequenceOf(
            ClientBonsByDayState(
                clientBonsByDay = listOf(
                    ClientBonsByDay(
                        id = 1,
                        idClient = 101,
                        nameClient = "John Doe",
                        total = true,
                        payed = 1500,
                        date = Date()
                    ),
                    ClientBonsByDay(
                        id = 2,
                        idClient = 102,
                        nameClient = "Jane Smith",
                        total = false,
                        payed = 2500,
                        date = Date()
                    )
                )
            )
        )
}
// In ClientBonsByDayContract.kt - Fix preview for add functionality
@Preview(showBackground = true)
@Composable
fun ClientBonsByDayScreenPreview(
    @PreviewParameter(ClientBonsByDayStatePreviewParameterProvider::class)
    state: ClientBonsByDayState
) {
    var previewState by remember { mutableStateOf(state) }

    MaterialTheme {
        ClientBonsByDayScreen(
            state = previewState,
            actions = ClientBonsByDayActions(
                onClick = {},
                onAddBon = { newBon ->
                    // Create a preview version of the new bon with an incremented ID
                    val previewBon = newBon.copy(
                        id = (previewState.clientBonsByDay.maxOfOrNull { it.id } ?: 0) + 1
                    )
                    // Update preview state with new bon
                    previewState = previewState.copy(
                        clientBonsByDay = previewState.clientBonsByDay + previewBon
                    )
                },
                onUpdateBon = {},
                onDeleteBon = {}
            )
        )
    }
}

