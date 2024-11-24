package com.example.Start.P2_ClientBonsByDay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableGrid

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
        // Statistics Card
        DaySoldStatisticsTabele(state.daySoldStatistics)

        Spacer(modifier = Modifier.height(24.dp))

        // Client Table
        ClientTable(state)
    }
}

@Composable
private fun DaySoldStatisticsTabele(state: List<DaySoldStatistics>) {
    val columns = listOf(
        TableColumn<DaySoldStatistics>(  // Added type parameter
            title = "Date",
            weight = 1f
        ) { it.dayDate },
        TableColumn<DaySoldStatistics>(  // Added type parameter
            title = "Total du jour",
            weight = 1f
        ) { "%.2f".format(it.totalInDay) },
        TableColumn<DaySoldStatistics>(  // Added type parameter
            title = "Payé du jour",
            weight = 1f
        ) { "%.2f".format(it.payedInDay) },
        TableColumn<DaySoldStatistics>(  // Added type parameter
            title = "Reste à payer",
            weight = 1f
        ) { "%.2f".format(it.totalInDay - it.payedInDay) }
    )

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistiques journalières",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TableGrid(
            items = state,
            columns = columns
        )
    }
}

@Composable
private fun ClientTable(state: DaySoldBonsScreen) {
    val columns = listOf(
        TableColumn<DaySoldBonsModel>(
            title = "ID",
            weight = 0.5f
        ) { it.id.toString() },
        TableColumn<DaySoldBonsModel>(  // Added type parameter
            title = "Client ID",
            weight = 0.5f
        ) { it.idClient.toString() },
        TableColumn<DaySoldBonsModel>(  // Added type parameter
            title = "Client Name",
            weight = 1f
        ) { it.nameClient },
        TableColumn<DaySoldBonsModel>(  // Added type parameter
            title = "Total",
            weight = 1f
        ) { "%.2f".format(it.total) },
        TableColumn<DaySoldBonsModel>(  // Added type parameter
            title = "Payed",
            weight = 1f
        ) { "%.2f".format(it.payed) },
        TableColumn<DaySoldBonsModel>(  // Added type parameter
            title = "Date",
            weight = 1f
        ) { it.date }
    )

    TableGrid(
        items = state.daySoldBonsModel,
        columns = columns
    )
}
