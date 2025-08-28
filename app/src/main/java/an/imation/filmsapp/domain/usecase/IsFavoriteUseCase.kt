package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.repository.IFavoritesRepository

class IsFavoriteUseCase(private val repo: IFavoritesRepository) {
    operator fun invoke(id: Int) = repo.isFavorite(id)
}