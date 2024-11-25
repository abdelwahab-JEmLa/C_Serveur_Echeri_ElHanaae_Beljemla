package com.example.Start.P2_ClientBonsByDay.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun RowDateDefiner(
    modifier: Modifier = Modifier,
    uiState: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
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

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)  // Fixed height for the container
            .padding(horizontal = 16.dp)
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(listOf(
                "Statistics Date" to selectedStatsDate,
                "Entry Date" to selectedDate
            )) { (title, date) ->
                DateCard(
                    title = title,
                    date = date,
                    onDateClick = {
                        isStatsDateSelection = title == "Statistics Date"
                        if (isStatsDateSelection) {
                            showStatsDateDialog = true
                        } else {
                            showDateDialog = true
                        }
                    }
                )
            }
        }
    }

    // Dialog handling remains the same
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
private fun DateCard(
    title: String,
    date: LocalDate,
    onDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
            .width(250.dp)  // Fixed width for each card
            .height(100.dp), // Fixed height for each card
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()  // Fill the card's fixed size
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Date and Arabic day
                Text(
                    text = buildAnnotatedString {
                        append(date.format(DateTimeFormatter.ISO_DATE))
                        append("  ")
                        withStyle(MaterialTheme.typography.labelMedium.toSpanStyle()) {
                            append(arabicDayNames[date.dayOfWeek.toString()] ?: "")
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            IconButton(
                onClick = onDateClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select $title",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
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

@Preview(name = "RowDateDefiner")
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

        RowDateDefiner(
            uiState = previewState,
            actions = previewActions
        )
    }
}
