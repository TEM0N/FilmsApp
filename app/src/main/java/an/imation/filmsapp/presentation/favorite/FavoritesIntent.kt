package an.imation.filmsapp.presentation.favorite

sealed interface FavoritesIntent {
    data class SelectTab(val index: Int): FavoritesIntent
}