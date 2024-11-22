package com.example.serveurecherielhanaaebeljemla.Models

import a_RoomDB.ArticlesBasesStatsTable
import a_RoomDB.CategoriesTabelle
import a_RoomDB.ClientsModel
import a_RoomDB.ColorsArticlesTabelle
import a_RoomDB.SoldArticlesTabelle
import a_RoomDB.SuppliersTabelle
import com.example.serveurecherielhanaaebeljemla.Models.Res.DevicesTypeManager
import com.example.serveurecherielhanaaebeljemla.Models.Res.ProductDisplayController

data class UiState(
    val appSettingsSaverModel: List<AppSettingsSaverModel> = emptyList(),
    val devicesTypeManager: List<DevicesTypeManager> = emptyList(),

    val articlesBasesStatTables: List<ArticlesBasesStatsTable> = emptyList(),
    val categories: List<CategoriesTabelle> = emptyList(),
    val colorsArticlesTabelleModel: List<ColorsArticlesTabelle> = emptyList(),
    val soldArticlesModel: List<SoldArticlesTabelle?> = emptyList(),
    val clientsModel: List<ClientsModel> = emptyList(),
    val suppliers: List<SuppliersTabelle> = emptyList(),
    val productDisplayController: ProductDisplayController,
    val maxPriceMap: Map<Pair<Long, Long>, List<PriceRecord>> = emptyMap(),
    val isLoading: Boolean = false,
    val loadingProgress: Float = 0f,
    val error: String? = null
)
// Update the price mapping to include client ID
data class PriceRecord(
    val price: Double,
    val clientId: Long,
    val date: Long
)
