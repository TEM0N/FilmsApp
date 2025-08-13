package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.presentation.genre.GenreEvent
import an.imation.filmsapp.presentation.genre.GenreIntent
import an.imation.filmsapp.presentation.genre.GenresState
import an.imation.filmsapp.presentation.vm.GenresViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
fun GenresScreen(navigator: DestinationsNavigator) {
    val vm = koinViewModel<GenresViewModel>()
    val state: GenresState by vm.state.collectAsStateWithLifecycle()
    val event: Flow<GenreEvent> by remember { mutableStateOf(vm.event) }
    val context = LocalContext.current

    GenresUI(
        state = state
    )

    LaunchedEffect(Unit) {
        event.filterIsInstance<GenreEvent.ShowError>().collect {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun GenresUI(
    state: GenresState = GenresState(),
    intent: (GenreIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.category)) }
        )

        when {
            state.isLoading -> LoadingBlock()
            state.error != null -> ErrorBlock(error = stringResource(id = state.error))
            else -> GenresListBlock(genres = state.genres, moviesByGenre = state.moviesByGenre)
        }
    }
}
@Composable
@Preview
private fun GenresListBlock(
    genres: List<GenreDomainModel> = emptyList(),
    moviesByGenre: Map<Int, List<MovieDomainModel>> = emptyMap()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(genres) { genre ->
            Text(
                text = genre.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            val movies = moviesByGenre[genre.id] ?: emptyList()
            if (movies.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies) { movie ->
                        MovieCard(movie)
                    }
                }
            }
            HorizontalDivider()
        }
    }
}
