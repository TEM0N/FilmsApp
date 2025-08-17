package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel

interface IMovieDetailsRepository {
    suspend fun fetchMovieDetails(movieId: Int): TResult<MovieDetailsDomainModel, MovieExceptionDomainModel>
}
