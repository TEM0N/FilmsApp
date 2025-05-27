package an.imation.filmsapp.presentation

sealed interface MovieEvent {
    data class ShowError(val message: Int) : MovieEvent
}