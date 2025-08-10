package an.imation.filmsapp.presentation.movie

sealed interface MovieIntent {
    //data object LoadMovies : MovieIntent
    data object LoadNextPage : MovieIntent

}