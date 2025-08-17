package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.usecase.FetchMovieDetailsUseCase
import an.imation.filmsapp.presentation.details.MovieDetailsEvent
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
    private val movieId: Int
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.asStateFlow()

    private val _events = SingleFlowEvent<MovieDetailsEvent>(viewModelScope)
    val events = _events.flow

    init {
        loadMovieDetails(movieId)
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