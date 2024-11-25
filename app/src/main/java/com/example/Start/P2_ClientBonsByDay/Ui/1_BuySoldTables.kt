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
import com.example.serveurecherielhanaaebeljemla.Models.BuyBonModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsModel

@Composable
fun BuyBonTable(state: List<BuyBonModel>) {
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
