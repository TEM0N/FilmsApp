package an.imation.filmsapp.presentation

import an.imation.filmsapp.domain.FetchPopularMoviesUseCase
import an.imation.filmsapp.domain.TResult
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

    private fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = fetchMovies()) {
                is TResult.Success -> {
                    _uiState.update {
                        it.copy(
                        movies = result.data,
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
