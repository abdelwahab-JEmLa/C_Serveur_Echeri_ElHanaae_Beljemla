package com.example.Start.P2_ClientBonsByDay.clientbonsbyday

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ClientBonsByDayScreen(
    state: ClientBonsByDayState,
    actions: ClientBonsByDayActions
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Client Bons By Day",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(state.clientBonsByDay) { bon ->
                    ClientBonCard(
                        bon = bon,
                        onClick = { actions.onClick() }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Fixed FAB position - moved higher up
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 16.dp) // Increased bottom padding to move it higher
                .offset(y = (-16).dp) // Additional offset to move it even higher if needed
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Bon")
        }

        if (showDialog) {
            AddBonDialog(
                onDismiss = { showDialog = false },
                onAddBon = { newBon ->
                    actions.onAddBon(newBon)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun ClientBonCard(
    bon: ClientBonsByDay,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = bon.nameClient,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Total: ${if (bon.total) "Yes" else "No"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Payed: ${bon.payed}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(bon.date)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBonDialog(
    onDismiss: () -> Unit,
    onAddBon: (ClientBonsByDay) -> Unit
) {
    var nameClient by remember { mutableStateOf("") }
    var payed by remember { mutableStateOf("0") }
    var isTotal by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Bon") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nameClient,
                    onValueChange = { nameClient = it },
                    label = { Text("Client Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = payed,
                    onValueChange = { payed = it.filter { char -> char.isDigit() } },
                    label = { Text("Amount Paid") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = isTotal,
                        onCheckedChange = { isTotal = it }
                    )
                    Text("Is Total")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nameClient.isNotBlank()) {
                        onAddBon(
                            ClientBonsByDay(
                                nameClient = nameClient,
                                payed = payed.toLongOrNull() ?: 0,
                                total = isTotal,
                                date = Date()
                            )
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
