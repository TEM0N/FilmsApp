package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.mapper.GenreDataMapper
import an.imation.filmsapp.data.mapper.toMovieExceptionDomainModel
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.GenreDomainModel
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.IGenreRepository
import android.util.Log

class GenreRepositoryImpl(
    private val api: ITmdbApi,
    private val mapper: GenreDataMapper
) : IGenreRepository {
    override suspend fun fetchGenres(): TResult<List<GenreDomainModel>, MovieExceptionDomainModel> {
        return runCatching {
            val response = api.getGenres()
            val genres = response.genres.mapNotNull { mapper.toDomain(it) }
            val invalidCurrent = response.genres.size - genres.size
            if(invalidCurrent > 0){
                Log.e("!!!", "кол-во битых данных $invalidCurrent")
            }
            TResult.Success<List<GenreDomainModel>, MovieExceptionDomainModel>(genres)
        }.getOrElse { e ->
            TResult.Error(e.toMovieExceptionDomainModel())
        }
    }
}