package com.example.Start.P2_ClientBonsByDay

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.BuyBonModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsModel
import com.example.serveurecherielhanaaebeljemla.Models.DaySoldBonsScreen
import com.example.serveurecherielhanaaebeljemla.Modules.Main.AppSettingsSaverModelDao
import com.example.serveurecherielhanaaebeljemla.Modules.Main.BuyBonModelDao
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
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Actions UI pour ClientBonsByDay
 */
// In ClientBonsByDayActions:
data class ClientBonsByDayActions(
    val onClick: () -> Unit = {},
    val onDateSelected: (String) -> Unit = {},
    val onStatisticsDateSelected: (String) -> Unit = {}
)

@Composable
fun rememberClientBonsByDayActions(viewModel: ClientBonsByDayViewModel): ClientBonsByDayActions {
    return remember(viewModel) {
        ClientBonsByDayActions(
            onClick = {},
            onDateSelected = { viewModel.updateAppSettingsDate(it) },
            onStatisticsDateSelected = { viewModel.updateStatisticsDate(it) }
        )
    }
}


@HiltViewModel
class ClientBonsByDayViewModel @Inject constructor(
    private val clientBonsByDayDao: ClientBonsByDayDao,
    private val buyBonModelDao: BuyBonModelDao,
    private val appSettingsSaverModelDao: AppSettingsSaverModelDao,

    ) : ViewModel() {

    // État UI
    private val _stateFlow = MutableStateFlow(DaySoldBonsScreen())
    val state: StateFlow<DaySoldBonsScreen> = _stateFlow.asStateFlow()

    // Firebase Setup
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val refDaySoldBons = firebaseDatabase.getReference("1_DaySoldBons")
    private val refDaySoldStatistics = firebaseDatabase.getReference("1B_DaySoldStatistics")
    private val refBuyBon = firebaseDatabase.getReference("1C_BuyBon")
    private val refAppSettingsSaverModel = firebaseDatabase.getReference("2_AppSettingsSaverNew")

    private var daySoldBonsListener: ValueEventListener? = null
    private var buyBonListener: ValueEventListener? = null
    private var appSettingsSaverModelListener: ValueEventListener? = null

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    // In ClientBonsByDayViewModel, add this new function:
    fun updateStatisticsDate(date: String) {
        viewModelScope.launch {
            try {
                // Get current settings
                val currentSettings = appSettingsSaverModelDao.getAll().firstOrNull()

                // Create new or update existing settings
                val updatedSettings = currentSettings?.copy(
                    displayStatisticsDate = date
                ) ?: AppSettingsSaverModel(
                    id = 1,
                    displayStatisticsDate = date
                )

                // Update local database
                appSettingsSaverModelDao.upsert(updatedSettings)

                // Update Firebase
                refAppSettingsSaverModel.child(updatedSettings.id.toString()).setValue(updatedSettings)

                // Update UI state
                _stateFlow.update { currentState ->
                    currentState.copy(
                        appSettingsSaverModel = listOf(updatedSettings)
                    )
                }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = "Error updating statistics date: ${e.message}") }
            }
        }
    }
    fun updateAppSettingsDate(date: String) {
        viewModelScope.launch {
            try {
                // Get current settings
                val currentSettings = appSettingsSaverModelDao.getAll().firstOrNull()

                // Create new or update existing settings
                val updatedSettings = currentSettings?.copy(
                    dateForNewEntries = date
                ) ?: AppSettingsSaverModel(
                    id = 1,
                    dateForNewEntries = date
                )

                // Update local database
                appSettingsSaverModelDao.upsert(updatedSettings)

                // Update Firebase
                refAppSettingsSaverModel.child(updatedSettings.id.toString()).setValue(updatedSettings)

                // Update UI state
                _stateFlow.update { currentState ->
                    currentState.copy(
                        appSettingsSaverModel = listOf(updatedSettings)
                    )
                }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(error = "Error updating date: ${e.message}") }
            }
        }
    }

    /**
     * Configure l'écouteur Firebase
     */
    init {
        initializeData()
        setupFirebaseListeners()
    }
    /**
     * Initialise les données
     */
    private fun initializeData() {
        viewModelScope.launch {
            try {
                // Collecteur AppSettingsSaverModel
                launch {
                    appSettingsSaverModelDao.getAllFlow().collect { appSettingsSaverModel ->
                        _stateFlow.update { currentState ->
                            currentState.copy(
                                appSettingsSaverModel = appSettingsSaverModel,
                                isLoading = false,
                                isInitialized = true
                            )
                        }
                    }
                }
                // Collecteurs séparés pour les bons et les statistiques
                launch {
                    clientBonsByDayDao.getAllBonsFlow().collect { bons ->

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
                    buyBonModelDao.getAllFlow().collect { buyBonModel ->
                        _stateFlow.update { currentState ->
                            currentState.copy(
                                buyBonModel = buyBonModel,
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

    private fun setupFirebaseListeners() {
        setupDaySoldBonsListener()
        setupBuyBonListener()
        setupAppSettingsSaverModel()

    }
    private fun setupAppSettingsSaverModel() {
        appSettingsSaverModelListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    try {
                        // Get local data
                        val localDatas = appSettingsSaverModelDao.getAll()
                        val localDatasMap = localDatas.associateBy { it.id }

                        // Process Firebase data
                        val firebaseData = mutableListOf<AppSettingsSaverModel>()
                        snapshot.children.forEach { childSnapshot ->
                            childSnapshot.getValue(AppSettingsSaverModel::class.java)?.let {
                                    firebaseData.add(it)
                            }
                        }

                        // Compare and synchronize
                        val firebaseBuyBonsMap = firebaseData.associateBy { it.id }

                        // Handle updates and additions
                        firebaseData.forEach { 
                            val localData = localDatasMap[it.id]
                            if (localData == null || localData != it) {
                                appSettingsSaverModelDao.upsert(it)
                            }
                        }

                        // Handle deletions
                        localDatas.forEach { 
                            if (!firebaseBuyBonsMap.containsKey(it.id)) {
                                appSettingsSaverModelDao.delete(it)
                            }
                        }

                    } catch (e: Exception) {
                        _stateFlow.update { it.copy(error = "Erreur sync Firebase BuyBon: ${e.message}") }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _stateFlow.update { it.copy(error = "Sync Firebase BuyBon annulée: ${error.message}") }
            }
        }

        buyBonListener?.let {
            refBuyBon.addValueEventListener(it)
        }
    }

    private fun setupDaySoldBonsListener() {
        daySoldBonsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    try {
                        val localBons = clientBonsByDayDao.getAllBons()
                        val localBonsMap = localBons.associateBy { it.id }

                        val firebaseBons = mutableListOf<DaySoldBonsModel>()
                        snapshot.children.forEach { childSnapshot ->
                            childSnapshot.getValue(DaySoldBonsModel::class.java)?.let { bon ->
                                firebaseBons.add(bon)
                            }
                        }

                        val firebaseBonsMap = firebaseBons.associateBy { it.id }

                        // Handle updates and additions
                        firebaseBons.forEach { firebaseBon ->
                            val localBon = localBonsMap[firebaseBon.id]
                            if (localBon == null || localBon != firebaseBon) {
                                clientBonsByDayDao.upsertBon(firebaseBon)
                            }
                        }

                        // Handle deletions
                        localBons.forEach { localBon ->
                            if (!firebaseBonsMap.containsKey(localBon.id)) {
                                clientBonsByDayDao.deleteBon(localBon)
                            }
                        }
                    } catch (e: Exception) {
                        _stateFlow.update { it.copy(error = "Erreur sync Firebase DaySoldBons: ${e.message}") }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _stateFlow.update { it.copy(error = "Sync Firebase DaySoldBons annulée: ${error.message}") }
            }
        }

        daySoldBonsListener?.let {
            refDaySoldBons.addValueEventListener(it)
        }
    }

    private fun setupBuyBonListener() {
        buyBonListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    try {
                        // Get local data
                        val localBuyBons = buyBonModelDao.getAll()
                        val localBuyBonsMap = localBuyBons.associateBy { it.vid }

                        // Process Firebase data
                        val firebaseBuyBons = mutableListOf<BuyBonModel>()
                        snapshot.children.forEach { childSnapshot ->
                            childSnapshot.getValue(BuyBonModel::class.java)?.let { buyBon ->
                                firebaseBuyBons.add(buyBon)
                            }
                        }

                        // Compare and synchronize
                        val firebaseBuyBonsMap = firebaseBuyBons.associateBy { it.vid }

                        // Handle updates and additions
                        firebaseBuyBons.forEach { firebaseBuyBon ->
                            val localBuyBon = localBuyBonsMap[firebaseBuyBon.vid]
                            if (localBuyBon == null || localBuyBon != firebaseBuyBon) {
                                buyBonModelDao.upsert(firebaseBuyBon)
                            }
                        }

                        // Handle deletions
                        localBuyBons.forEach { localBuyBon ->
                            if (!firebaseBuyBonsMap.containsKey(localBuyBon.vid)) {
                                buyBonModelDao.delete(localBuyBon)
                            }
                        }

                    } catch (e: Exception) {
                        _stateFlow.update { it.copy(error = "Erreur sync Firebase BuyBon: ${e.message}") }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _stateFlow.update { it.copy(error = "Sync Firebase BuyBon annulée: ${error.message}") }
            }
        }

        buyBonListener?.let {
            refBuyBon.addValueEventListener(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove both Firebase listeners
        daySoldBonsListener?.let {
            refDaySoldBons.removeEventListener(it)
        }
        buyBonListener?.let {
            refBuyBon.removeEventListener(it)
        }
    }
}
