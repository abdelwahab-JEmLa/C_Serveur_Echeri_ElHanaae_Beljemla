package com.example.Start.P2_ClientBonsByDay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.StatistiquesSoldInDay
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableColumn
import com.example.Start.P2_ClientBonsByDay.Ui.U1_ClientTabel.TableGrid
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    Column {
        StatistiquesInDayCard(
            statistics = state.statistiquesSoldInDay
        )
        ClientTable(state)
    }
}
@Composable
fun StatistiquesInDayCard(
    statistics: List<StatistiquesSoldInDay>,
    modifier: Modifier = Modifier
) {
    val currentDate = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    // Find today's statistics
    val todayStats = statistics.find { it.dayDate == currentDate }

    // Calculate changes from previous day
    val yesterdayStats = statistics
        .find { it.dayDate == LocalDate.now().minusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }

    val totalChange = if (yesterdayStats != null && todayStats != null) {
        ((todayStats.totalInDay - yesterdayStats.totalInDay) / yesterdayStats.totalInDay * 100)
    } else 0.0

    val paymentChange = if (yesterdayStats != null && todayStats != null) {
        ((todayStats.payedInDay - yesterdayStats.payedInDay) / yesterdayStats.payedInDay * 100)
    } else 0.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Today's Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Total Sales Card
                StatisticItem(
                    title = "Total Sales",
                    value = todayStats?.totalInDay ?: 0.0,
                    percentageChange = totalChange,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Total Payments Card
                StatisticItem(
                    title = "Total Payments",
                    value = todayStats?.payedInDay ?: 0.0,
                    percentageChange = paymentChange,
                    modifier = Modifier.weight(1f)
                )
            }

            // Payment Progress Indicator
            if (todayStats != null && todayStats.totalInDay > 0) {
                val paymentProgress = (todayStats.payedInDay / todayStats.totalInDay)
                    .coerceIn(0.0, 1.0)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Payment Progress",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${(paymentProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    LinearProgressIndicator(
                        progress = paymentProgress.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticItem(
    title: String,
    value: Double,
    percentageChange: Double,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val color = if (percentageChange >= 0)
                    Color(0xFF4CAF50) else Color(0xFFE57373)

                Surface(
                    color = color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "${if (percentageChange >= 0) "+" else ""}%.1f%%".format(percentageChange),
                        color = color,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
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
        TableColumn(
            title = "Date",
            weight = 1f
        ) { it.date }
    )

    TableGrid(
        items = state.daySoldBonsModel,
        columns = columns
    )
}
