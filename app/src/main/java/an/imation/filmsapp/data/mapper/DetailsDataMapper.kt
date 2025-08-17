package an.imation.filmsapp.data.mapper

import an.imation.filmsapp.data.model.MovieDetailsApiModel
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel

class DetailsDataMapper {

    fun toDomain(apiModel: MovieDetailsApiModel): MovieDetailsDomainModel = runCatching {
        MovieDetailsDomainModel(
            id = apiModel.id?.toInt()!!,
            title = apiModel.title.orEmpty(),
            originalTitle = apiModel.original_title.orEmpty(),
            posterUrl = apiModel.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" },
            releaseDate = apiModel.release_date.orEmpty(),
            runtime = apiModel.runtime?.let { "$it min" } ?: "Unknown",
            genres = apiModel.genres?.mapNotNull { it.name } ?: emptyList(),
            rating = apiModel.vote_average?.let { String.format("%.1f", it.toDoubleOrNull() ?: 0.0) } ?: "N/A",
            language = apiModel.original_language.orEmpty(),
            budget = apiModel.budget?.toLongOrNull()?.let { "$${"%,d".format(it)}" } ?: "N/A",
            revenue = apiModel.revenue?.toLongOrNull()?.let { "$${"%,d".format(it)}" } ?: "N/A",
            status = apiModel.status.orEmpty(),
            productionCompanies = apiModel.production_companies?.mapNotNull { it.name } ?: emptyList(),
            productionCountries = apiModel.production_countries?.mapNotNull { it.name } ?: emptyList(),
            overview = apiModel.overview.orEmpty()
        )
    }.getOrElse {
        MovieDetailsDomainModel(
            id = 0,
            title = "",
            originalTitle = "",
            posterUrl = null,
            releaseDate = "",
            runtime = "Unknown",
            genres = emptyList(),
            rating = "N/A",
            language = "",
            budget = "N/A",
            revenue = "N/A",
            status = "",
            productionCompanies = emptyList(),
            productionCountries = emptyList(),
            overview = ""
        )
    }
}
