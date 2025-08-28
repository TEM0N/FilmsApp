package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.model.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IFavoritesRepository {
    fun getAll(): Flow<List<MovieDomainModel>>
    suspend fun add(movie: MovieDomainModel)
    suspend fun remove(movie: MovieDomainModel)
    fun isFavorite(id: Int): Flow<Boolean>
}