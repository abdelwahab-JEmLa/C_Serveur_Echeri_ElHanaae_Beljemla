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
 * Provides sample data for the preview
 */
class ClientBonsByDayStatePreviewParameterProvider :
    PreviewParameterProvider<DaySoldBonsScreen> {
    override val values: Sequence<DaySoldBonsScreen>
        get() = sequenceOf(
            DaySoldBonsScreen(
                daySoldBonsModel = listOf(
                    DaySoldBonsModel(
                        date = "2024-11-24",
                        id = 1,
                        idClient = 8,
                        nameClient = "(Adlen Bananie)",
                        payed = 7862.4,
                        total = 7862.4
                    ),
                    DaySoldBonsModel(
                        date = "2024-11-24",
                        id = 2,
                        idClient = 1,
                        nameClient = "(Abderahman Tamaris)",
                        payed = 11425.4,
                        total = 11425.4
                    )
                )
            )
        )
}

@Preview(showBackground = true)
@Composable
fun ClientBonsByDayScreenPreview(
    @PreviewParameter(ClientBonsByDayStatePreviewParameterProvider::class)
    state: DaySoldBonsScreen
) {
    MaterialTheme {
        ClientBonsByDayScreen(
            state = state,
            actions = ClientBonsByDayActions(
                onAddBon = {}
            )
        )
    }
}
