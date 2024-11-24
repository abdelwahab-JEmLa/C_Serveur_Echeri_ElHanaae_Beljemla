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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
@Composable
fun AutoResizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    maxLines: Int = Int.MAX_VALUE
) {
    var fontSize by remember(text) {
        mutableStateOf(style.fontSize)
    }

    var previousFontSize by remember {
        mutableStateOf(fontSize)
    }

    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.hasVisualOverflow) {
                previousFontSize = fontSize
                fontSize *= 0.9f
            } else if (fontSize != previousFontSize) {
                previousFontSize = fontSize
            }
        }
    )
}
@Composable
private fun GridCell(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .height(40.dp)
            .border(0.5.dp, Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        AutoResizedText(  text = text,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            )
    }
}
