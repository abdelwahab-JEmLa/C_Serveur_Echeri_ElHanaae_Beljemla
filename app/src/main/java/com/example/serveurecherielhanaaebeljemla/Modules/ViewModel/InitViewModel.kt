package com.example.serveurecherielhanaaebeljemla.Modules.ViewModel

import a_RoomDB.ArticlesBasesStatsTable
import a_RoomDB.CategoriesTabelle
import a_RoomDB.ClientsModel
import a_RoomDB.ColorsArticlesTabelle
import a_RoomDB.SoldArticlesTabelle
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serveurecherielhanaaebeljemla.Models.AppSettingsSaverModel
import com.example.serveurecherielhanaaebeljemla.Models.Res.ProductDisplayController
import com.example.serveurecherielhanaaebeljemla.Models.UiState
import com.example.serveurecherielhanaaebeljemla.Modules.Main.AppDatabase
import com.google.firebase.database.BuildConfig
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


open class InitViewModel(
    context: Context,
    private val database: AppDatabase,
) : ViewModel() {
    private val tag = "HeadViewModel"
    private val firestore = Firebase.firestore

    val _uiState = MutableStateFlow(
        UiState(
        productDisplayController = ProductDisplayController()
    )
    )
    open val uiState = _uiState.asStateFlow()


  //  ***
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val refAppSettingsSaverModel = firebaseDatabase.getReference("A_AppSettingsSaverModel") //-->
    //Hi Claud,what i went from u to do is to
    //Find All TODOs and Fix Them 

    //TODO:
    // groupe ces ref don une list 
    private val refDBJetPackExport = firebaseDatabase.getReference("e_DBJetPackExport")
    private val refCategorieModel = firebaseDatabase.getReference("H_CategorieTabele")
    private val refColorsArticles = firebaseDatabase.getReference("H_ColorsArticles")
    private val refSoldArticlesTabelle = firebaseDatabase.getReference("O_SoldArticlesTabelle")
    private val refClientsTabelle = firebaseDatabase.getReference("G_Clients")
    private val refDevicesTypeManager = firebaseDatabase.getReference("P_DevicesTypeManager")


    private fun updateLoadingProgress(progress: Float) {
        _uiState.update { it.copy(loadingProgress = progress) }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(
            isLoading = isLoading,
            loadingProgress = if (!isLoading) 0f else it.loadingProgress
        ) }
    }

    init {
        viewModelScope.launch {
            loadDataOfUiStateFromRoom()
        }
    }






    fun updateLongAppSetting(name: String, value: Long) {
        viewModelScope.launch {
            try {

                val existingSettings = _uiState.value.appSettingsSaverModel
                val maxId = existingSettings.maxOfOrNull { it.id } ?: 0

                val currentSetting = existingSettings.find { it.name == name }
                    ?.copy(
                        valueLong = value,
                        date = Date()
                    )
                    ?: AppSettingsSaverModel(
                        id = maxId + 1,
                        name = name,
                        valueLong = value,
                        date = Date()
                    )

                // Update local database
                database.appSettingsSaverModelDao().insert(currentSetting)

                // Update Firebase
                firebaseDatabase.getReference("A_AppSettingsSaverModel")
                    .child(currentSetting.id.toString())
                    .setValue(currentSetting)
                    .await()

                _uiState.update { state ->
                    state.copy(
                        appSettingsSaverModel = state.appSettingsSaverModel.map {
                            if (it.name == name) currentSetting else it
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**EXPO INTIA*/
    fun exportToWarningDataBaseBakup() {
        viewModelScope.launch {
            try {
                setLoading(true)


                // Get current timestamp for the backup
                val timestamp = System.currentTimeMillis()
                val backupRef = firebaseDatabase.getReference("WarningDataBaseBakup/$timestamp")

                // Create a backup object with all relevant data
                val backupData = hashMapOf(
                    "articlesBasesStatTables" to _uiState.value.articlesBasesStatTables,
                    "appSettingsSaverModel" to _uiState.value.appSettingsSaverModel,
                    "categories" to _uiState.value.categories,
                    "colorsArticlesTabelleModel" to _uiState.value.colorsArticlesTabelleModel,
                    "soldArticlesModel" to _uiState.value.soldArticlesModel,
                    "clientsModel" to _uiState.value.clientsModel,
                    "suppliers" to _uiState.value.suppliers,
                    "backupTimestamp" to timestamp,
                    "backupDate" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(timestamp))
                )

                // Update loading progress as we save each collection
                val totalCollections = backupData.size
                var currentCollection = 0

                backupData.forEach { (key, value) ->
                    try {
                        backupRef.child(key).setValue(value).await()
                        currentCollection++
                        // Adjust progress to start from 30% and go to 100%
                        updateLoadingProgress(0.3f + (0.7f * currentCollection.toFloat() / totalCollections))
                    } catch (e: Exception) {
                        _uiState.update { it.copy(error = "Failed to backup $key: ${e.message}") }
                    }
                }

                // Add metadata about the backup
                backupRef.child("metadata").setValue(
                    hashMapOf(
                        "totalCollections" to totalCollections,
                        "backupComplete" to true,
                        "deviceInfo" to Build.MODEL,
                        "appVersion" to BuildConfig.VERSION_NAME,
                        "totalArticlesExported" to _uiState.value.articlesBasesStatTables.size
                    )
                ).await()

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Backup failed: ${e.message}") }
            } finally {
                setLoading(false)
            }
        }
    }


    fun importFromFirebase() {
        viewModelScope.launch {
            try {
                setLoading(true)
                updateLoadingProgress(10f)


                appSettingsSaverModelInitialize(15f)


                updateLoadingProgress(20f)

                val categoriesSnapshot = refCategorieModel.get().await()
                updateLoadingProgress(40f)

                val categories = categoriesSnapshot.children.mapNotNull { snapshot ->
                    snapshot.getValue(CategoriesTabelle::class.java)
                }
                database.categoriesModelDao().insertAll(categories)

                updateLoadingProgress(70f)

                colorInitialize(80f)
                clientsInitialize(82f)
                soldArticlesTabelleIntia(85f)

                val articlesSnapshot = refDBJetPackExport.get().await()
                val articles = articlesSnapshot.children.mapNotNull { snapshot ->
                    snapshot.getValue(ArticlesBasesStatsTable::class.java)
                }
                database.articlesBasesStatsModelDao().insertAll(articles)
                updateLoadingProgress(100f)

                loadDataOfUiStateFromRoom()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun appSettingsSaverModelInitialize(fl: Float) {
        val appSettingsSaverModelSnapshot = refAppSettingsSaverModel.get().await()
        val appSettingsSaverModel = appSettingsSaverModelSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(AppSettingsSaverModel::class.java)
        }
        database.appSettingsSaverModelDao().insertAll(appSettingsSaverModel)
        updateLoadingProgress(fl)
    }
    private suspend fun clientsInitialize(fl: Float) {
        val clientsSnapshot = refClientsTabelle.get().await()
        val clients = clientsSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(ClientsModel::class.java)
        }
        database.clientsModelDao().insertAll(clients)
        updateLoadingProgress(fl)
    }
    private suspend fun colorInitialize(fl: Float) {
        // Import colors
        val colorsSnapshot = refColorsArticles.get().await()

        val colors = colorsSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(ColorsArticlesTabelle::class.java)
        }
        database.colorsArticlesDao().insertAll(colors)
        updateLoadingProgress(fl)
    }
    private suspend fun soldArticlesTabelleIntia(fl: Float) {

        val soldArticlesSnapshot = refSoldArticlesTabelle.get().await()

        val soldArticles = soldArticlesSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(SoldArticlesTabelle::class.java)
        }
        database.soldArticlesModelDao().insertAll(soldArticles)
        updateLoadingProgress(fl)
    }


    private suspend fun loadDataOfUiStateFromRoom() {
        try {
            setLoading(true)
            var progress = 0f

            while (progress < 100f) {
                progress += 10f
                updateLoadingProgress(progress)
                delay(100)
            }

            // Load all settings including client setting
            val settings = database.appSettingsSaverModelDao().getAll()
            if (!settings.any { it.name == "clientBuyerNowId" }) {
                database.appSettingsSaverModelDao().insert(
                    AppSettingsSaverModel(
                        id = System.currentTimeMillis(),
                        name = "clientBuyerNowId",
                        valueLong = 0
                    )
                )
            }

            val articles = database.articlesBasesStatsModelDao().getAll()
            val categories = database.categoriesModelDao().getAll()
            val colors = database.colorsArticlesDao().getAllOrdred()
            val soldArticles = database.soldArticlesModelDao().getAll()
            val clients = database.clientsModelDao().getAll()
            val devicesTypeManager = database.devicesTypeManagerDao().getAll()


            _uiState.update { it.copy(
                appSettingsSaverModel = database.appSettingsSaverModelDao().getAll(),
                articlesBasesStatTables = articles,
                categories = categories,
                colorsArticlesTabelleModel = colors,
                soldArticlesModel = soldArticles,
                clientsModel = clients,
                devicesTypeManager= devicesTypeManager,
            ) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        } finally {
            setLoading(false)
        }
    }

}
