package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Modules.Main.ClientBonsByDayDao
import com.example.serveurecherielhanaaebeljemla.Modules.Main.StatistiquesSoldInDayDao
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Actions UI pour ClientBonsByDay
 */
data class ClientBonsByDayActions(
    val onClick: () -> Unit = {},
    val onAddBon: (DaySoldBonsModel) -> Unit = {},
    val onDeleteBon: (DaySoldBonsModel) -> Unit = {}
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
    private val clientBonsByDayDao: ClientBonsByDayDao,
    private val statistiquesSoldInDayDao: StatistiquesSoldInDayDao,
) : ViewModel() {

    // État UI
    private val _stateFlow = MutableStateFlow(DaySoldBonsScreen())
    val state: StateFlow<DaySoldBonsScreen> = _stateFlow.asStateFlow()

    // Firebase Setup
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val refDaySoldBons = firebaseDatabase.getReference("1_DaySoldBons")
    private val refStatistics = firebaseDatabase.getReference("1_StatistiquesSoldInDay")
    private var valueEventListener: ValueEventListener? = null
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        initializeData()
        setupFirebaseListener()
    }

    /**
     * Met à jour les statistiques quotidiennes
     */
    private suspend fun updateDailyStatistics(daySoldBons: List<DaySoldBonsModel>) {
        try {
            val today = LocalDate.now().format(dateFormatter)

            // Calcul des totaux pour aujourd'hui
            val todaysBons = daySoldBons.filter { it.date == today }
            val totalInDay = todaysBons.sumOf { it.total }
            val payedInDay = todaysBons.sumOf { it.payed }

            // Création ou mise à jour des statistiques
            val statistics = StatistiquesSoldInDay(
                dayDate = today,
                totalInDay = totalInDay,
                payedInDay = payedInDay
            )

            // Mise à jour base de données locale
            val existingStats = statistiquesSoldInDayDao.getStatisticsByDate(today)
            if (existingStats != null) {
                statistics.vid = existingStats.vid
            }
            statistiquesSoldInDayDao.upsert(statistics)

            // Mise à jour Firebase
            refStatistics.child(today).setValue(statistics)
        } catch (e: Exception) {
            _stateFlow.update { it.copy(error = "Erreur mise à jour statistiques: ${e.message}") }
        }
    }



    /**
     * Ajoute ou met à jour un bon
     */
    fun upsertBon(bon: DaySoldBonsModel) {
        viewModelScope.launch {
            try {
                // Mise à jour Room
                clientBonsByDayDao.upsertBon(bon)

                // Mise à jour Firebase
                refDaySoldBons.child(bon.id.toString()).setValue(bon)
                    .addOnSuccessListener {
                        _stateFlow.update { it.copy(error = null) }
                    }
                    .addOnFailureListener { e ->
                        val errorMessage = "Erreur mise à jour Firebase: ${e.message}"
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

    /**
     * Supprime un bon
     */
    fun deleteBon(bon: DaySoldBonsModel) {
        viewModelScope.launch {
            try {
                // Suppression Room
                clientBonsByDayDao.deleteBon(bon)

                // Suppression Firebase
                refDaySoldBons.child(bon.id.toString()).removeValue()
                    .addOnFailureListener { e ->
                        val errorMessage = "Erreur suppression Firebase: ${e.message}"
                        _stateFlow.update { it.copy(error = errorMessage) }
                    }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Initialise les données
     */
    private fun initializeData() {
        viewModelScope.launch {
            try {
                // Collecteurs séparés pour les bons et les statistiques
                launch {
                    clientBonsByDayDao.getAllBonsFlow().collect { bons ->
                        // Mise à jour des statistiques
                        updateDailyStatistics(bons)

                        // Mise à jour de l'état
                        _stateFlow.update { currentState ->
                            currentState.copy(
                                daySoldBonsModel = bons,
                                isLoading = false,
                                isInitialized = true
                            )
                        }
                    }
                }

                // Collecteur pour les statistiques
                launch {
                    statistiquesSoldInDayDao.getAllFlow().collect { statistics ->
                        _stateFlow.update { currentState ->
                            currentState.copy(
                                statistiquesSoldInDay = statistics,
                                isLoading = false,
                                isInitialized = true
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur initialisation: ${e.message}",
                        isInitialized = false
                    )
                }
            }
        }
    }

    /**
     * Configure l'écouteur Firebase
     */
    private fun setupFirebaseListener() {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    try {
                        // Récupération données locales
                        val localBons = clientBonsByDayDao.getAllBons()
                        val localBonsMap = localBons.associateBy { it.id }

                        // Traitement données Firebase
                        val firebaseBons = mutableListOf<DaySoldBonsModel>()
                        snapshot.children.forEach { childSnapshot ->
                            childSnapshot.getValue(DaySoldBonsModel::class.java)?.let { bon ->
                                firebaseBons.add(bon)
                            }
                        }

                        // Comparaison et synchronisation
                        val firebaseBonsMap = firebaseBons.associateBy { it.id }

                        // Gestion mises à jour et ajouts
                        firebaseBons.forEach { firebaseBon ->
                            val localBon = localBonsMap[firebaseBon.id]
                            if (localBon == null || localBon != firebaseBon) {
                                clientBonsByDayDao.upsertBon(firebaseBon)
                            }
                        }

                        // Gestion suppressions
                        localBons.forEach { localBon ->
                            if (!firebaseBonsMap.containsKey(localBon.id)) {
                                clientBonsByDayDao.deleteBon(localBon)
                            }
                        }

                    } catch (e: Exception) {
                        _stateFlow.update { it.copy(error = "Erreur sync Firebase: ${e.message}") }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _stateFlow.update { it.copy(error = "Sync Firebase annulée: ${error.message}") }
            }
        }

        valueEventListener?.let {
            refDaySoldBons.addValueEventListener(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Suppression de l'écouteur Firebase
        valueEventListener?.let {
            refDaySoldBons.removeEventListener(it)
        }
    }
}
