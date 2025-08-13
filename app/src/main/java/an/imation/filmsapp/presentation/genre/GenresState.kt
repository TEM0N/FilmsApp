package an.imation.filmsapp.presentation.genre

import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.model.MovieDomainModel

data class GenresState(
    val genres: List<GenreDomainModel> = emptyList(),
    val moviesByGenre: Map<Int, List<MovieDomainModel>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: Int? = null
)