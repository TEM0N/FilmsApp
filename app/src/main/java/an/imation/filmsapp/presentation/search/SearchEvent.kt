package an.imation.filmsapp.presentation.search

sealed interface SearchEvent {
    data class ShowError(val message: Int) : SearchEvent
}