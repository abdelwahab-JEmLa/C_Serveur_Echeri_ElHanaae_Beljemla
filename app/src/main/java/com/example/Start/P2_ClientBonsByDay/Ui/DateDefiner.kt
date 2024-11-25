package com.example.Start.P2_ClientBonsByDay.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayActions
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val arabicDayNames = mapOf(
    "SATURDAY" to "السبت",
    "SUNDAY" to "الأحد",
    "MONDAY" to "الاثنين",
    "TUESDAY" to "الثلاثاء",
    "WEDNESDAY" to "الأربعاء",
    "THURSDAY" to "الخميس",
    "FRIDAY" to "الجمعة"
)

@Composable
fun DateDefiner(
    modifier: Modifier = Modifier,
    uiState: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    // Safely initialize dates with proper null handling
    var selectedDate by remember(uiState.appSettingsSaverModel) {
        val savedDate = try {
            uiState.appSettingsSaverModel
                .firstOrNull()
                ?.dateForNewEntries
                ?.takeIf { it.isNotBlank() }
                ?.let { LocalDate.parse(it) }
        } catch (e: Exception) {
            null
        }
        mutableStateOf(savedDate ?: LocalDate.now())
    }

    var selectedStatsDate by remember(uiState.appSettingsSaverModel) {
        val savedStatsDate = try {
            uiState.appSettingsSaverModel
                .firstOrNull()
                ?.displayStatisticsDate
                ?.takeIf { it.isNotBlank() }
                ?.let { LocalDate.parse(it) }
        } catch (e: Exception) {
            null
        }
        mutableStateOf(savedStatsDate ?: LocalDate.now())
    }

    var showDateDialog by remember { mutableStateOf(false) }
    var showStatsDateDialog by remember { mutableStateOf(false) }
    var isStatsDateSelection by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Entry Date Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Entry Date: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}")
                        append("  ")
                        withStyle(MaterialTheme.typography.labelMedium.toSpanStyle()) {
                            append(arabicDayNames[selectedDate.dayOfWeek.toString()] ?: "")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    isStatsDateSelection = false
                    showDateDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Entry Date"
                    )
                }
            }
        }

        // Statistics Date Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Statistics Date: ${selectedStatsDate.format(DateTimeFormatter.ISO_DATE)}")
                        append("  ")
                        withStyle(MaterialTheme.typography.labelMedium.toSpanStyle()) {
                            append(arabicDayNames[selectedStatsDate.dayOfWeek.toString()] ?: "")
                        }
                    },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    isStatsDateSelection = true
                    showStatsDateDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Statistics Date"
                    )
                }
            }
        }
    }

    if (showDateDialog || showStatsDateDialog) {
        DateSelectionDialog(
            onDismiss = {
                showDateDialog = false
                showStatsDateDialog = false
            },
            onDateSelected = { date ->
                if (isStatsDateSelection) {
                    selectedStatsDate = date
                    actions.onStatisticsDateSelected(date.toString())
                    showStatsDateDialog = false
                } else {
                    selectedDate = date
                    actions.onDateSelected(date.toString())
                    showDateDialog = false
                }
            }
        )
    }
}


@Composable
private fun DateSelectionDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()

    // Generate list of last 10 days
    val dates = remember {
        (0..9).map { today.minusDays(it.toLong()) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dates) { date ->
                        FilledTonalButton(
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append(when(date) {
                                        today -> "Today"
                                        today.minusDays(1) -> "Yesterday"
                                        today.minusDays(2) -> "Day before yesterday"
                                        else -> date.format(DateTimeFormatter.ISO_DATE)
                                    })
                                    append(" (${date.format(DateTimeFormatter.ISO_DATE)}) ")
                                    append(arabicDayNames[date.dayOfWeek.toString()] ?: "")
                                }
                            )
                        }
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview(name = "DateDefiner")
@Composable
private fun PreviewDateDefiner() {
    MaterialTheme {
        // Create preview data
        val previewState = DaySoldBonsScreen(
            appSettingsSaverModel = listOf(
                AppSettingsSaverModel(
                    id = 1,
                    dateForNewEntries = LocalDate.now().toString()
                )
            )
        )
        val previewActions = ClientBonsByDayActions()

        DateDefiner(
            uiState = previewState,
            actions = previewActions
        )
    }
}
