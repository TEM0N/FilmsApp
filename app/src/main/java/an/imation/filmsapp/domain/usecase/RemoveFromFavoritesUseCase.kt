package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.repository.IFavoritesRepository

class RemoveFromFavoritesUseCase(private val repo: IFavoritesRepository) {
    suspend operator fun invoke(movie: MovieDomainModel) = repo.remove(movie)
}