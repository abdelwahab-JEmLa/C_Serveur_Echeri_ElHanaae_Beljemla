package com.example.Start.P2_ClientBonsByDay

import P1_StartupScreen.Main.FloatingActionButtonGroup.FloatingActionButtonGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.Ui.BuyBonTable
import com.example.Start.P2_ClientBonsByDay.Ui.ClientTable
import com.example.Start.P2_ClientBonsByDay.Ui.RowDateDefiner
import com.example.Start.P2_ClientBonsByDay.Ui.DaySoldStatisticsTabele
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen
import java.time.LocalDate

@Composable
fun ClientBonsByDayScreen(
    state: DaySoldBonsScreen,
    actions: ClientBonsByDayActions
) {
    // Get the display statistics date from app settings
    val statisticsDate by remember(state.appSettingsSaverModel) {
        mutableStateOf(
            state.appSettingsSaverModel
                .firstOrNull()
                ?.displayStatisticsDate
                ?: LocalDate.now().toString()
        )
    }
    val filteredClientBons = state.daySoldBonsModel.filter {
        it.date == statisticsDate
    }
    val filteredBuyBons = state.buyBonModel.filter {
        it.date == statisticsDate
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
        ) {
            RowDateDefiner(
                Modifier,
                state,
                actions)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(7.dp)
            ) {
                // Statistics Card
                item {
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
                    ClientTable(filteredClientBons)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Buy Bon Table with filtering
                item {
                    BuyBonTable(filteredBuyBons)
                }
            }
        }

        // FAB and Dialog container
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp)
        ) {

            // Animated FAB group
            FloatingActionButtonGroup(
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
