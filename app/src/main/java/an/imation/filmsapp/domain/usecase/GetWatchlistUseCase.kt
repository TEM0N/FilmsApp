package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.repository.IWatchlistRepository

class GetWatchlistUseCase(private val repo: IWatchlistRepository) {
    operator fun invoke() = repo.getAll()
}