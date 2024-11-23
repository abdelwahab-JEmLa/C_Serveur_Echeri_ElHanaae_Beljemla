package P0_MainScreen.Ui.Main.AppNavHost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.EditRoad
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayDestination
import com.example.Start.P2_ClientBonsByDay.ClientBonsByDayRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.ClientBonsByDay.route,
            modifier = Modifier.fillMaxSize()
        ) {

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
        Screen.ClientBonsByDay
    )
}




