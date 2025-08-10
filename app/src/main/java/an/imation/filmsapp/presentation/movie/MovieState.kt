package an.imation.filmsapp.presentation.movie

import an.imation.filmsapp.domain.model.MovieDomainModel

data class MovieState(
    val movies: List<MovieDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Int? = null,
    val nextPage: Int = 1,
    val maxPage: Int = 1,
    val lastPageSize: Int = 0
) {
    val isHaveNextPage: Boolean get() = nextPage <= maxPage
}