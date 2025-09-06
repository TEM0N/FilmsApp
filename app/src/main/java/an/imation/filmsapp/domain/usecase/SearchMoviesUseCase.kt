package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.model.MoviePageDomainModel
import an.imation.filmsapp.domain.repository.IMovieRepository

class SearchMoviesUseCase(
    private val repository: IMovieRepository
) {
    suspend operator fun invoke(query: String, page: Int): TResult<MoviePageDomainModel, MovieExceptionDomainModel> {
        return repository.searchMovies(query, page)
    }
}
