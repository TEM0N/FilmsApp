package an.imation.filmsapp.domain.model

data class MoviePageDomainModel(
    val movies: List<MovieDomainModel>,
    val maxPage: Int
)