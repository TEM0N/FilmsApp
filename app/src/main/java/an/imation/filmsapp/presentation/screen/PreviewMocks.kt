package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.domain.model.MovieDomainModel

object PreviewMocks {
    val emptyMovie = MovieDomainModel(
        id = 0,
        title = "",
        posterUrl = "",
        overview = "",
        rating = 0.0
    )
}