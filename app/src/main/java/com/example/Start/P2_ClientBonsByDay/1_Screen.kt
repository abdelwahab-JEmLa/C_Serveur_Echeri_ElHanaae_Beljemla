package com.example.Start.P2_ClientBonsByDay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableGrid
import java.time.LocalDate

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Statistics Card
        item {
            val today = LocalDate.now().toString()
            val todayStatistics = state.daySoldStatistics.filter { it.dayDate == today }
            DaySoldStatisticsTabele(todayStatistics)
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Client Table
        item {
            ClientTable(state)
        }
    }
}

@Composable
private fun DaySoldStatisticsTabele(state: List<DaySoldStatistics>) {
    val columns = listOf(
        TableColumn<DaySoldStatistics>(
            title = "Date",
            weight = 1f
        ) { it.dayDate },
        TableColumn<DaySoldStatistics>(
            title = "Total du jour",
            weight = 1f
        ) { "%.2f".format(it.totalInDay) },
        TableColumn<DaySoldStatistics>(
            title = "Payé du jour",
            weight = 1f
        ) { "%.2f".format(it.payedInDay) },
        TableColumn<DaySoldStatistics>(
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
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (state.isEmpty()) {
            Text(
                text = "Aucune statistique disponible pour aujourd'hui",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            TableGrid(
                items = state,
                columns = columns
            )
        }
    }
}

@Composable
private fun ClientTable(state: DaySoldBonsScreen) {
    val columns = listOf(
        TableColumn<DaySoldBonsModel>(
            title = "ID",
            weight = 0.5f
        ) { it.id.toString() },
        TableColumn<DaySoldBonsModel>(
            title = "Client ID",
            weight = 0.5f
        ) { it.idClient.toString() },
        TableColumn<DaySoldBonsModel>(
            title = "Client Name",
            weight = 1f
        ) { it.nameClient },
        TableColumn<DaySoldBonsModel>(
            title = "Total",
            weight = 1f
        ) { "%.2f".format(it.total) },
        TableColumn<DaySoldBonsModel>(
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
