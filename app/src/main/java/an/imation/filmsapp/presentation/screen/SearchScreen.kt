package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.R
import an.imation.filmsapp.presentation.search.SearchEvent
import an.imation.filmsapp.presentation.search.SearchIntent
import an.imation.filmsapp.presentation.search.SearchState
import an.imation.filmsapp.presentation.vm.SearchViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SearchScreen(navigator: DestinationsNavigator) {
    val vm = koinViewModel<SearchViewModel>()
    val state: SearchState by vm.uiState.collectAsStateWithLifecycle()
    val intent: (SearchIntent) -> Unit by remember { mutableStateOf(vm::onIntent) }
    val event: Flow<SearchEvent> by remember { mutableStateOf(vm.events) }
    val context = LocalContext.current

    UI(
        state = state,
        intent = intent,
        navigator = navigator
    )

    LaunchedEffect(Unit) {
        event.filterIsInstance<SearchEvent.ShowError>().collect {
            Toast.makeText(context, context.getString(it.message), Toast.LENGTH_SHORT).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun UI(
    state: SearchState = SearchState(),
    intent: (SearchIntent) -> Unit = {},
    navigator: DestinationsNavigator? = null
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.search)) },
            navigationIcon = {
                IconButton(onClick = { navigator?.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        OutlinedTextField(
            value = state.query,
            onValueChange = { intent(SearchIntent.UpdateQuery(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text(stringResource(R.string.enter)) },
            singleLine = true
        )

        when {
            state.isLoading && state.movies.isEmpty() -> LoadingBlock()
            state.error != null && state.movies.isEmpty() -> ErrorBlock(
                error = stringResource(id = state.error),
                intent = { intent(SearchIntent.UpdateQuery(state.query)) }
            )
            else -> MoviesGrid(
                movies = state.movies,
                intent = { intent(SearchIntent.LoadNextPage) },
                navigator = navigator
            )
        }
    }
}
