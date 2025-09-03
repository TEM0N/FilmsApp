package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IMovieDetailsRepository

class FetchMovieDetailsUseCase(
    private val repository: IMovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): TResult<MovieDetailsDomainModel, MovieExceptionDomainModel> = repository.fetchMovieDetails(movieId)
}
