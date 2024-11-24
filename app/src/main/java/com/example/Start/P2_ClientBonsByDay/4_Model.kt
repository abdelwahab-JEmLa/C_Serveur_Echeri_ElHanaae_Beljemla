package com.example.Start.P2_ClientBonsByDay

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * UI State that represents ClientBonsByDayScreen
 **/
data class DaySoldBonsScreen(
    val daySoldBonsModel: List<DaySoldBonsModel> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isInitialized: Boolean = false
)

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



