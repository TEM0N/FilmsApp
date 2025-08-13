package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel

interface IMovieByGenreRepository {
    suspend fun fetchMoviesByGenre(genreId: Int): TResult<List<MovieDomainModel>, MovieExceptionDomainModel>
}