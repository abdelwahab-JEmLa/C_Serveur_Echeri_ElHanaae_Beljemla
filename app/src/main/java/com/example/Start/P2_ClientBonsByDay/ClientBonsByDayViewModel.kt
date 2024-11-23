package com.example.Start.P2_ClientBonsByDay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Modules.Main.ClientBonsByDayDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientBonsByDayViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val clientBonsByDayDao: ClientBonsByDayDao
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(ClientBonsByDayState())
    val state: StateFlow<ClientBonsByDayState> = _stateFlow.asStateFlow()

    init {
        initializeData()
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

    fun addBon(bon: ClientBonsByDay) {
        viewModelScope.launch {
            try {
                clientBonsByDayDao.insertBon(bon)
            } catch (e: Exception) {
                val errorMessage = e.message
                _stateFlow.update { it.copy(error = errorMessage) }
                savedStateHandle["error"] = errorMessage
            }
        }
    }

    fun updateBon(bon: ClientBonsByDay) {
        viewModelScope.launch {
            try {
                clientBonsByDayDao.updateBon(bon)
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = e.message) }
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
}
