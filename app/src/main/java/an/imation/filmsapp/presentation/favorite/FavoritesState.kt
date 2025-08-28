package an.imation.filmsapp.presentation.favorite

import an.imation.filmsapp.domain.model.MovieDomainModel

data class FavoritesState(
    val favorites: List<MovieDomainModel> = emptyList(),
    val watchlist: List<MovieDomainModel> = emptyList(),
    val selectedTab: Int = 0
)