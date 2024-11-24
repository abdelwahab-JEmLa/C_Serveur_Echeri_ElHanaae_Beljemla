package com.example.Start.P2_ClientBonsByDay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
            listOf("ID", "Client ID", "Client Name", "Total", "Payed", "Date").forEach { headerText ->
                GridHeader(
                    modifier = Modifier.weight(1f),
                    text = headerText
                )
            }
        }

        // Data rows
        state.daySoldBonsModel.forEach { bon ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    bon.id.toString(),
                    bon.idClient.toString(),
                    bon.nameClient,
                    "%.2f".format(bon.total),
                    "%.2f".format(bon.payed),
                    bon.date
                ).forEach { cellText ->
                    GridCell(
                        modifier = Modifier.weight(1f),
                        text = cellText
                    )
                }
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
