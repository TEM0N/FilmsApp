package an.imation.filmsapp.presentation.screen
import an.imation.filmsapp.R
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.presentation.vm.MovieViewModel
import an.imation.filmsapp.presentation.movie.MovieEvent
import an.imation.filmsapp.presentation.movie.MovieIntent
import an.imation.filmsapp.presentation.movie.MovieState
import an.imation.filmsapp.presentation.theme.MyTypography
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
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
        state = state,
        intent = vm::onIntent
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
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = stringResource(R.string.category)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.favorite)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        }
        when {
            state.movies.isEmpty() && state.isLoading -> LoadingBlock()
            state.movies.isEmpty() && state.error != null -> ErrorBlock(error = stringResource(id = state.error), intent)
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
        Text(error, color = colorResource(id = R.color.red))
    }
}

@Composable
@Preview
private fun MoviesGrid(
    movies: List<MovieDomainModel> = listOf(PreviewMocks.emptyMovie),
    intent: (MovieIntent) -> Unit = {},
    visibleThreshold: Int = 10
) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        state = listState,
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(movies, key = { _, movie -> movie.id }) { index, movie ->
            if (index == movies.size - visibleThreshold) {
                //Log.d("MoviesGrid", "Request LoadNextPage: index = $index, movies.size = ${movies.size}")
                intent(MovieIntent.LoadNextPage)
            }
            MovieCard(movie)
        }

    }
}

@Composable
@Preview
private fun MovieCard(
    movie: MovieDomainModel = PreviewMocks.emptyMovie
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
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    //.weight(0.25f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    movie.title,
                    style = MyTypography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "★ ${movie.rating}",
                    style = MyTypography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}