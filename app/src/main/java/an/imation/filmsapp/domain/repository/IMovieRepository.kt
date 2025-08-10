package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.model.MoviePageDomainModel

interface IMovieRepository {
    suspend fun fetchPopularMovies(page: Int): TResult<MoviePageDomainModel, MovieExceptionDomainModel>
}