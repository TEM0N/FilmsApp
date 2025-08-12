package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel

interface IGenreRepository {
    suspend fun fetchGenres(): TResult<List<GenreDomainModel>, MovieExceptionDomainModel>
}