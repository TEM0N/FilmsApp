package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.R
import an.imation.filmsapp.presentation.favorite.FavoritesEvent
import an.imation.filmsapp.presentation.favorite.FavoritesIntent
import an.imation.filmsapp.presentation.favorite.FavoritesState
import an.imation.filmsapp.presentation.movie.MovieEvent
import an.imation.filmsapp.presentation.vm.FavoritesViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel
@Destination
@Composable
fun FavoritesScreen(
    navigator: DestinationsNavigator,
    vm: FavoritesViewModel = koinViewModel()
) {
    val state: FavoritesState by vm.state.collectAsStateWithLifecycle()
    val intent: (FavoritesIntent) -> Unit by remember { mutableStateOf(vm::onIntent) }
    val event: Flow<FavoritesEvent> by remember { mutableStateOf(vm.events) }
    val context = LocalContext.current

    UI(
        state = state,
        intent = intent,
        navigator = navigator
    )

    LaunchedEffect(Unit) {
        event.filterIsInstance<MovieEvent.ShowError>().collect {
            Toast.makeText(
                context,
                it.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
@Preview
private fun UI(
    state: FavoritesState = FavoritesState(),
    intent: (FavoritesIntent) -> Unit = {},
    navigator: DestinationsNavigator? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        FavoritesHeader(
            navigator = navigator,
            selectedTab = state.selectedTab,
            onTabSelected = { tab -> intent(FavoritesIntent.SelectTab(tab)) }
        )

        when (state.selectedTab) {
            0 -> MoviesGrid(state.favorites, {}, navigator)
            1 -> MoviesGrid(state.watchlist, {}, navigator)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun FavoritesHeader(
    navigator: DestinationsNavigator? = null,
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    Column {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(onClick = { navigator?.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabSelected(0) }
            ) {
                Text(stringResource(R.string.favorite))
            }
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabSelected(1) }
            ) {
                Text(stringResource(R.string.want_see))
            }
        }
    }
}