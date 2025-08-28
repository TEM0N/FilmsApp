package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.presentation.details.MovieDetailsEvent
import an.imation.filmsapp.presentation.details.MovieDetailsIntent
import an.imation.filmsapp.presentation.details.MovieDetailsState
import an.imation.filmsapp.presentation.theme.MyTypography
import an.imation.filmsapp.presentation.vm.MovieDetailsViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    navigator: DestinationsNavigator,
    vm: MovieDetailsViewModel = koinViewModel(parameters = { parametersOf(movieId) })
) {

    val state by vm.state.collectAsStateWithLifecycle()
    val event by remember { mutableStateOf(vm.events) }
    val context = LocalContext.current

    MovieDetailsUI(
        state = state,
        navigator = navigator,
        onToggleFavorite = { vm.onIntent(MovieDetailsIntent.ToggleFavorite(state.details)) },
        onToggleWatchlist = { vm.onIntent(MovieDetailsIntent.ToggleWatchlist(state.details)) }
    )

    LaunchedEffect(Unit) {
        event.filterIsInstance<MovieDetailsEvent.ShowError>().collect {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
@Preview
private fun MovieDetailsUI(
    state: MovieDetailsState = MovieDetailsState(),
    navigator: DestinationsNavigator? = null,
    onToggleFavorite: () -> Unit = {},
    onToggleWatchlist: () -> Unit = {}
) {
    when {
        state.isLoading -> LoadingBlock()
        state.error != null -> ErrorBlock(error = stringResource(id = state.error))
        else -> DetailsContent(
            navigator = navigator,
            details = state.details,
            isFavorite = state.isFavorite,
            isInWatchlist = state.isInWatchlist,
            onToggleFavorite = onToggleFavorite,
            onToggleWatchlist = onToggleWatchlist
        )    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun DetailsContent(
    navigator: DestinationsNavigator? = null,
    details: MovieDetailsDomainModel = PreviewMocks.emptyMovieDetails,
    isFavorite: Boolean = false,
    isInWatchlist: Boolean = false,
    onToggleFavorite: () -> Unit = {},
    onToggleWatchlist: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            //.padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.details))},
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
        details.posterUrl?.let { PosterBlock(it, details.title) }
        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                    modifier = Modifier.scale(if (isFavorite) 1.2f else 1f)
                )
            }
            IconButton(onClick = onToggleWatchlist) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                    contentDescription = "",
                    tint = if (isInWatchlist) Color.Red else Color.Gray,
                    modifier = Modifier.scale(if (isInWatchlist) 1.2f else 1f)
                )
            }
        }

        InfoBlock(stringResource(R.string.original_title), details.originalTitle)
        InfoBlock(stringResource(R.string.release_date), details.releaseDate)
        InfoBlock(stringResource(R.string.runtime), details.runtime)
        InfoBlock(stringResource(R.string.genres), details.genres.joinToString())
        InfoBlock(stringResource(R.string.rating), details.rating)
        InfoBlock(stringResource(R.string.language), details.language)
        InfoBlock(stringResource(R.string.budget), details.budget)
        InfoBlock(stringResource(R.string.revenue), details.revenue)
        InfoBlock(stringResource(R.string.status), details.status)
        InfoBlock(stringResource(R.string.production_companies), details.productionCompanies.joinToString())
        InfoBlock(stringResource(R.string.production_countries), details.productionCountries.joinToString())
        InfoBlock(stringResource(R.string.overview), details.overview)
    }
}

@Composable
@Preview
private fun PosterBlock(
    posterUrl: String = PreviewMocks.emptyMovieDetails.posterUrl.toString(),
    title: String = PreviewMocks.emptyMovieDetails.title
) {
    AsyncImage(
        model = posterUrl,
        contentDescription = title,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        contentScale = ContentScale.Fit
    )
}

@Composable
@Preview
private fun InfoBlock(
    label: String = "",
    value: String = ""
) {
    if (value.isNotBlank()) {
        Column(modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp)) {
            Text(
                text = label,
                style = MyTypography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            )
            Text(
                text = value,
                style = MyTypography.bodySmall
            )
        }
    }
}
