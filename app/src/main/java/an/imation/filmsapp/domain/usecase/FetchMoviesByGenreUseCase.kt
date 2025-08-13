package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IMovieByGenreRepository


class FetchMoviesByGenreUseCase(
    private val repository: IMovieByGenreRepository
) {
    suspend operator fun invoke(genreId: Int): TResult<List<MovieDomainModel>, MovieExceptionDomainModel> =
        repository.fetchMoviesByGenre(genreId)
}

