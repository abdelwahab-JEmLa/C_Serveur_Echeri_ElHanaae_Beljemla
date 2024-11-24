package com.example.Start.P2_ClientBonsByDay

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * UI State that represents ClientBonsByDayScreen
 **/
data class ClientBonsByDayState(
    val clientBonsByDay: List<ClientBonsByDay> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isInitialized: Boolean = false
)

@Entity(tableName = "client_bons_by_day")
data class ClientBonsByDay(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val idClient: Long = 0,
    val nameClient: String = "",
    val total: Boolean = false,
    val payed: Long = 0,
    val date: Date = Date()
) {
    // No-argument constructor for Firebase
    constructor() : this(0)
}



