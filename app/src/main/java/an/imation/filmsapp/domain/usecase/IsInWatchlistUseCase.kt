package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.repository.IWatchlistRepository


class IsInWatchlistUseCase(private val repo: IWatchlistRepository) {
    operator fun invoke(id: Int) = repo.isInWatchlist(id)
}