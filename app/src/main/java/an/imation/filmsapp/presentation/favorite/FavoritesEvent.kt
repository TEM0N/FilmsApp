package an.imation.filmsapp.presentation.favorite

sealed interface FavoritesEvent {
    data class ShowError(val message: Int) : FavoritesEvent
}