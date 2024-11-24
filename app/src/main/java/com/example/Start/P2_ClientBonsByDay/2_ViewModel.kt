package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Modules.Main.ClientBonsByDayDao
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ClientBonsByDay Actions emitted from the UI Layer
 * passed to the coordinator to handle
 **/
data class ClientBonsByDayActions(
    val onClick: () -> Unit = {},
    val onAddBon: (ClientBonsByDay) -> Unit = {},
    val onDeleteBon: (ClientBonsByDay) -> Unit = {}
)

@Composable
fun rememberClientBonsByDayActions(viewModel: ClientBonsByDayViewModel): ClientBonsByDayActions {
    return remember(viewModel) {
        ClientBonsByDayActions(
            onClick = {},
            onAddBon = { bon -> viewModel.upsertBon(bon) },
            onDeleteBon = { bon -> viewModel.deleteBon(bon) }
        )
    }
}

@HiltViewModel
class ClientBonsByDayViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val clientBonsByDayDao: ClientBonsByDayDao
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(ClientBonsByDayState())
    val state: StateFlow<ClientBonsByDayState> = _stateFlow.asStateFlow()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val refClientBonsByDay = firebaseDatabase.getReference("1_ClientBonsByDay")

    fun upsertBon(bon: ClientBonsByDay) {
        viewModelScope.launch {
            try {
                clientBonsByDayDao.upsertBon(bon)
                //-->
                //Hi Claud,what i went from u to do is to
                //Find All TODOs and Fix Them 

                //TODO:
                // ajout un set au fire base

            } catch (e: Exception) {
                val errorMessage = e.message
                _stateFlow.update { it.copy(error = errorMessage) }
                savedStateHandle["error"] = errorMessage
            }
        }
    }


    fun deleteBon(bon: ClientBonsByDay) {
        viewModelScope.launch {
            try {
                clientBonsByDayDao.deleteBon(bon)
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = e.message) }
            }
        }
    }
    init {
        initializeData()
        //-->
        //Hi Claud,what i went from u to do is to
        //Find All TODOs and Fix Them 

        //TODO:
        // ajoute un on data change de refClientBonsByDay il upsert 
        // room
    }

    private fun initializeData() {
        viewModelScope.launch {
            try {
                clientBonsByDayDao.getAllBonsFlow()
                    .collect { bons ->
                        _stateFlow.value = ClientBonsByDayState(
                            clientBonsByDay = bons,
                            isLoading = false,
                            isInitialized = true
                        )
                    }
            } catch (e: Exception) {
                _stateFlow.value = ClientBonsByDayState(
                    isLoading = false,
                    error = e.message,
                    isInitialized = false
                )
            }
        }
    }
    fun retryInitialization() {
        _stateFlow.value = _stateFlow.value.copy(isLoading = true, error = null)
        initializeData()
    }
}
