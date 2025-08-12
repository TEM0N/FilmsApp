package an.imation.filmsapp.presentation.genre

import an.imation.filmsapp.domain.model.GenreDomainModel

data class GenresState(
    val genres: List<GenreDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: Int? = null
)