package an.imation.filmsapp.presentation.details

import an.imation.filmsapp.domain.model.MovieDetailsDomainModel

sealed interface MovieDetailsIntent {
    data class LoadDetails(val id: Int) : MovieDetailsIntent
    data class ToggleFavorite(val movie: MovieDetailsDomainModel) : MovieDetailsIntent
    data class ToggleWatchlist(val movie: MovieDetailsDomainModel) : MovieDetailsIntent
}