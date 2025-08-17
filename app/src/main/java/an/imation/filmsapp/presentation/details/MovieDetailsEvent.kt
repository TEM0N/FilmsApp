package an.imation.filmsapp.presentation.details

sealed interface MovieDetailsEvent {
    data class ShowError(val message: Int) : MovieDetailsEvent
}