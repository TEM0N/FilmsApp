package an.imation.filmsapp.presentation.genre

sealed interface GenreEvent {
    data class ShowError(val message: Int) : GenreEvent
}