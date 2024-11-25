package com.example.Start.P2_ClientBonsByDay.Ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.Objects.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.Objects.TableGrid
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen

@Composable
fun DaySoldStatisticsTabele(
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
// First, let's create a data class to hold our statistics
private data class DayStatistics(
    val date: String,
    val totalSold: Double,
    val totalBuy: Double,
    val profit: Double
)
