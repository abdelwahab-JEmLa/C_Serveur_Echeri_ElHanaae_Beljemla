package P0_MainScreen.Ui.Main.AppNavHost

import P0_MainScreen.Ui.Objects.LoadingOverlay
import P1_StartupScreen.Main.FragmentStartUpScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.serveurecherielhanaaebeljemla.AppViewModels

@Composable
fun AppNavHost(
    appViewModels: AppViewModels,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isFabVisible: Boolean,

) {
    val uiState by appViewModels.initViewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.EditDatabaseWithCreateNewArticles.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.EditDatabaseWithCreateNewArticles.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FragmentStartUpScreen(

                        viewModel = appViewModels.initViewModel,

                        isFabVisible = isFabVisible,
                       )

                    if (uiState.isLoading) {
                        LoadingOverlay(
                            progress = uiState.loadingProgress / 100f,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
            }

        }
    }
}
