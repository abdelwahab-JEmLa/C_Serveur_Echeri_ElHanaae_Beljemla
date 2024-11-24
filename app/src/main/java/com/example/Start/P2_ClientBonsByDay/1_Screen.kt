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
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) { //-->
                //Hi Claud,what i went from u to do is to
                //Find All TODOs and Fix Them

                //TODO:
                // utlise une loop ddfait que le heigt 40
                GridHeader(modifier = Modifier.weight(1f), text = "ID")
                GridHeader(modifier = Modifier.weight(1f), text = "Client ID")
                GridHeader(modifier = Modifier.weight(1f), text = "Client Name")
                GridHeader(modifier = Modifier.weight(1f), text = "Total")
                GridHeader(modifier = Modifier.weight(1f), text = "Payed")
                GridHeader(modifier = Modifier.weight(1f), text = "Date")
            }

            // Data rows
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(state.daySoldBonsModel) { bon ->//-->
                    //Hi Claud,what i went from u to do is to
                    //Find All TODOs and Fix Them

                    //TODO:
                    // pk il saffichon comme l image
                    GridCell(text = bon.id.toString())
                    GridCell(text = bon.idClient.toString())
                    GridCell(text = bon.nameClient)
                    GridCell(text = "%.2f".format(bon.total))
                    GridCell(text = "%.2f".format(bon.payed))
                    GridCell(text = bon.date)
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
            .padding(horizontal = 2.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun GridCell(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, Color.LightGray)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

