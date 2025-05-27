package an.imation.filmsapp.presentation
import an.imation.filmsapp.domain.MovieDomainModel
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel
@Composable
fun MovieScreen() {
    val vm = koinViewModel<MovieViewModel>()
    val state by vm.uiState.collectAsStateWithLifecycle()
    val event: SharedFlow<MovieEvent> by remember { mutableStateOf(vm.events) }
    val context = LocalContext.current
    MovieUI(
        state = state
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
private fun MovieUI(
    state: MovieState = MovieState(),
    intent: (MovieIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 32.dp)
    ) {
        when {
            state.isLoading -> LoadingBlock()
            state.error != null -> ErrorBlock(error = stringResource(id = state.error), intent)
            else -> MoviesGrid(state.movies, intent)
        }
    }
}

@Composable
@Preview
private fun LoadingBlock() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview
private fun ErrorBlock(
    error: String = "",
    intent: (MovieIntent) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(error, color = MaterialTheme.colorScheme.error)

    }
}

@Composable
@Preview
private fun MoviesGrid(
    movies: List<MovieDomainModel> = listOf(
        MovieDomainModel(
            id = 1,
            title = "",
            posterUrl = "",
            overview = "",
            rating = 1.0
        ),
        MovieDomainModel(
            id = 2,
            title = "",
            posterUrl = "",
            overview = "",
            rating = 2.0
        )
    ),
    intent: (MovieIntent) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie)
        }
    }
}

@Composable
@Preview
private fun MovieCard(
    movie: MovieDomainModel = MovieDomainModel(id = 1, title = "", posterUrl = "", overview = "", rating = 1.0
    )
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(0.57f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.75f),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    movie.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "★ ${movie.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}