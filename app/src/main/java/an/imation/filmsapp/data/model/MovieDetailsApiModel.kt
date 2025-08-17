package an.imation.filmsapp.data.model

data class MovieDetailsApiModel(
    val id: String?,
    val title: String?,
    val original_title: String?,
    val poster_path: String?,
    val release_date: String?,
    val runtime: String?,
    val genres: List<GenreApiModel>?,
    val vote_average: String?,
    val overview: String?,
    val original_language: String?,
    val budget: String?,
    val revenue: String?,
    val status: String?,
    val production_companies: List<CompanyApiModel>?,
    val production_countries: List<CountryApiModel>?,
    val description: String?
)