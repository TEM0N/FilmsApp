package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.domain.usecase.GetFavoritesUseCase
import an.imation.filmsapp.domain.usecase.GetWatchlistUseCase
import an.imation.filmsapp.presentation.favorite.FavoritesEvent
import an.imation.filmsapp.presentation.favorite.FavoritesIntent
import an.imation.filmsapp.presentation.favorite.FavoritesState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavorites: GetFavoritesUseCase,
    private val getWatchlist: GetWatchlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    private val _events = SingleFlowEvent<FavoritesEvent>(viewModelScope)
    val events = _events.flow
    init {
        viewModelScope.launch {
            getFavorites().collect { favs ->
                _state.update { it.copy(favorites = favs) }
            }
        }
        viewModelScope.launch {
            getWatchlist().collect { list ->
                _state.update { it.copy(watchlist = list) }
            }
        }
    }

    fun onIntent(intent: FavoritesIntent) {
        when(intent) {
            is FavoritesIntent.SelectTab -> _state.update { it.copy(selectedTab = intent.index) }
        }
    }
}
