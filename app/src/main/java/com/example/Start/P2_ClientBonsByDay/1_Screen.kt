package com.example.Start.P2_ClientBonsByDay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Headers row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GridHeader(
                modifier = Modifier.weight(0.5f),
                text = "ID"
            )
            GridHeader(
                modifier = Modifier.weight(0.5f),
                text = "Client ID"
            )
            GridHeader(
                modifier = Modifier.weight(1f),
                text = "Client Name"
            )
            GridHeader(
                modifier = Modifier.weight(1f),
                text = "Total"
            )
            GridHeader(
                modifier = Modifier.weight(1f),
                text = "Payed"
            )
            GridHeader(
                modifier = Modifier.weight(1f),
                text = "Date"
            )
        }

        // Data rows
        state.daySoldBonsModel.forEach { bon ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // ID column - narrower
                GridCell(
                    modifier = Modifier.weight(0.5f),
                    text = bon.id.toString()
                )
                // Client ID column - narrower
                GridCell(
                    modifier = Modifier.weight(0.5f),
                    text = bon.idClient.toString()
                )
                // Other columns - normal width
                GridCell(
                    modifier = Modifier.weight(1f),
                    text = bon.nameClient
                )
                GridCell(
                    modifier = Modifier.weight(1f),
                    text = "%.2f".format(bon.total)
                )
                GridCell(
                    modifier = Modifier.weight(1f),
                    text = "%.2f".format(bon.payed)
                )
                GridCell(
                    modifier = Modifier.weight(1f),
                    text = bon.date
                )
            }
        }
    }
}

@Composable
private fun GridHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun GridCell(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(0.5.dp, Color.LightGray)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
