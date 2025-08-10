package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.repository.IMovieRepository
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.model.MoviePageDomainModel

class FetchPopularMoviesUseCase(
    private val repository: IMovieRepository
) {
    suspend operator fun invoke(page: Int): TResult<MoviePageDomainModel, MovieExceptionDomainModel> {
        return repository.fetchPopularMovies(page)
    }
}