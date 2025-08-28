package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.model.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IWatchlistRepository {
    fun getAll(): Flow<List<MovieDomainModel>>
    suspend fun add(movie: MovieDomainModel)
    suspend fun remove(movie: MovieDomainModel)
    fun isInWatchlist(id: Int): Flow<Boolean>
}