package P1_StartupScreen.Main

import P0_MainScreen.Ui.Objects.LoadingOverlay
import P1_StartupScreen.Main.FloatingActionButtonGroup.FloatingActionButtonGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

import com.example.serveurecherielhanaaebeljemla.Models.UiState
import com.example.serveurecherielhanaaebeljemla.Modules.ViewModel.InitViewModel

@Composable
fun FragmentStartUpScreen(
    viewModel: InitViewModel,
    isFabVisible: Boolean,
) {
      val uiState by viewModel.uiState.collectAsState()

    MainUi(
        uiState = uiState,
        isFabVisible = isFabVisible,

    )
}

@Composable
fun MainUi(
    uiState: UiState,
    isFabVisible: Boolean,

    ) {


    Box(modifier = Modifier.fillMaxSize()) {


        AnimatedFabGroup(
            isFabVisible = isFabVisible,
                  )

        if (uiState.isLoading) {
            LoadingOverlay(
                progress = uiState.loadingProgress,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun AnimatedFabGroup(
    isFabVisible: Boolean,
  ) {
    Box(                               
        modifier = Modifier
            .padding(bottom = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = isFabVisible ,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 }
        ) {
            FloatingActionButtonGroup(
                modifier = Modifier.zIndex(1f),
                          )
        }
    }
}
