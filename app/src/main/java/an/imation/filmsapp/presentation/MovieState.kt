package an.imation.filmsapp.presentation

import an.imation.filmsapp.domain.MovieDomainModel

data class MovieState(
    val movies: List<MovieDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Int? = null
)