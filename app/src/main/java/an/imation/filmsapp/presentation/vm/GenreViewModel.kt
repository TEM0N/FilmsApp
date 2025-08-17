package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.usecase.FetchGenresUseCase
import an.imation.filmsapp.domain.usecase.FetchMoviesByGenreUseCase
import an.imation.filmsapp.presentation.genre.GenreEvent
import an.imation.filmsapp.presentation.genre.GenresState
import an.imation.filmsapp.presentation.parseToString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GenresViewModel(
    private val fetchGenres: FetchGenresUseCase,
    private val fetchMoviesByGenre: FetchMoviesByGenreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GenresState())
    val state: StateFlow<GenresState> = _state.asStateFlow()

    private val _event = SingleFlowEvent<GenreEvent>(viewModelScope)
    val event = _event.flow

    init {
        loadGenres()
    }

    private fun loadGenres() {
        if (_state.value.isLoading) return

        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (val result = fetchGenres()) {
                is TResult.Success -> {
                    _state.update { it.copy(genres = result.data, isLoading = false) }
                    loadMoviesForGenres(result.data)
                }
                is TResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.exception.parseToString()) }
                    _event.emit(GenreEvent.ShowError(result.exception.parseToString()))
                }
            }
        }
    }

    private fun loadMoviesForGenres(genres: List<GenreDomainModel>) {
        genres.forEach { genre ->
            viewModelScope.launch {
                when (val res = fetchMoviesByGenre(genre.id)) {
                    is TResult.Success -> _state.update { it.copy(
                        moviesByGenre = it.moviesByGenre + (genre.id to res.data)
                    ) }
                    is TResult.Error -> _event.emit(GenreEvent.ShowError(res.exception.parseToString()))
                }
            }
        }
    }
}
