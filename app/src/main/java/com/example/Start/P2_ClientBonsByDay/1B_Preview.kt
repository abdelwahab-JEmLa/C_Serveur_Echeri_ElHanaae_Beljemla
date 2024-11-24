package com.example.Start.P2_ClientBonsByDay

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider


/**
 * PreviewParameter Provider for ClientBonsByDayScreen Preview
 * Add values to the sequence to see the preview in different states
 **/
class ClientBonsByDayStatePreviewParameterProvider :
    PreviewParameterProvider<DaySoldBonsScreen> {
    override val values: Sequence<DaySoldBonsScreen>
        get() = sequenceOf(
            DaySoldBonsScreen(
                daySoldBonsModel = listOf(
                    DaySoldBonsModel(
                        id = 1,
                        idClient = 101,
                        nameClient = "John Doe",

                    ),
                    DaySoldBonsModel(
                        id = 2,
                        idClient = 102,
                        nameClient = "Jane Smith",

                    )
                )
            )
        )
}
// In 4_Model.kt - Fix preview for add functionality
@Preview(showBackground = true)
@Composable
fun ClientBonsByDayScreenPreview(
    @PreviewParameter(ClientBonsByDayStatePreviewParameterProvider::class)
    state: DaySoldBonsScreen
) {
    var previewState by remember { mutableStateOf(state) }

    MaterialTheme {
        ClientBonsByDayScreen(
            state = previewState,
            actions = ClientBonsByDayActions(
                onAddBon = { newBon ->
                    // Create a preview version of the new bon with an incremented ID
                    val previewBon = newBon.copy(
                        id = (previewState.daySoldBonsModel.maxOfOrNull { it.id } ?: 0) + 1
                    )
                    // Update preview state with new bon
                    previewState = previewState.copy(
                        daySoldBonsModel = previewState.daySoldBonsModel + previewBon
                    )
                },
            )
        )
    }
}
