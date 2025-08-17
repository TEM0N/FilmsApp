package an.imation.filmsapp.presentation.details

import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.presentation.screen.PreviewMocks

data class MovieDetailsState(
    val isLoading: Boolean = false,
    val details: MovieDetailsDomainModel = PreviewMocks.emptyMovieDetails,
    val error: Int? = null
)