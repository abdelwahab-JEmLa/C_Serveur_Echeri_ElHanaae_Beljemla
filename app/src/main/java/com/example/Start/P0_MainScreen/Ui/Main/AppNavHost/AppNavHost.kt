package P0_MainScreen.Ui.Main.AppNavHost

import P1_StartupScreen.Main.FragmentStartUpScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.EditRoad
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.Start.P2_ClientBonsByDay.clientbonsbyday.ClientBonsByDay
import com.example.Start.P2_ClientBonsByDay.clientbonsbyday.ClientBonsByDayActions
import com.example.Start.P2_ClientBonsByDay.clientbonsbyday.ClientBonsByDayDestination
import com.example.Start.P2_ClientBonsByDay.clientbonsbyday.ClientBonsByDayRoute
import com.example.Start.P2_ClientBonsByDay.clientbonsbyday.ClientBonsByDayState
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isFabVisible: Boolean,
) {

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.EditDatabaseWithCreateNewArticles.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.EditDatabaseWithCreateNewArticles.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    FragmentStartUpScreen(
                        isFabVisible = isFabVisible,
                    )
                }
            }

            // Add the ClientBonsByDay route
            composable(ClientBonsByDayDestination().route) {
                ClientBonsByDayRoute()
            }
        }
    }
}
sealed class Screen(
    val route: String,
    val icon: ImageVector,
    val title: String,
    val color: Color
) {
    data object EditDatabaseWithCreateNewArticles : Screen(
        route = "main_fragment_edit_database_with_create_new_articles",
        icon = Icons.Default.EditRoad,
        title = "Create New Articles",
        color = Color(0xFFE30E0E)
    )
    data object ToggleFab : Screen(
        route = "toggle_fab",
        icon = Icons.Default.Visibility,
        title = "Toggle FAB",
        color = Color(0xFF2196F3)
    )
    data object ClientBonsByDay : Screen(
        route = "clientBonsByDay",
        icon = Icons.Default.CreditScore,
        title = "Client Bons By Day",
        color = Color(0xFF2196F3)
    )
}

// Update NavigationItems to include the new screen
object NavigationItems {
    fun getItems() = listOf(
        Screen.EditDatabaseWithCreateNewArticles,
        Screen.ClientBonsByDay
    )
}




