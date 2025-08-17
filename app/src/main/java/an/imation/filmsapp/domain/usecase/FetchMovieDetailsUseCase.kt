package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.repository.IMovieDetailsRepository

class FetchMovieDetailsUseCase(
    private val repository: IMovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int) = repository.fetchMovieDetails(movieId)
}
