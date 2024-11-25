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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableGrid
import com.example.serveurecherielhanaaebeljemla.Models.BuyBonModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen
import java.time.LocalDate

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(7.dp)
    ) {
        // Statistics Card
        item {
            // Get the display statistics date from app settings
            val statisticsDate by remember(state.appSettingsSaverModel) {
                mutableStateOf(
                    state.appSettingsSaverModel
                        .firstOrNull()
                        ?.displayStatisticsDate
                        ?: LocalDate.now().toString()
                )
            }

            // Filter statistics based on selected date
            DaySoldStatisticsTabele(
                state = state,
                dateStatistics = statisticsDate
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Client Table with filtering
        item {
            val statisticsDate = state.appSettingsSaverModel
                .firstOrNull()
                ?.displayStatisticsDate
                ?: LocalDate.now().toString()

            // Filter client bons by date
            val filteredClientBons = state.daySoldBonsModel.filter {
                it.date == statisticsDate
            }

            ClientTable(filteredClientBons)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Buy Bon Table with filtering
        item {
            val statisticsDate = state.appSettingsSaverModel
                .firstOrNull()
                ?.displayStatisticsDate
                ?: LocalDate.now().toString()

            // Filter buy bons by date
            val filteredBuyBons = state.buyBonModel.filter {
                it.date == statisticsDate
            }

            BuyBonTable(filteredBuyBons)
        }
    }
}

@Composable
private fun BuyBonTable(state: List<BuyBonModel>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Achats des Bons",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        val columns = listOf(
            TableColumn<BuyBonModel>(
                title = "Fournisseur ID",
                weight = 0.5f
            ) { it.idSupplier.toString() },
            TableColumn<BuyBonModel>(
                title = "Fournisseur",
                weight = 1f
            ) { it.nameSupplier },
            TableColumn<BuyBonModel>(
                title = "Total",
                weight = 1f
            ) { "%.2f".format(it.total) },
        )

        TableGrid(
            items = state,
            columns = columns
        )
    }
}

// First, let's create a data class to hold our statistics
data class DayStatistics(
    val date: String,
    val totalSold: Double,
    val totalBuy: Double,
    val profit: Double
)

@Composable
private fun DaySoldStatisticsTabele(
    state: DaySoldBonsScreen,
    dateStatistics: String
) {
    // Calculate statistics for the given date
    val statistics = DayStatistics(
        date = dateStatistics,
        totalSold = state.daySoldBonsModel
            .filter { it.date == dateStatistics }
            .sumOf { it.total },
        totalBuy = state.buyBonModel
            .filter { it.date == dateStatistics }
            .sumOf { it.total },
        profit = state.daySoldBonsModel
            .filter { it.date == dateStatistics }
            .sumOf { it.total } -
                state.buyBonModel
                    .filter { it.date == dateStatistics }
                    .sumOf { it.total }
    )

    val columns = listOf(
        TableColumn<DayStatistics>(
            title = "Date",
            weight = 1f
        ) { it.date },
        TableColumn<DayStatistics>(
            title = "Total Sold du jour",
            weight = 1f
        ) { "%.2f".format(it.totalSold) },
        TableColumn<DayStatistics>(
            title = "T Buy",
            weight = 1f
        ) { "%.2f".format(it.totalBuy) },
        TableColumn<DayStatistics>(
            title = "Benifice",
            weight = 1f
        ) { "%.2f".format(it.profit) }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistiques journalières pour le $dateStatistics",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        TableGrid(
            items = listOf(statistics),  // Pass a list containing our single statistics object
            columns = columns
        )
    }
}
@Composable
 fun ClientTable(state: List<DaySoldBonsModel>) {
    val columns = listOf(
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
    )

    TableGrid(
        items = state,
        columns = columns
    )
}
