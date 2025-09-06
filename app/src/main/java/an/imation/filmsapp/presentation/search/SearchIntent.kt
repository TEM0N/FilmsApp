package an.imation.filmsapp.presentation.search

sealed interface SearchIntent {
    data class UpdateQuery(val query: String) : SearchIntent
    data object LoadNextPage : SearchIntent
}