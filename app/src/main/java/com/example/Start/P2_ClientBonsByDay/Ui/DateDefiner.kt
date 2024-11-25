package com.example.Start.P2_ClientBonsByDay.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.tooling.preview.Preview
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayActions
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen

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
    // Initialize date from uiState.appSettingsSaverModel
    var selectedDate by remember(uiState.appSettingsSaverModel) {
        val savedDate = uiState.appSettingsSaverModel
            .firstOrNull()
            ?.dateForNewEntries
            ?.let { LocalDate.parse(it) }
            ?: LocalDate.now()
        mutableStateOf(savedDate)
    }
    var showDateDialog by remember { mutableStateOf(false) }


    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                    append("Date: ${selectedDate.format(DateTimeFormatter.ISO_DATE)}")
                    append("  ")
                    withStyle(MaterialTheme.typography.labelMedium.toSpanStyle()) {
                        append(arabicDayNames[selectedDate.dayOfWeek.toString()] ?: "")
                    }
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { showDateDialog = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        }
    }

    if (showDateDialog) {
        DateSelectionDialog(
            onDismiss = { showDateDialog = false },
            onDateSelected = { date ->
                selectedDate = date
                actions.onDateSelected(date.toString()) 
                showDateDialog = false
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
