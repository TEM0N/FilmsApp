package an.imation.filmsapp.domain.model

data class MovieDetailsDomainModel(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val posterUrl: String?,
    val releaseDate: String,
    val runtime: String,
    val genres: List<String>,
    val rating: String,
    val language: String,
    val budget: String,
    val revenue: String,
    val status: String,
    val productionCompanies: List<String>,
    val productionCountries: List<String>,
    val overview: String
)
