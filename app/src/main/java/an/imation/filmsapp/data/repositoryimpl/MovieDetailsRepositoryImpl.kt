package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.mapper.DetailsDataMapper
import an.imation.filmsapp.data.mapper.MovieDataMapper
import an.imation.filmsapp.data.mapper.toMovieExceptionDomainModel
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IMovieDetailsRepository

class MovieDetailsRepositoryImpl(
    private val api: ITmdbApi,
    private val mapper: DetailsDataMapper
) : IMovieDetailsRepository {
    override suspend fun fetchMovieDetails(movieId: Int): TResult<MovieDetailsDomainModel, MovieExceptionDomainModel> {
        return runCatching{
            val response = api.getMovieDetails(movieId)
            val details = mapper.toDomain(response)
            TResult.Success<MovieDetailsDomainModel, MovieExceptionDomainModel>(details)
        }.getOrElse { e ->
            TResult.Error<MovieDetailsDomainModel, MovieExceptionDomainModel>(
                e.toMovieExceptionDomainModel()
            )
        }
    }
}
