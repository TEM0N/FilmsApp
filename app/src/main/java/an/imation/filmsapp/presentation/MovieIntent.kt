package an.imation.filmsapp.presentation

sealed interface MovieIntent {
    data object LoadMovies : MovieIntent
}