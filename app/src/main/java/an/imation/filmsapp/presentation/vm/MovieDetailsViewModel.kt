package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.data.mapper.toDomain
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.usecase.AddToFavoritesUseCase
import an.imation.filmsapp.domain.usecase.AddToWatchlistUseCase
import an.imation.filmsapp.domain.usecase.FetchMovieDetailsUseCase
import an.imation.filmsapp.domain.usecase.IsFavoriteUseCase
import an.imation.filmsapp.domain.usecase.IsInWatchlistUseCase
import an.imation.filmsapp.domain.usecase.RemoveFromFavoritesUseCase
import an.imation.filmsapp.domain.usecase.RemoveFromWatchlistUseCase
import an.imation.filmsapp.presentation.details.MovieDetailsEvent
import an.imation.filmsapp.presentation.details.MovieDetailsIntent
import an.imation.filmsapp.presentation.details.MovieDetailsState
import an.imation.filmsapp.presentation.parseToString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val fetchDetails: FetchMovieDetailsUseCase,
    private val addToFavorites: AddToFavoritesUseCase,
    private val removeFromFavorites: RemoveFromFavoritesUseCase,
    private val isFavorite: IsFavoriteUseCase,
    private val addToWatchlist: AddToWatchlistUseCase,
    private val removeFromWatchlist: RemoveFromWatchlistUseCase,
    private val isInWatchlist: IsInWatchlistUseCase,
    private val movieId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.asStateFlow()

    private val _events = SingleFlowEvent<MovieDetailsEvent>(viewModelScope)
    val events = _events.flow

    init {
        loadMovieDetails(movieId)

        viewModelScope.launch {
            isFavorite(movieId).collect { fav ->
                _state.update { it.copy(isFavorite = fav) }
            }
        }
        viewModelScope.launch {
            isInWatchlist(movieId).collect { wl ->
                _state.update { it.copy(isInWatchlist = wl) }
            }
        }
    }

    fun onIntent(intent: MovieDetailsIntent) {
        when (intent) {
            is MovieDetailsIntent.LoadDetails -> loadMovieDetails(intent.id)
            is MovieDetailsIntent.ToggleFavorite -> toggleFavorite(intent.movie)
            is MovieDetailsIntent.ToggleWatchlist -> toggleWatchlist(intent.movie)
        }
    }

    private fun toggleFavorite(movie: MovieDetailsDomainModel) {
        viewModelScope.launch {
            if (_state.value.isFavorite) {
                removeFromFavorites(movie.toDomain())
            } else {
                addToFavorites(movie.toDomain())
            }
        }
    }

    private fun toggleWatchlist(movie: MovieDetailsDomainModel) {
        viewModelScope.launch {
            if (_state.value.isInWatchlist) {
                removeFromWatchlist(movie.toDomain())
            } else {
                addToWatchlist(movie.toDomain())
            }
        }
    }
    private fun loadMovieDetails(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = fetchDetails(id)) {
                is TResult.Success -> _state.update {
                    it.copy(
                        isLoading = false,
                        details = result.data
                    )
                }

                is TResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.exception.parseToString()
                        )
                    }
                    _events.emit(MovieDetailsEvent.ShowError(result.exception.parseToString()))
                }
            }
        }
    }
}