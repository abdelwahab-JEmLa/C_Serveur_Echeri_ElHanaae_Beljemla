package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Modules.Main.ClientBonsByDayDao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
                // Update local Room database
                clientBonsByDayDao.upsertBon(bon)

                // Update Firebase
                refClientBonsByDay.child(bon.id.toString()).setValue(bon)
                    .addOnSuccessListener {
                        // Firebase update successful
                        _stateFlow.update { it.copy(error = null) }
                    }
                    .addOnFailureListener { e ->
                        // Handle Firebase update failure
                        val errorMessage = "Firebase update failed: ${e.message}"
                        _stateFlow.update { it.copy(error = errorMessage) }
                        savedStateHandle["error"] = errorMessage
                    }

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
                // Delete from local Room database
                clientBonsByDayDao.deleteBon(bon)

                // Delete from Firebase
                refClientBonsByDay.child(bon.id.toString()).removeValue()
                    .addOnFailureListener { e ->
                        val errorMessage = "Firebase deletion failed: ${e.message}"
                        _stateFlow.update { it.copy(error = errorMessage) }
                    }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = e.message) }
            }
        }
    }

    init {
        initializeData()
        setupFirebaseListener()
    }

    private fun setupFirebaseListener() {
        refClientBonsByDay.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    try {
                        snapshot.children.forEach { childSnapshot ->
                            childSnapshot.getValue(ClientBonsByDay::class.java)?.let { bon ->
                                // Update Room database with Firebase data
                                clientBonsByDayDao.upsertBon(bon)
                            }
                        }
                    } catch (e: Exception) {
                        _stateFlow.update { it.copy(error = "Firebase sync error: ${e.message}") }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _stateFlow.update { it.copy(error = "Firebase sync cancelled: ${error.message}") }
            }
        })
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


    override fun onCleared() {
        super.onCleared()
        // Remove Firebase listener when ViewModel is cleared
        refClientBonsByDay.removeEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
