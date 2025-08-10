package an.imation.filmsapp.presentation.movie

sealed interface MovieEvent {
    data class ShowError(val message: Int) : MovieEvent
}