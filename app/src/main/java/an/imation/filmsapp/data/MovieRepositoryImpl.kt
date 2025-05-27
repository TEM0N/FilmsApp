package an.imation.filmsapp.data

import an.imation.filmsapp.domain.IMovieRepository
import an.imation.filmsapp.domain.MovieDomainModel
import an.imation.filmsapp.domain.MovieExceptionDomainModel
import an.imation.filmsapp.domain.TResult

class MovieRepositoryImpl(
    private val api: TmdbApi,
    private val mapper: MovieDataMapper
) : IMovieRepository {
    override suspend fun fetchPopularMovies(): TResult<List<MovieDomainModel>, MovieExceptionDomainModel> {
        return runCatching {
            val apiMovies = api.getPopularMovies()
            val movies = apiMovies.movies.mapNotNull { mapper.toDomain(it) }

            TResult.Success<List<MovieDomainModel>, MovieExceptionDomainModel>(movies)
        }.getOrElse { exception ->
            TResult.Error<List<MovieDomainModel>, MovieExceptionDomainModel>(
                exception.toMovieExceptionDomainModel()
            )
        }
    }
}