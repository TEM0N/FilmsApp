package an.imation.filmsapp.presentation.search

import an.imation.filmsapp.domain.model.MovieDomainModel

data class SearchState(
    val query: String = "",
    val movies: List<MovieDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Int? = null,
    val nextPage: Int = 1,
    val maxPage: Int = 1
) {
    val isHaveNextPage: Boolean get() = nextPage <= maxPage
}