package an.imation.filmsapp.domain

class FetchPopularMoviesUseCase(
    private val repository: IMovieRepository
) {
    suspend operator fun invoke(): TResult<List<MovieDomainModel>, MovieExceptionDomainModel> {
        return repository.fetchPopularMovies()
    }
}