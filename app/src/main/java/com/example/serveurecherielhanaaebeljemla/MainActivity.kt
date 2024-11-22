package com.example.serveurecherielhanaaebeljemla

import com.example.Start.P0_MainScreen.Main.MainScreen
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serveurecherielhanaaebeljemla.Modules.Main.AppDatabase
import com.example.serveurecherielhanaaebeljemla.Modules.Main.PermissionHandler
import com.example.serveurecherielhanaaebeljemla.Modules.ViewModel.InitViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MyApplication : Application() {
    lateinit var database: AppDatabase
        private set

    private fun initializeDatabase() {
        try {
            database = AppDatabase.DatabaseModule.getDatabase(this)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to initialize database: ${e.message}")
        }
    }

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
            initializeDatabase()
            initializeFirebase()
        } catch (e: Exception) {
            // Log the error and possibly crash the app if these are critical
            throw IllegalStateException("Failed to initialize application: ${e.message}")
        }
    }
}

data class AppViewModels(
    val initViewModel: InitViewModel,
)

// ViewModelFactory.kt
class ViewModelFactory(
    private val context: Context,
    private val database: AppDatabase,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(InitViewModel::class.java) ->
                InitViewModel(
                    context.applicationContext,
                    database,
                ) as T

            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

class MainActivity : ComponentActivity() {
    private val database by lazy {
        (application as? MyApplication)?.database
            ?: throw IllegalStateException("Application must be MyApplication")
    }

    private val permissionHandler by lazy { PermissionHandler(this) }

    private val viewModelFactory by lazy {
        try {
            ViewModelFactory(applicationContext, database)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to create ViewModelFactory: ${e.message}")
        }
    }

    private val initViewModel: InitViewModel by viewModels { viewModelFactory }

    private val appViewModels by lazy {
        AppViewModels(
            initViewModel = initViewModel,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            permissionHandler.checkAndRequestPermissions(object : PermissionHandler.PermissionCallback {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionsGranted() {
                    setContent {
                        MainScreen(appViewModels)
                    }
                }

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionsDenied() {
                    // Handle denied permissions
                    finish() // or show an error message
                }

                override fun onPermissionRationale(permissions: Array<String>) {
                    // Show rationale if needed
                }
            })
        } catch (e: Exception) {
            // Handle any initialization errors
            e.printStackTrace()
            finish()
        }
    }
}
