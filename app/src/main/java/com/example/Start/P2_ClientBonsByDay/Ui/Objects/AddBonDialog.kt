package com.example.Start.P2_ClientBonsByDay.Ui.Objects
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayActions
import com.example.Start.P2_ClientBonsByDay.DaySoldBonsModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddBonDialog(
    onDismiss: () -> Unit,
    actions: ClientBonsByDayActions,
    daySoldBonsModel: List<DaySoldBonsModel>
) {
    var nameClient by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("0") }
    var payed by remember { mutableStateOf("0") }

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
                    value = total,
                    onValueChange = { total = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Total Amount") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = payed,
                    onValueChange = { payed = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Amount Paid") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nameClient.isNotBlank()) {
                        val newId = if (daySoldBonsModel.isEmpty()) 1L else daySoldBonsModel.maxOf { it.id } + 1
                        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .format(java.util.Date())

                        actions.onAddBon(
                            DaySoldBonsModel(
                                id = newId,
                                idClient = newId,
                                nameClient = nameClient,
                                total = total.toDoubleOrNull() ?: 0.0,
                                payed = payed.toDoubleOrNull() ?: 0.0,
                                date = currentDate
                            )
                        )
                        onDismiss()
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
