package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.mapper.MovieDataMapper
import an.imation.filmsapp.data.mapper.toMovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IMovieRepository
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.MoviePageDomainModel
import android.util.Log

class MovieRepositoryImpl(
    private val api: ITmdbApi,
    private val mapper: MovieDataMapper
) : IMovieRepository {
    override suspend fun fetchPopularMovies(page: Int): TResult<MoviePageDomainModel, MovieExceptionDomainModel> {
        return runCatching {
            val response = api.getPopularMovies(page)
            val movies = response.movies.mapNotNull { mapper.toDomain(it) }
            val invalidCurrent = response.movies.size - movies.size
            if(invalidCurrent > 0){
                Log.e("!!!", "кол-во битых данных $invalidCurrent")
            }
            TResult.Success<MoviePageDomainModel, MovieExceptionDomainModel>(
                MoviePageDomainModel(
                    movies = movies,
                    maxPage = response.totalPages
                )
            )
        }.getOrElse { exception ->
            TResult.Error<MoviePageDomainModel, MovieExceptionDomainModel>(
                exception.toMovieExceptionDomainModel()
            )
        }
    }
}