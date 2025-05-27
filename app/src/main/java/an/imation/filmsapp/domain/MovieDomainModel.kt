package an.imation.filmsapp.domain

data class MovieDomainModel(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val overview: String,
    val rating: Double
)