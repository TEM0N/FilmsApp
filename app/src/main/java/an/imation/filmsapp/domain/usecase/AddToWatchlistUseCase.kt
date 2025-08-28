package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.repository.IWatchlistRepository

class AddToWatchlistUseCase(private val repo: IWatchlistRepository) {
    suspend operator fun invoke(movie: MovieDomainModel) = repo.add(movie)
}