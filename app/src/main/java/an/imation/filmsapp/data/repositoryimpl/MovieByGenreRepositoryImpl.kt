package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.mapper.MovieDataMapper
import an.imation.filmsapp.data.mapper.toMovieExceptionDomainModel
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IMovieByGenreRepository
import android.util.Log

class MovieByGenreRepositoryImpl(
    private val api: ITmdbApi,
    private val mapper: MovieDataMapper
) : IMovieByGenreRepository {
    override suspend fun fetchMoviesByGenre(genreId: Int): TResult<List<MovieDomainModel>, MovieExceptionDomainModel> {
        return runCatching {
            val response = api.getMoviesByGenre(genreId)
            val movies = response.movies.mapNotNull { mapper.toDomain(it) }
            val invalidCurrent = response.movies.size - movies.size
            if (invalidCurrent > 0) {
                Log.e("!!!", "кол-во битых данных $invalidCurrent")
            }
            TResult.Success<List<MovieDomainModel>, MovieExceptionDomainModel>(movies)
        }.getOrElse { e ->
            TResult.Error<List<MovieDomainModel>, MovieExceptionDomainModel>(
                e.toMovieExceptionDomainModel()
            )
        }
    }
}