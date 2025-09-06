package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.usecase.SearchMoviesUseCase
import an.imation.filmsapp.presentation.parseToString
import an.imation.filmsapp.presentation.search.SearchEvent
import an.imation.filmsapp.presentation.search.SearchIntent
import an.imation.filmsapp.presentation.search.SearchState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchMovies: SearchMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchState())
    val uiState: StateFlow<SearchState> = _uiState.asStateFlow()

    private val _events = SingleFlowEvent<SearchEvent>(viewModelScope)
    val events = _events.flow

    fun onIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateQuery -> {
                _uiState.update { it.copy(query = intent.query, nextPage = 1, movies = emptyList()) }
                loadMovies(reset = true)
            }
            SearchIntent.LoadNextPage -> loadMovies()
        }
    }

    private fun loadMovies(reset: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || state.query.isBlank() || !state.isHaveNextPage) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (val result = searchMovies(state.query, state.nextPage)) {
                is TResult.Success -> {
                    _uiState.update {
                        it.copy(
                            movies = if (reset) result.data.movies else it.movies + result.data.movies,
                            maxPage = result.data.maxPage,
                            nextPage = it.nextPage + 1,
                            isLoading = false
                        )
                    }
                }
                is TResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.exception.parseToString()) }
                    _events.emit(SearchEvent.ShowError(result.exception.parseToString()))
                }
            }
        }
    }
}
