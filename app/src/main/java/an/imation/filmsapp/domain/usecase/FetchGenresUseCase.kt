package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IGenreRepository

class FetchGenresUseCase(
    private val repository: IGenreRepository
) {
    suspend operator fun invoke(): TResult<List<GenreDomainModel>, MovieExceptionDomainModel> {
        return repository.fetchGenres()
    }
}