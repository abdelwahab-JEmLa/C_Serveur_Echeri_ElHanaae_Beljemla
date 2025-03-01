package com.example.serveurecherielhanaaebeljemla.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UI State that represents ClientBonsByDayScreen
 **/
data class DaySoldBonsScreen(
    val daySoldBonsModel: List<DaySoldBonsModel> = emptyList(),
    val buyBonModel: List<BuyBonModel> = emptyList(),
    val appSettingsSaverModel: List<AppSettingsSaverModel> = emptyList(),

    val isLoading: Boolean = true,
    val error: String? = null,
    val isInitialized: Boolean = false
)

// AppSettingsSaverModel.kt
@Entity
data class AppSettingsSaverModel(
    @PrimaryKey var id: Long = 0,
    val name: String = "",
    val dateForNewEntries: String = "", //yyyy-mm-dd
    val displayStatisticsDate: String = "", //yyyy-mm-dd
) {
    // No-argument constructor for Firebase
    constructor() : this(0)
}


@Entity
data class DaySoldBonsModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val idClient: Long = 0,
    val nameClient: String = "",
    val total: Double = 0.0,
    val payed: Double = 0.0,
    val date: String = "",
) {
    // No-argument constructor for Firebase
    constructor() : this(0)
}
@Entity
data class BuyBonModel(
    @PrimaryKey(autoGenerate = true)
    var vid: Long = 0,
    val date: String = "",
    val idSupplier: Long = 0,
    val nameSupplier: String = "",
    val total: Double = 0.0,
    val payed: Double = 0.0,
) {
    // No-argument constructor for Firebase
    constructor() : this(0)
}


