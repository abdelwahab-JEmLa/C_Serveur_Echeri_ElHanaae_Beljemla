package com.example.Start.P2_ClientBonsByDay

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen


/**
 * PreviewParameter Provider for ClientBonsByDayScreen Preview
 * Provides sample data for the preview
 */
class ClientBonsByDayStatePreviewParameterProvider :
    PreviewParameterProvider<DaySoldBonsScreen> {
    override val values: Sequence<DaySoldBonsScreen> = sequenceOf(
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

    // Provide a default value
    override val count: Int = 1
}

@Preview(showBackground = true)
@Composable
fun ClientBonsByDayScreenParameterizedPreview(
    @PreviewParameter(ClientBonsByDayStatePreviewParameterProvider::class)
    state: DaySoldBonsScreen
) {
    MaterialTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
        }
    }
}
