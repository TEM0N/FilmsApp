package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.model.MovieDomainModel

object PreviewMocks {
    val emptyMovie = MovieDomainModel(
        id = 0,
        title = "",
        posterUrl = "",
        overview = "",
        rating = 0.0
    )

    val emptyMovieDetails = MovieDetailsDomainModel(
        id = 0,
        title = "",
        originalTitle = "",
        posterUrl = "",
        overview = "",
        rating = "",
        language = "",
        releaseDate = "",
        runtime = "",
        genres = listOf("", "", ""),
        budget = "",
        revenue = "",
        status = "",
        productionCompanies = listOf("", ""),
        productionCountries = listOf("", "")
    )
}