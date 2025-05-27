package an.imation.filmsapp.domain

interface IMovieRepository {
    suspend fun fetchPopularMovies(): TResult<List<MovieDomainModel>, MovieExceptionDomainModel>
}