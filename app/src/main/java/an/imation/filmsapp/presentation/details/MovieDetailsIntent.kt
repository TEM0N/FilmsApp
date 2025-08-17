package an.imation.filmsapp.presentation.details

sealed interface MovieDetailsIntent {
    data class LoadDetails(val id: Int) : MovieDetailsIntent
}