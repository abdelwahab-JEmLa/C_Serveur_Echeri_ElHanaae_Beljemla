package com.example.serveurecherielhanaaebeljemla

import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.Start.P0_MainScreen.Main.MainScreen
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayViewModel
import com.example.serveurecherielhanaaebeljemla.Modules.Main.AppDatabase
import com.example.serveurecherielhanaaebeljemla.Modules.Main.DatabaseRepository
import com.example.serveurecherielhanaaebeljemla.Modules.Main.PermissionHandler
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

data class AppViewModels(
    val clientBonsByDayViewModel: ClientBonsByDayViewModel
)

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var databaseRepository: DatabaseRepository  // Added DatabaseRepository

    private fun initializeFirebase() {
        try {
            FirebaseApp.initializeApp(this)
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize Firebase: ${e.message}")
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            // Database and Repository are automatically initialized by Hilt
            initializeFirebase()
            // Accessing the repository silences the "never used" warnings
            databaseRepository.getDaos()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize application: ${e.message}")
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionHandler by lazy { PermissionHandler(this) }

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var databaseRepository: DatabaseRepository  // Added DatabaseRepository

    private val clientBonsByDayViewModel: ClientBonsByDayViewModel by viewModels()

    private val appViewModels by lazy {
        AppViewModels(
            clientBonsByDayViewModel = clientBonsByDayViewModel
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // Accessing the repository silences the "never used" warnings
            databaseRepository.getDaos()

            permissionHandler.checkAndRequestPermissions(object : PermissionHandler.PermissionCallback {
                override fun onPermissionsGranted() {
                    setContent {
                        MainScreen(appViewModels)
                    }
                }

                override fun onPermissionsDenied() {
                    finish()
                }

                override fun onPermissionRationale(permissions: Array<String>) {
                    // Show rationale if needed
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }
    }
}
