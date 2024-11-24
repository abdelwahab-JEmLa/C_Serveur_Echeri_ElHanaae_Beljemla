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
import com.example.Start.P2_ClientBonsByDay.Ui.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.TableGrid

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    val columns = listOf(
        TableColumn(
            title = "ID",
            weight = 0.5f
        ) { it.id.toString() },
        TableColumn(
            title = "Client ID",
            weight = 0.5f
        ) { it.idClient.toString() },
        TableColumn(
            title = "Client Name",
            weight = 1f
        ) { it.nameClient },
        TableColumn(
            title = "Total",
            weight = 1f
        ) { "%.2f".format(it.total) },
        TableColumn(
            title = "Payed",
            weight = 1f
        ) { "%.2f".format(it.payed) },
        TableColumn<DaySoldBonsModel>(
            title = "Date",
            weight = 1f
        ) { it.date }
    )

    TableGrid(
        items = state.daySoldBonsModel,
        columns = columns
    )
}

