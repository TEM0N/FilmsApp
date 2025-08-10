package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.domain.usecase.FetchPopularMoviesUseCase
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.presentation.movie.MovieEvent
import an.imation.filmsapp.presentation.movie.MovieIntent
import an.imation.filmsapp.presentation.movie.MovieState
import an.imation.filmsapp.presentation.parseToString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieViewModel(
    private val fetchMovies: FetchPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieState())
    val uiState: StateFlow<MovieState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<MovieEvent>()
    val events: SharedFlow<MovieEvent> = _events

    init {
        loadMovies()
    }
    fun onIntent(intent: MovieIntent) {
        when (intent) {
            MovieIntent.LoadNextPage -> loadMovies()
        }
    }
    private fun loadMovies() {
        val state = _uiState.value
        if (state.isLoading || !state.isHaveNextPage) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            //_uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = fetchMovies(state.nextPage)) {
                is TResult.Success -> {
                    _uiState.update {
                        it.copy(
                            movies = (it.movies + result.data.movies).distinctBy { movie -> movie.id },
                            maxPage = result.data.maxPage,
                            nextPage = it.nextPage + 1,
                            lastPageSize = result.data.movies.size,
                            isLoading = false,
                            error = null
                        )
                    }
                }
                is TResult.Error -> {
                    _uiState.update{
                        it.copy(
                        isLoading = false,
                        error = result.exception.parseToString()
                        )
                    }
                    _events.emit(MovieEvent.ShowError(result.exception.parseToString()))
                }
            }
        }
    }
}
